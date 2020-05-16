package Game;

class DataManager {

    static String gameMode;
    static GameBoard gameBoard;
    static Player player1;
    static Player player2;


    static void setGameMode(String gameMode) {
        DataManager.gameMode = gameMode;
    }

    static void setGameBoard(GameBoard gameBoard) {
        DataManager.gameBoard = gameBoard;
    }

    static void setPlayer1(String pseudo) {
        player1 = new Player(pseudo, Sprites.cross);
    }

    static void setPlayer2(String pseudo) {
        player2 = new Player(pseudo, Sprites.circle);
    }
}
