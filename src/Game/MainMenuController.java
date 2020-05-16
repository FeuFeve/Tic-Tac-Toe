package Game;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMenuController {

    @FXML private Label gameTitle;

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

        // Animate the title
        ScaleTransition st = new ScaleTransition(Duration.millis(2000), gameTitle);
        st.setByX(-0.2);
        st.setByY(-0.2);
        st.setAutoReverse(true);
        st.setCycleCount(Timeline.INDEFINITE);
        st.play();

        // Animate the crosses/circles
        List<FadeTransition> fadeTransitions = new ArrayList<>();
        fadeTransitions.add(new FadeTransition(Duration.millis(2000), player1Cross1));
        fadeTransitions.add(new FadeTransition(Duration.millis(2000), player1Cross2));
        fadeTransitions.add(new FadeTransition(Duration.millis(2000), player2Circle1));
        fadeTransitions.add(new FadeTransition(Duration.millis(2000), player2Circle2));

        for (FadeTransition ft : fadeTransitions) {
            ft.setFromValue(1);
            ft.setToValue(0.1);
            ft.setAutoReverse(true);
            ft.setCycleCount(Timeline.INDEFINITE);
            ft.play();
        }
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
