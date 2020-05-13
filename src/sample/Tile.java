package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Tile {

    Pane pane;

    Player owner;

    ImageView background;


    Tile(Image backgroundImage) {
        background = new ImageView(backgroundImage);
        pane = new Pane();
        pane.getChildren().add(background);
        background.fitWidthProperty().bind(pane.widthProperty());
        background.fitHeightProperty().bind(pane.heightProperty());
    }

    Tile(Image backgroundImage, int rotation) {
        this(backgroundImage);
        background.setRotate(rotation);
    }
}
