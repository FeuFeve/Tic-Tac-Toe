package Game;

import java.io.Serializable;

class Save implements Serializable {

    String gameMode;
    GameBoard gameBoard;
    Player player1;
    Player player2;


    Save(String gameMode, GameBoard gameBoard, Player player1, Player player2) {
        this.gameMode = gameMode;
        this.gameBoard = gameBoard;
        this.player1 = player1;
        this.player2 = player2;
    }
}
