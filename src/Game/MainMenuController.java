package Game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML private ChoiceBox<String> gameModeBox;
    @FXML private TextField pseudo1;
    @FXML private TextField pseudo2;
    @FXML private Button playButton;

    // Images
    @FXML private ImageView player1Cross1;
    @FXML private ImageView player1Cross2;
    @FXML private ImageView player2Circle1;
    @FXML private ImageView player2Circle2;

    // Vars
    ObservableList<String> gameModes = FXCollections.observableArrayList("Player vs Player", "Player vs Easy AI", "Player vs Normal AI", "Player vs Hard AI");


    @FXML
    private void initialize() {
        // Init the game mode choice box
        gameModeBox.setValue(gameModes.get(0));
        gameModeBox.setItems(gameModes);
    }

    @FXML
    private void startGame(ActionEvent event) throws IOException {
        String gameMode = gameModeBox.getValue();
        String player1 = pseudo1.getText();
        String player2 = pseudo2.getText();

        // Set the pseudos if they are not
        if (player1.isBlank()) {
            player1 = "Player 1";
        }
        if (player2.isBlank()) {
            player2 = "Player 2";
        }

        System.out.println("Starting a new " + gameMode + " game with " + player1 + " and " + player2);

        // Get the game scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("game_window.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Get the stage
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Set the game scene to the stage
        window.setScene(gameScene);
        window.show();
    }
}
