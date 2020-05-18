package Game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.Serializable;

class Tile implements Serializable {

    transient Pane pane;

    int x;
    int y;
    Player owner;

    String backgroundPath;
    transient ImageView background;
    int rotation;


    Tile(int x, int y, String backgroundImagePath) {
        this.x = x;
        this.y = y;
        backgroundPath = backgroundImagePath;

        // Init the transients variables
        initTransients();
    }

    void initTransients() {
        // Init background image from path
        background = new ImageView(new Image(backgroundPath));

        // Init pane
        pane = new Pane();
        pane.getChildren().add(background);
        background.fitWidthProperty().bind(pane.widthProperty());
        background.fitHeightProperty().bind(pane.heightProperty());

        // Reset background sprite rotation
        resetRotation(this.rotation);
    }

    void resetRotation(int rotation) {
        this.rotation = rotation;
        background.setRotate(rotation);
    }
}
