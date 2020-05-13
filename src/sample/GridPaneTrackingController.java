package sample;

import javafx.fxml.FXML;
import javafx.scene.layout.*;

import java.util.List;


public class GridPaneTrackingController {

    @FXML
    private GridPane grid;

    private GameBoard gameBoard;


    public void initialize() {
        int rows = 3;
        int columns = 3;

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
        Player player1 = new Player("Player 1", Colors.player1Color);
        Player player2 = new Player("Player 2", Colors.player2Color);

        createGameBoard(player1, player2, rows, columns);
    }

    private void createGameBoard(Player player1, Player player2, int rows, int columns) {
        gameBoard = new GameBoard(player1, player2, rows, columns);

        for (int x = 0; x < gameBoard.tiles.size(); x++) {

            List<Tile> row = gameBoard.tiles.get(x);
            for (int y = 0; y < row.size(); y++) {

                Tile tile = row.get(y);
                tile.pane.setOnMouseClicked(e -> {
                    if (tile.owner == null) {
                        System.out.println("Clicked a tile.");
                        tile.owner = gameBoard.currentPlayer;
                        tile.pane.setStyle("-fx-background-color: " + gameBoard.currentPlayer.color);
                        gameBoard.switchPlayerTurn();
                    }
                });
                grid.add(tile.pane, x, y);
            }
        }
    }
}