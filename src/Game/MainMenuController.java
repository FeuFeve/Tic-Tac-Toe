package Game;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML private Label gameTitle;
    @FXML private Label optionsLabel;

    @FXML private ChoiceBox<String> gameModeBox;
    @FXML private TextField pseudo1;
    @FXML private TextField pseudo2;

    // Images
    @FXML private ImageView player1Cross1;
    @FXML private ImageView player1Cross2;
    @FXML private ImageView player2Circle1;
    @FXML private ImageView player2Circle2;

    // Splash examples
    @FXML private ImageView splashExample1;
    @FXML private ImageView splashExample2;
    @FXML private ImageView splashExample3;
    @FXML private ImageView splashExample4;

    @FXML private Button playButton;
    @FXML private Button optionsButton;

    @FXML private Label trainingMessage1;
    @FXML private Label trainingMessage2;

    // Vars
    ObservableList<String> gameModes = FXCollections.observableArrayList("Player vs Player", "Player vs Easy AI", "Player vs Medium AI", "Player vs Hard AI");
    private static boolean isTraining;


    @FXML
    private void initialize() {
        // Train the medium and hard-difficulty AIs if they does not exist, and do it on a background thread so the UI isn't frozen
        Runnable trainAIsTask = () -> {
            setIsTraining(true);
            System.out.println("Initializing the Medium/Hard AIs...");
            AI.initMediumAndHardAIs();
            setIsTraining(false);
        };
        Thread trainAIsThread = new Thread(trainAIsTask);
        trainAIsThread.setDaemon(true);
        trainAIsThread.start();

        // Init the game mode choice box
        if (DataManager.gameMode == null) {
            gameModeBox.setValue(gameModes.get(0));
        }
        else {
            gameModeBox.setValue(DataManager.gameMode);
        }
        gameModeBox.setItems(gameModes);

        // Init the players' pseudos
        if (DataManager.player1 != null && !DataManager.player1.pseudo.equals("Player 1")) {
            pseudo1.setText(DataManager.player1.pseudo);
        }
        if (DataManager.player2 != null && !DataManager.player2.pseudo.equals("Player 2")) {
            pseudo2.setText(DataManager.player2.pseudo);
        }

        // Prevent a NullPointerException happening because the current code is inside of the "initialize()" function
        Platform.runLater(() -> {
            Scene scene = gameTitle.getScene();

            // Press the <Enter> key to launch the game with the current parameters
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER && !isTraining) {
                    try {
                        startGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        // Animate the title
        GameAnimator.animateTitle(gameTitle);

        // Animate the crosses/circles
        GameAnimator.animateFadingNode(player1Cross1, 1, 0.1, 2000, Timeline.INDEFINITE);
        GameAnimator.animateFadingNode(player1Cross2, 1, 0.1, 2000, Timeline.INDEFINITE);
        GameAnimator.animateFadingNode(player2Circle1, 1, 0.1, 2000, Timeline.INDEFINITE);
        GameAnimator.animateFadingNode(player2Circle2, 1, 0.1, 2000, Timeline.INDEFINITE);

        // Animate the splash examples
        GameAnimator.animateFadingNode(splashExample1, 0, 1, 3000, Timeline.INDEFINITE);
        GameAnimator.animateFadingNode(splashExample2, 0, 1, 2500, Timeline.INDEFINITE);
        GameAnimator.animateFadingNode(splashExample3, 0, 1, 4000, Timeline.INDEFINITE);
        GameAnimator.animateFadingNode(splashExample4, 0, 1, 5000, Timeline.INDEFINITE);

        // Animate the training messages
        GameAnimator.animateFadingNode(trainingMessage1, 1, 0.1, 2000, Timeline.INDEFINITE);
        GameAnimator.animateFadingNode(trainingMessage2, 1, 0.1, 2000, Timeline.INDEFINITE);
    }

    // Disable the possibility to launch a game or go to the options when initializing the medium/hard AIs
    void setIsTraining(boolean isTraining) {
        MainMenuController.isTraining = isTraining;
        Platform.runLater(() -> {
            playButton.setDisable(isTraining);
            optionsButton.setDisable(isTraining);
            optionsLabel.setVisible(!isTraining);
            trainingMessage1.setVisible(isTraining);
            trainingMessage2.setVisible(isTraining);
        });
    }

    @FXML
    private void startGame() throws IOException {
        // Reset the game board if there is one
        DataManager.gameBoard = null;

        // Reset the game board parameters if launched with Medium/Hard AIs as they only support 3 by 3 game boards
        String gameMode = gameModeBox.getValue();
        if (gameMode.equals("Player vs Medium AI") || gameMode.equals("Player vs Hard AI")) {
            DataManager.rows = 3;
            DataManager.columns = 3;
            DataManager.winningCombo = 3;
        }

        String player1 = pseudo1.getText();
        String player2 = pseudo2.getText();

        // Set the pseudos if they are not
        if (player1.isBlank()) {
            player1 = "Player 1";
        }
        if (player2.isBlank()) {
            player2 = "Player 2";
        }

        DataManager.setGameMode(gameMode);
        DataManager.setPlayer1(player1);
        DataManager.setPlayer2(player2);

        System.out.println("Starting a new " + gameMode + " game with " + player1 + " and " + player2);

        // Get the game scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("game_window.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Get the stage
        Stage window = (Stage) gameTitle.getScene().getWindow();

        // Set the game scene to the stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Yet Another Tic-Tac-Toe Game (" + gameMode + ")");
    }

    @FXML
    private void goToOptions() throws IOException {
        // Get the game scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("options.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Get the stage
        Stage window = (Stage) gameTitle.getScene().getWindow();

        // Set the game scene to the stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Yet Another Tic-Tac-Toe Game (Options)");
    }
}
