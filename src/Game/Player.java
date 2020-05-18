package Game;

import javafx.scene.image.Image;

import java.io.Serializable;

class Player implements Serializable {

    String pseudo;
    String color;
    int score = 0;

    String shapePath;
    transient Image shape;


    Player(String pseudo, String shapePath, String color) {
        this.pseudo = pseudo;
        this.shapePath = shapePath;
        this.color = color;

        // Init the transients variables
        initTransients();
    }

    void initTransients() {
        shape = new Image(shapePath);
    }
}
