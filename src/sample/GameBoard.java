package sample;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {

    List<List<Tile>> tiles = new ArrayList<>();


    GameBoard(int rows, int columns) {

        for (int x = 0; x < rows; x++) {

            List<Tile> row = new ArrayList<>();
            for (int y = 0; y < columns; y++) {
                row.add(new Tile());
            }

            tiles.add(row);
        }
    }
}
