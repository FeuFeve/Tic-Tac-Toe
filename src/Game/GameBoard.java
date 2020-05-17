package Game;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

class GameBoard {

    List<List<Tile>> tiles = new ArrayList<>();
    int rows;
    int columns;

    double width;
    double height;

    int winningCombo;
    List<Tile> winningTiles = new ArrayList<>();

    int turn = 1;

    Player currentPlayer;


    GameBoard(int rows, int columns, int winningCombo, double width, double height) {
        currentPlayer = DataManager.player1;
        this.rows = rows;
        this.columns = columns;
        this.winningCombo = winningCombo;
        this.width = width;
        this.height = height;

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
    }

    void switchPlayerTurn() {
        if (currentPlayer == DataManager.player1) {
            currentPlayer = DataManager.player2;
        }
        else {
            currentPlayer = DataManager.player1;
        }
    }

    boolean isFull() {
        return turn == rows * columns;
    }

    Tile getTileAt(int x, int y) {
        return tiles.get(y).get(x);
    }

    private Pair<Tile, Tile> getWinningLineEnds() {
        Tile first = null;
        Tile second = null;

        int firstWeight = -1;
        int secondWeight = 1_000_000;

        for (Tile tile : winningTiles) {
            int weight = tile.y*columns + tile.x;
            if (weight > firstWeight) {
                first = tile;
                firstWeight = weight;
            }
            else if (weight < secondWeight) {
                second = tile;
                secondWeight = weight;
            }
        }

        return new Pair<>(first, second);
    }

    private Pair<Double, Double> getTileMiddleXYCoordinates(Tile tile) {
        double tileWidth = width/columns;
        double tileHeight = height/rows;

        double x = tile.x * tileWidth + tileWidth/2;
        double y = tile.y * tileHeight + tileHeight/2;

        return new Pair<>(x, y);
    }

    Pair<Pair<Double, Double>, Pair<Double, Double>> getWinningLineXYCoordinates() {
        Pair<Tile, Tile> winningLineEnds = getWinningLineEnds();
        Tile first = winningLineEnds.getKey();
        Tile second = winningLineEnds.getValue();

        Pair<Double, Double> firstCoords = getTileMiddleXYCoordinates(first);
        Pair<Double, Double> secondCoords = getTileMiddleXYCoordinates(second);

        return new Pair<>(firstCoords, secondCoords);
    }
}
