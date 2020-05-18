package Game;

import java.io.Serializable;

class Save implements Serializable {

    int rows;
    int columns;
    int winningCombo;

    String gameMode;
    GameBoard gameBoard;
    Player player1;
    Player player2;


    Save(int rows, int columns, int winningCombo, String gameMode, GameBoard gameBoard, Player player1, Player player2) {
        this.rows = rows;
        this.columns = columns;
        this.winningCombo = winningCombo;

        this.gameMode = gameMode;
        this.gameBoard = gameBoard;
        this.player1 = player1;
        this.player2 = player2;
    }
}
