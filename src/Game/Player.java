package Game;

import javafx.scene.image.Image;

class Player {

    String pseudo;
    Image shape;
    int score = 0;


    Player(String pseudo, Image shape) {
        this.pseudo = pseudo;
        this.shape = shape;
    }
}
