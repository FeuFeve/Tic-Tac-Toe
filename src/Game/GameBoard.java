package Game;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class GameBoard implements Serializable {

    List<List<Tile>> tiles = new ArrayList<>();
    int rows;
    int columns;

    double width;
    double height;

    int winningCombo;
    List<Tile> winningTiles = new ArrayList<>();

    int turn = 1;

    Player currentPlayer;
    boolean hasWinner;


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
                    row.add(new Tile(x, y, Sprites.backgroundAnglePath));
                }
                else if (x == rows-1 && y == 0) {           // Top right corner
                    Tile tile = new Tile(x, y, Sprites.backgroundAnglePath);
                    tile.resetRotation(90);
                    row.add(tile);
                }
                else if (x == rows-1 && y == columns-1) {   // Bottom right corner
                    Tile tile = new Tile(x, y, Sprites.backgroundAnglePath);
                    tile.resetRotation(180);
                    row.add(tile);
                }
                else if (x == 0 && y == columns-1) {        // Bottom left corner
                    Tile tile = new Tile(x, y, Sprites.backgroundAnglePath);
                    tile.resetRotation(270);
                    row.add(tile);
                }
                else if (y == 0) {                          // Top row
                    row.add(new Tile(x, y, Sprites.backgroundEdgePath));
                }
                else if (x == rows-1) {                     // Right column
                    Tile tile = new Tile(x, y, Sprites.backgroundEdgePath);
                    tile.resetRotation(90);
                    row.add(tile);
                }
                else if (y == columns-1) {                  // Bottom row
                    Tile tile = new Tile(x, y, Sprites.backgroundEdgePath);
                    tile.resetRotation(180);
                    row.add(tile);
                }
                else if (x == 0) {                          // Left column
                    Tile tile = new Tile(x, y, Sprites.backgroundEdgePath);
                    tile.resetRotation(270);
                    row.add(tile);
                }
                else {                                      // Middle tiles
                    row.add(new Tile(x, y, Sprites.backgroundMiddlePath));
                }
            }

            tiles.add(row);
        }
    }

    // Used when de-serializing (loading) a game
    void initPanes() {
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.initTransients();
            }
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

    int getAvailableTiles() {
        return (rows * columns) - turn + 1;
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
            if (weight < secondWeight) {
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
