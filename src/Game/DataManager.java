package Game;

import java.io.*;

class DataManager {

    static int rows = 3;
    static int columns = 3;
    static int winningCombo = 3;

    static String gameMode;
    static GameBoard gameBoard;
    static Player player1;
    static Player player2;

    static String saveDirPath = "Saves/";


    static void setGameMode(String gameMode) {
        DataManager.gameMode = gameMode;
    }

    static void setPlayer1(String pseudo) {
        player1 = new Player(pseudo, Sprites.crossPath, Colors.player1Background);
    }

    static void setPlayer2(String pseudo) {
        player2 = new Player(pseudo, Sprites.circlePath, Colors.player2Background);
    }

    static void makeSaveDir() {
        new File(saveDirPath).mkdirs();
    }

    static void save() throws IOException {
        // Make sure that the "Save" directory is created
        makeSaveDir();

        // Save the data inside of non-static members for quick/easy serialization
        Save save = new Save(rows, columns, winningCombo, gameMode, gameBoard, player1, player2);
        String savePath = saveDirPath + player1.pseudo + " vs " + player2.pseudo + ".ser";

        // Serialize the data
        FileOutputStream fileOut = new FileOutputStream(savePath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(save);
        out.close();
        fileOut.close();

        System.out.println("Saved the game in: \"" + savePath + "\"");
    }

    static void load(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(file.getPath());
        ObjectInputStream in = new ObjectInputStream(fileIn);

        Save save = (Save) in.readObject();
        rows = save.rows;
        columns = save.columns;
        winningCombo = save.winningCombo;

        gameMode = save.gameMode;
        gameBoard = save.gameBoard;
        player1 = save.player1;
        player2 = save.player2;

        in.close();
        fileIn.close();

        // JavaFX objects are not serializable, so we have to re-init them (marked as transient in the code)
        gameBoard.initPanes();
        player1.initTransients();
        player2.initTransients();
    }
}
