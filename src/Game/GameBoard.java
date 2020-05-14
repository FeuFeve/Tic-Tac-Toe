package Game;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {

    List<List<Tile>> tiles = new ArrayList<>();
    int rows;
    int columns;
    int winningCombo;

    int turn = 1;

    Player player1;
    Player player2;
    Player currentPlayer;


    GameBoard(Player player1, Player player2, int rows, int columns, int winningCombo) {
        this.rows = rows;
        this.columns = columns;
        this.winningCombo = winningCombo;

        for (int y = 0; y < rows; y++) {

            List<Tile> row = new ArrayList<>();
            for (int x = 0; x < columns; x++) {

                if (x == 0 && y == 0) {                     // Top left corner
                    row.add(new Tile(x, y, Sprites.backgroundAngle));
                }
                else if (x == rows-1 && y == 0) {           // Top right corner
                    row.add(new Tile(x, y, Sprites.backgroundAngle, 90));
                }
                else if (x == rows-1 && y == columns-1) {   // Bottom right corner
                    row.add(new Tile(x, y, Sprites.backgroundAngle, 180));
                }
                else if (x == 0 && y == columns-1) {        // Bottom left corner
                    row.add(new Tile(x, y, Sprites.backgroundAngle, 270));
                }
                else if (y == 0) {                          // Top row
                    row.add(new Tile(x, y, Sprites.backgroundEdge));
                }
                else if (x == rows-1) {                     // Right column
                    row.add(new Tile(x, y, Sprites.backgroundEdge, 90));
                }
                else if (y == columns-1) {                  // Bottom row
                    row.add(new Tile(x, y, Sprites.backgroundEdge, 180));
                }
                else if (x == 0) {                          // Left column
                    row.add(new Tile(x, y, Sprites.backgroundEdge, 270));
                }
                else {                                      // Middle tiles
                    row.add(new Tile(x, y, Sprites.backgroundMiddle));
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

    boolean isFull() {
        return turn == rows * columns;
    }

    Tile getTileAt(int x, int y) {
        return tiles.get(y).get(x);
    }
}
