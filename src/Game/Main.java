package Game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Yet Another Tic-Tac-Toe Game");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Exiting the application.");
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
