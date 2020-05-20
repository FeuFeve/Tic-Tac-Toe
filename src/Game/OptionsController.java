package Game;

import Game.JavaAI.MultiLayerPerceptron;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class OptionsController {

    @FXML private Button backToMainMenuButton;
    @FXML private Button trainMediumAIButton;
    @FXML private Button trainHardAIButton;

    @FXML private TextField rowsAndColsEntry;
    @FXML private TextField comboEntry;
    @FXML private TextField mediumAITrainsEntry;
    @FXML private TextField hardAITrainsEntry;

    @FXML private Label savedPopUp;
    @FXML private Label mediumAIGamesCount;
    @FXML private Label hardAIGamesCount;
    @FXML private Label progressPercentage;

    @FXML private ProgressBar trainingSessionProgress;

    // Vars
    MultiLayerPerceptron mediumAI;
    MultiLayerPerceptron hardAI;

    private static boolean isTraining;


    @FXML
    private void initialize() {
        // Initialize game board parameters
        rowsAndColsEntry.setText(String.valueOf(DataManager.rows));
        comboEntry.setText(String.valueOf(DataManager.winningCombo));

        // Get the AIs
        mediumAI = MultiLayerPerceptron.load(AI.mediumAIPath);
        hardAI = MultiLayerPerceptron.load(AI.hardAIPath);

        // Set the AIs totals
        mediumAIGamesCount.setText(NumberFormater.formatNumber(mediumAI.trainingCount));
        hardAIGamesCount.setText(NumberFormater.formatNumber(hardAI.trainingCount));

        // Prevent a NullPointerException happening because the current code is inside of the "initialize()" function
        Platform.runLater(() -> {
            Scene scene = backToMainMenuButton.getScene();

            // Press the <Enter> key to launch the game with the current parameters
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE && !isTraining) {
                    try {
                        loadMainMenu();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    // Disable the possibility go back to the main menu or launch another train when already training an AI
    void setIsTraining(boolean isTraining) {
        OptionsController.isTraining = isTraining;
        Platform.runLater(() -> {
            backToMainMenuButton.setDisable(isTraining);
            trainMediumAIButton.setDisable(isTraining);
            trainHardAIButton.setDisable(isTraining);
        });
    }

    @FXML
    private void loadMainMenu() throws IOException {
        // Get the game scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Get the stage
        Stage window = (Stage) backToMainMenuButton.getScene().getWindow();

        // Set the game scene to the stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Yet Another Tic-Tac-Toe Game");
    }

    @FXML
    private void saveOptions() {
        // Only save the options when the number of rows/columns is 10 or under, and when the combo is less than or equal to the number of rows/columns
        try {
            int rowsAndColumns = Integer.parseInt(rowsAndColsEntry.getText());
            int combo = Integer.parseInt(comboEntry.getText());
            if (combo > rowsAndColumns || rowsAndColumns > 10 || rowsAndColumns < 3 || combo < 3) {
                return;
            }

            DataManager.rows = rowsAndColumns;
            DataManager.columns = rowsAndColumns;
            DataManager.winningCombo = combo;

            GameAnimator.animateFadingNode(savedPopUp, 0, 1, 500, 2);
            System.out.println("Saved the options.");
        } catch (Exception ignored) {  }
    }

    @FXML
    private void resetOptions() {
        try {
            DataManager.rows = 3;
            DataManager.columns = 3;
            DataManager.winningCombo = 3;

            rowsAndColsEntry.setText("3");
            comboEntry.setText("3");

            GameAnimator.animateFadingNode(savedPopUp, 0, 1, 500, 2);
            System.out.println("Options reset.");
        } catch (Exception ignored) {  }
    }

    @FXML
    private void trainMediumAI() {
        int epochs;

        // Do nothing if it is not a valid number
        try {
            epochs = Integer.parseInt(mediumAITrainsEntry.getText());
        } catch (NumberFormatException e) {
            return;
        }

        // Do nothing if the number is above 10M (to avoid unintentionally long trainings)
        if (epochs > 10_000_000 || epochs < 100) {
            return;
        }

        // Train the medium difficulty AI on a background thread so the UI isn't frozen
        Runnable trainAITask = () -> {
            setIsTraining(true);
            int totalEpochs = AI.train(AI.mediumAIPath, epochs, false);
            setIsTraining(false);

            // Force to run this lines on the UI thread
            Platform.runLater(() -> {
                mediumAITrainsEntry.setText("");
                mediumAIGamesCount.setText(NumberFormater.formatNumber(totalEpochs));
            });
        };
        Thread trainAIThread = new Thread(trainAITask);
        trainAIThread.setDaemon(true);
        trainAIThread.start();

        launchProgressBarListener(trainAIThread);
    }

    @FXML
    private void trainHardAI() {
        int epochs;

        // Do nothing if it is not a valid number
        try {
            epochs = Integer.parseInt(hardAITrainsEntry.getText());
        } catch (NumberFormatException e) {
            return;
        }

        // Do nothing if the number is above 10M (to avoid unintentionally long trainings)
        if (epochs > 10_000_000 || epochs < 100) {
            return;
        }

        // Train the medium difficulty AI on a background thread so the UI isn't frozen
        Runnable trainAITask = () -> {
            setIsTraining(true);
            int totalEpochs = AI.train(AI.hardAIPath, epochs, false);
            setIsTraining(false);

            // Force to run this lines on the UI thread
            Platform.runLater(() -> {
                hardAITrainsEntry.setText("");
                hardAIGamesCount.setText(NumberFormater.formatNumber(totalEpochs));
            });
        };
        Thread trainAIThread = new Thread(trainAITask);
        trainAIThread.setDaemon(true);
        trainAIThread.start();

        launchProgressBarListener(trainAIThread);
    }

    // Actualise the progress bar during the training
    private void launchProgressBarListener(Thread trainAIThread) {
        Runnable listenToAIProgressTask = () -> {
            int currentProgress = 0;
            while (trainAIThread.isAlive()) {
                double progress = (double)AI.currentTrainingCount / AI.currentTrainingTotal;
                trainingSessionProgress.setProgress(progress);

                if (progress * 100 > currentProgress) {
                    int finalCurrentProgress = currentProgress;
                    Platform.runLater(() -> progressPercentage.setText(finalCurrentProgress + "%"));
                    currentProgress++;
                }
            }

            if (AI.currentTrainingCount == AI.currentTrainingTotal) {
                Platform.runLater(() -> progressPercentage.setText("100%"));
            }
        };
        Thread listenToAIProgressThread = new Thread(listenToAIProgressTask);
        listenToAIProgressThread.setDaemon(true);
        listenToAIProgressThread.start();
    }
}
