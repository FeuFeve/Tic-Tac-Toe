package Game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GameController {

    @FXML private GridPane grid;
    @FXML private Pane gridToppingPane;

    @FXML private Label currentPlayerTurn;

    @FXML private Label pseudo1;
    @FXML private Label pseudo2;
    @FXML private Label score1;
    @FXML private Label score2;

    private boolean play;


    public void initialize() {
        // Init the score table
        pseudo1.setText(DataManager.player1.pseudo);
        pseudo2.setText(DataManager.player2.pseudo);
        score1.setText(String.valueOf(DataManager.player1.score));
        score2.setText(String.valueOf(DataManager.player2.score));

        // Init grid pane & the game board
        initializeGame(DataManager.gameBoard);

        // Prevent a NullPointerException happening because the current code is inside of the "initialize()" function
        Platform.runLater(() -> {
            Scene scene = grid.getScene();

            // Press the <Enter> key to launch the game with the current parameters
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    launchNewGame();
                }
                else if (event.getCode() == KeyCode.ESCAPE) {
                    try {
                        loadMainMenu();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    private void initializeGame(GameBoard gameBoard) {
        grid.getChildren().clear();

        play = true;
        int rows = DataManager.rows;
        int columns = DataManager.columns;
        int winningCombo = DataManager.winningCombo;

        for (int i = 0; i < rows; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            grid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < columns; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            grid.getRowConstraints().add(rowConstraints);
        }

        if (gameBoard == null) {
            DataManager.gameBoard = new GameBoard(rows, columns, winningCombo, grid.getPrefWidth(), grid.getPrefHeight());
        }

        initializeGameBoard();

        // Init the player turn label
        GameAnimator.changeLabel(currentPlayerTurn, DataManager.gameBoard.currentPlayer.pseudo, DataManager.gameBoard.currentPlayer.color);
    }

    private void initializeGameBoard() {
        GameBoard gameBoard = DataManager.gameBoard;

        for (int y = 0; y < gameBoard.tiles.size(); y++) {

            List<Tile> row = gameBoard.tiles.get(y);
            for (int x = 0; x < row.size(); x++) {

                Tile tile = row.get(x);
                tile.pane.setOnMouseClicked(e -> {
                    if (play && tile.owner == null) {
                        // Let the player play, and update the interface/game board
                        doPlayerTurn(tile, gameBoard);

                        // If no one has won yet and there is still available tiles
                        if (play) {
                            // Check if the other player is an AI, in this case, let the AI play
                            if (!DataManager.gameMode.equals("Player vs Player")) {
                                doAITurn(gameBoard, 500);
                            }
                        }
                    }
                });

                // When loading a save, the game board might have already been used
                if (tile.owner != null) {
                    // Animate the player's shape on the tile
                    GameAnimator.animateClickedTile(tile);

                    // Check if the current player has won
                    if (play && gameBoard.hasWinner) {
                        checkForWinningPattern(tile);
                    }

                    // Check if the game board is full and needs a reset
                    if (play && gameBoard.isFull() && !gameBoard.hasWinner) {
                        System.out.println("No player won this time!");
                        play = false;
                    }
                }

                grid.add(tile.pane, x, y);
            }
        }
    }

    private void doPlayerTurn(Tile tile, GameBoard gameBoard) {
        tile.owner = gameBoard.currentPlayer;
        System.out.println("- " + tile.owner.pseudo + " chose the tile: (" + tile.x + ", " + tile.y + ")");

        // Animate the player's shape on the tile
        GameAnimator.animateClickedTile(tile);

        // Check if the current player has won
        checkForWinningPattern(tile);

        // Check if the game board is full and needs a reset
        if (gameBoard.isFull() && !gameBoard.hasWinner) {
            System.out.println("No player won this time!");
            play = false;
        }
        else {
            gameBoard.turn++;
        }

        // If no one has won yet (and the gameBoard isn't filled), change the player turn
        if (play) {
            gameBoard.switchPlayerTurn();

            // Update the player turn label
            GameAnimator.changeLabel(currentPlayerTurn, gameBoard.currentPlayer.pseudo, gameBoard.currentPlayer.color);
        }
    }

    private void doAITurn(GameBoard gameBoard, int sleepingTimeMillis) {
        // To ensure the player can't play during the AI turn
        gridToppingPane.setDisable(false);

        // Create a task that the AI thread will use
        Runnable AITask = () -> {
            double startTime = System.nanoTime();

            int chosenTile = AI.play(gameBoard.getAvailableTiles());
            for (List<Tile> row : gameBoard.tiles) {
                for (Tile tile : row) {
                    if (tile.owner == null) {
                        if (chosenTile == 0) {
                            // Make the thread sleep if needed so the AI do not play "instantly"
                            double endTime = System.nanoTime();
                            int remainingTime = sleepingTimeMillis - (int)(endTime - startTime)/1_000_000;
                            if (remainingTime > 0) {
                                try {
                                    Thread.sleep(remainingTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Make the AI play (Platform.runLater() to avoid "IllegalStateException: Not on FX application thread" from GameAnimator
                            Platform.runLater(() -> doPlayerTurn(tile, gameBoard));
                            return;
                        }
                        else {
                            chosenTile--;
                        }
                    }
                }
            }
        };

        // Create a thread for the AI, using the AI task. The thread is essentially here to call "Thread.sleep()" without freezing the UI
        Thread AIThread = new Thread(AITask);
        AIThread.start();

        // Re-enable the player to play
        gridToppingPane.setDisable(true);
    }

    private void checkForWinningPattern(Tile tile) {
        // Try in an horizontal line
        checkInLine(tile, 1, 0);

        // Try in an top-left to bottom-right diagonal
        checkInLine(tile, 1, 1);

        // Try in an vertical line
        checkInLine(tile, 0, 1);

        // Try in an top-right to bottom-left diagonal
        checkInLine(tile, -1, 1);
    }

    private void checkInLine(Tile currentTile, int vectorX, int vectorY) {
        int combo = 1;

        // Will be used if the combo is reached to draw a line on it
        DataManager.gameBoard.winningTiles = new ArrayList<>();
        DataManager.gameBoard.winningTiles.add(currentTile);

        // Try in a direction
        combo = checkInDirection(currentTile, combo, vectorX, vectorY);

        // Try in the other direction
        checkInDirection(currentTile, combo, -vectorX, -vectorY);
    }

    private int checkInDirection(Tile currentTile, int currentCombo, int vectorX, int vectorY) {
        GameBoard gameBoard = DataManager.gameBoard;
        Player player = currentTile.owner;
        int vectorMultiplier = 1;

        while (true) {
            try {
                Tile tile = gameBoard.getTileAt(currentTile.x + (vectorX * vectorMultiplier), currentTile.y + (vectorY * vectorMultiplier));
                if (tile.owner == player) {
                    currentCombo++;
                    gameBoard.winningTiles.add(tile);
                    if (currentCombo == gameBoard.winningCombo) {
                        if (!gameBoard.hasWinner) {
                            System.out.println(tile.owner.pseudo + " won!");
                            // Update the winner's score
                            tile.owner.score++;
                            if (tile.owner == DataManager.player1) {
                                GameAnimator.animateScore(score1, DataManager.player1);
                            }
                            else {
                                GameAnimator.animateScore(score2, DataManager.player2);
                            }

                            gameBoard.hasWinner = true;
                        }

                        // Get the coordinates of the winning pattern
                        Pair<Pair<Double, Double>, Pair<Double, Double>> winningLineCoordinates = gameBoard.getWinningLineXYCoordinates();
                        double x1 = winningLineCoordinates.getKey().getKey();
                        double y1 = winningLineCoordinates.getKey().getValue();
                        double x2 = winningLineCoordinates.getValue().getKey();
                        double y2 = winningLineCoordinates.getValue().getValue();

                        // Draw a line to indicate the winning pattern
                        GameAnimator.animateWinningLine(gridToppingPane, x1, y1, x2, y2);

                        play = false;
                        return currentCombo;
                    }
                    vectorMultiplier++;
                }
                else {
                    return currentCombo;
                }
            } catch (IndexOutOfBoundsException e) {
                return currentCombo;
            }
        }
    }

    @FXML
    private void launchNewGame() {
        System.out.println("##############################");
        System.out.println(DataManager.rows + " " + DataManager.columns + " " + DataManager.winningCombo);
        gridToppingPane.getChildren().clear();
        gridToppingPane.setDisable(true);
        DataManager.gameBoard = null;
        initialize();
    }

    @FXML
    private void saveGame() throws IOException {
        DataManager.save();
    }

    @FXML
    private void loadGame() throws IOException, ClassNotFoundException {
        // Get the stage
        Stage window = (Stage) grid.getScene().getWindow();

        // Create a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a save");
        fileChooser.setInitialDirectory(new File(DataManager.saveDirPath));

        // Open the file chooser (file explorer) and let the user select a save he wants to load
        File saveFile = fileChooser.showOpenDialog(window);
        if (saveFile == null) {
            return;
        }

        // Remove the winning line if there is one
        gridToppingPane.getChildren().clear();
        gridToppingPane.setDisable(true);

        // Let the data manager load the information from the file
        DataManager.load(saveFile);

        // Re-initialize
        initialize();

        // Reset the stage window's title
        window.setTitle("Yet Another Tic-Tac-Toe Game (" + DataManager.gameMode + ")");
    }

    @FXML
    private void loadMainMenu() throws IOException {
        // Get the game scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Get the stage
        Stage window = (Stage) grid.getScene().getWindow();

        // Set the game scene to the stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Yet Another Tic-Tac-Toe Game");
    }
}