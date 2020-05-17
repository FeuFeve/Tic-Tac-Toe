package Game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

class Tile {

    Pane pane;

    int x;
    int y;
    Player owner;

    ImageView background;


    Tile(int x, int y, Image backgroundImage) {
        this.x = x;
        this.y = y;

        // Background image
        background = new ImageView(backgroundImage);
        pane = new Pane();
        pane.getChildren().add(background);
        background.fitWidthProperty().bind(pane.widthProperty());
        background.fitHeightProperty().bind(pane.heightProperty());
    }

    Tile(int x, int y, Image backgroundImage, int rotation) {
        this(x, y, backgroundImage);
        background.setRotate(rotation);
    }
}
