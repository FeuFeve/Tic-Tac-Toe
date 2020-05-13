package sample;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {

    List<List<Tile>> tiles = new ArrayList<>();

    Player player1;
    Player player2;
    Player currentPlayer;


    GameBoard(Player player1, Player player2, int rows, int columns) {

        for (int y = 0; y < columns; y++) {

            List<Tile> row = new ArrayList<>();
            for (int x = 0; x < rows; x++) {
                if (x == 0 && y == 0) {                     // Top left corner
                    row.add(new Tile(Sprites.backgroundAngle));
                }
                else if (x == 0 && y == columns-1) {        // Top right corner
                    row.add(new Tile(Sprites.backgroundAngle, 90));
                }
                else if (x == rows-1 && y == columns-1) {   // Bottom right corner
                    row.add(new Tile(Sprites.backgroundAngle, 180));
                }
                else if (x == rows-1 && y == 0) {           // Bottom left corner
                    row.add(new Tile(Sprites.backgroundAngle, 270));
                }
                else if (x == 0) {                          // Top row
                    row.add(new Tile(Sprites.backgroundEdge));
                }
                else if (y == columns-1) {                  // Right column
                    row.add(new Tile(Sprites.backgroundEdge, 90));
                }
                else if (x == rows-1) {                     // Bottom row
                    row.add(new Tile(Sprites.backgroundEdge, 180));
                }
                else if (y == 0) {                          // Left column
                    row.add(new Tile(Sprites.backgroundEdge, 270));
                }
                else {                                      // Middle tiles
                    row.add(new Tile(Sprites.backgroundMiddle));
                }
            }

            tiles.add(row);
        }

        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;
    }

    void switchPlayerTurn() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        }
        else {
            currentPlayer = player1;
        }
    }
}
