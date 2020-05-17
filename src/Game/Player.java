package Game;

import javafx.scene.image.Image;

class Player {

    String pseudo;
    Image shape;
    String color;
    int score = 0;


    Player(String pseudo, Image shape, String color) {
        this.pseudo = pseudo;
        this.shape = shape;
        this.color = color;
    }
}
