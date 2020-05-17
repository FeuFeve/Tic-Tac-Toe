package Game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GameController {

    @FXML private GridPane grid;
    @FXML private Pane gridToppingPane;

    private GameBoard gameBoard;
    private boolean play = true;


    public void initialize() {
        int rows = 3;
        int columns = 3;
        int winningCombo = 3;

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

        gameBoard = new GameBoard(rows, columns, winningCombo, grid.getPrefWidth(), grid.getPrefHeight());

        initializeGameBoard();
    }

    private void initializeGameBoard() {

        for (int y = 0; y < gameBoard.tiles.size(); y++) {

            List<Tile> row = gameBoard.tiles.get(y);
            for (int x = 0; x < row.size(); x++) {

                Tile tile = row.get(x);
                tile.pane.setOnMouseClicked(e -> {
                    if (play && tile.owner == null) {
                        tile.owner = gameBoard.currentPlayer;

                        // Set the player's shape on the tile
                        ImageView playerShape = new ImageView(gameBoard.currentPlayer.shape);
                        tile.pane.getChildren().add(playerShape);
                        playerShape.fitWidthProperty().bind(tile.pane.widthProperty());
                        playerShape.fitHeightProperty().bind(tile.pane.heightProperty());

                        // Check if the current player has won
                        checkForWinningPattern(tile);

                        // Check if the game board is full and needs a reset
                        if (gameBoard.isFull()) {
                            System.out.println("No player won this time!");
                            play = false;
                        }
                        else {
                            gameBoard.turn++;
                        }

                        gameBoard.switchPlayerTurn();
                    }
                });
                grid.add(tile.pane, x, y);
            }
        }
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
        gameBoard.winningTiles = new ArrayList<>();
        gameBoard.winningTiles.add(currentTile);

        // Try in a direction
        combo = checkInDirection(currentTile, combo, vectorX, vectorY);

        // Try in the other direction
        checkInDirection(currentTile, combo, -vectorX, -vectorY);
    }

    private int checkInDirection(Tile currentTile, int currentCombo, int vectorX, int vectorY) {
        Player player = currentTile.owner;
        int vectorMultiplier = 1;

        while (true) {
            try {
                Tile tile = gameBoard.getTileAt(currentTile.x + (vectorX * vectorMultiplier), currentTile.y + (vectorY * vectorMultiplier));
                if (tile.owner == player) {
                    currentCombo++;
                    gameBoard.winningTiles.add(tile);
                    if (currentCombo == gameBoard.winningCombo) {
                        System.out.println(tile.owner.pseudo + " won!");

                        Pair<Pair<Double, Double>, Pair<Double, Double>> winningLineCoordinates = gameBoard.getWinningLineXYCoordinates();
                        double x1 = winningLineCoordinates.getKey().getKey();
                        double y1 = winningLineCoordinates.getKey().getValue();
                        double x2 = winningLineCoordinates.getValue().getKey();
                        double y2 = winningLineCoordinates.getValue().getValue();

                        GameAnimator.animateWinningLine(gridToppingPane, x1, y1, x2, y2);

                        play = false;
                        return currentCombo;
                    }
                    vectorMultiplier++;
                }
                else {
                    return currentCombo;
                }
            }
            catch (IndexOutOfBoundsException e) {
                return currentCombo;
            }
        }
    }

    @FXML
    private void loadMainMenu(ActionEvent event) throws IOException {
        // Get the game scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Get the stage
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Set the game scene to the stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Yet Another Tic-Tac-Toe Game");
    }
}