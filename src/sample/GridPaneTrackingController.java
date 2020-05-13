package sample;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
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
        Player player1 = new Player("Player 1", Sprites.cross);
        Player player2 = new Player("Player 2", Sprites.circle);

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

                        // Set the player's shape on the tile
                        ImageView playerShape = new ImageView(gameBoard.currentPlayer.shape);
                        tile.pane.getChildren().add(playerShape);
                        playerShape.fitWidthProperty().bind(tile.pane.widthProperty());
                        playerShape.fitHeightProperty().bind(tile.pane.heightProperty());

                        gameBoard.switchPlayerTurn();
                    }
                });
                grid.add(tile.pane, x, y);
            }
        }
    }
}