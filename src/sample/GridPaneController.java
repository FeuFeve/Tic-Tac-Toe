package sample;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;


public class GridPaneController {

    @FXML
    private GridPane grid;

    private GameBoard gameBoard;
    private boolean toReset;


    public void initialize() {
        toReset = true;
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

        // TODO : modify
        Player player1 = new Player("Player 1", Sprites.cross);
        Player player2 = new Player("Player 2", Sprites.circle);

        gameBoard = new GameBoard(player1, player2, rows, columns, winningCombo);

        initializeGameBoard();
    }

    private void initializeGameBoard() {

        for (int y = 0; y < gameBoard.tiles.size(); y++) {

            List<Tile> row = gameBoard.tiles.get(y);
            for (int x = 0; x < row.size(); x++) {

                Tile tile = row.get(x);
                tile.pane.setOnMouseClicked(e -> {
                    toReset = false;
                    if (tile.owner == null) {
                        tile.owner = gameBoard.currentPlayer;

                        // Set the player's shape on the tile
                        ImageView playerShape = new ImageView(gameBoard.currentPlayer.shape);
                        tile.pane.getChildren().add(playerShape);
                        playerShape.fitWidthProperty().bind(tile.pane.widthProperty());
                        playerShape.fitHeightProperty().bind(tile.pane.heightProperty());

                        // Check if the current player has won
                        checkForWinningPattern(tile);

                        if (!toReset) {
                            // Check if the game board is full and needs a reset
                            if (gameBoard.isFull()) {
                                System.out.println("No player won this time!");
                                initialize();
                            }
                            else {
                                gameBoard.turn++;
                            }

                            gameBoard.switchPlayerTurn();
                        }
                        else {
                            toReset = false;
                        }
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
                    if (currentCombo == gameBoard.winningCombo) {
                        System.out.println(tile.owner.pseudo + " won!");
                        initialize();
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
}