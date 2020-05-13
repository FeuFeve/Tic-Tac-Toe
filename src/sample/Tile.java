package sample;

import javafx.scene.layout.Pane;

public class Tile {

    Pane pane;

    boolean isClicked;

    String baseColor = "#2E0734";
    String clickedColor = "#F9CD45";


    Tile() {
        pane = new Pane();
    }
}
