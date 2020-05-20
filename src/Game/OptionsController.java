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
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class OptionsController {

    @FXML private Button backToMainMenuButton;

    @FXML private TextField rowsAndColsEntry;
    @FXML private TextField comboEntry;
    @FXML private TextField mediumAITrainsEntry;
    @FXML private TextField hardAITrainsEntry;

    @FXML private Label savedPopUp;
    @FXML private Label mediumAIGamesCount;
    @FXML private Label hardAIGamesCount;

    @FXML private ProgressBar trainingSessionProgress;

    // Vars
    MultiLayerPerceptron mediumAI;
    MultiLayerPerceptron hardAI;


    @FXML
    private void initialize() {
        // Initialize game board parameters
        rowsAndColsEntry.setText(String.valueOf(DataManager.rows));
        comboEntry.setText(String.valueOf(DataManager.winningCombo));

        // Get the AIs
        mediumAI = MultiLayerPerceptron.load(AI.mediumAIPath);
        hardAI = MultiLayerPerceptron.load(AI.hardAIPath);

        // Set the AIs totals
        // Format big numbers into more readable ones
        Locale locale  = new Locale("en", "US");
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);
        mediumAIGamesCount.setText(decimalFormat.format(mediumAI.trainingCount));
        hardAIGamesCount.setText(decimalFormat.format(hardAI.trainingCount));
    }

    // Disable the possibility to launch a game or go to the options when initializing the medium/hard AIs
    void setIsTraining(boolean isTraining) {
        MainMenuController.isTraining = isTraining;
        Platform.runLater(() -> backToMainMenuButton.setDisable(isTraining));
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
        try {
            int rowsAndColumns = Integer.parseInt(rowsAndColsEntry.getText());
            int combo = Integer.parseInt(comboEntry.getText());
            if (combo > rowsAndColumns) {
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

            GameAnimator.animateFadingNode(savedPopUp, 0, 1, 2000, 1);
            System.out.println("Options reset.");
        } catch (Exception ignored) {  }
    }
}
