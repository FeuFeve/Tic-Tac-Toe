package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.List;


public class GridPaneTrackingController {

    @FXML
    private GridPane grid;

    private GameBoard gameBoard;


    public void initialize() {
        int numCols = 3;
        int numRows = 3;

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            grid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            grid.getRowConstraints().add(rowConstraints);
        }

        createGameBoard(numRows, numCols);
    }

    private void createGameBoard(int numRows, int numCols) {
        gameBoard = new GameBoard(numRows, numCols);

        for (int x = 0; x < gameBoard.tiles.size(); x++) {

            List<Tile> row = gameBoard.tiles.get(x);
            for (int y = 0; y < row.size(); y++) {

                Tile tile = row.get(y);
                tile.pane.setOnMouseClicked(e -> {
                    if (tile.isClicked) {
                        tile.pane.setStyle("-fx-background-color: " + tile.baseColor);
                    }
                    else {
                        tile.pane.setStyle("-fx-background-color: " + tile.clickedColor);
                    }
                    tile.isClicked = !tile.isClicked;
                });
                grid.add(tile.pane, x, y);
            }
        }
    }
}