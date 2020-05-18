package Game;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

class GameAnimator {


    static void animateTitle(Label title) {
        ScaleTransition st = new ScaleTransition(Duration.millis(2000), title);
        st.setByX(-0.2);
        st.setByY(-0.2);
        st.setAutoReverse(true);
        st.setCycleCount(Timeline.INDEFINITE);
        st.play();
    }

    static void animateFadingImage(ImageView image, double from, double to, int durationMillis) {
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), image);
        ft.setFromValue(from);
        ft.setToValue(to);
        ft.setAutoReverse(true);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.play();
    }

    static void animateClickedTile(Tile tile) {
        ImageView playerShape = new ImageView(tile.owner.shape);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), playerShape);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.play();

        tile.pane.getChildren().add(playerShape);
        playerShape.fitWidthProperty().bind(tile.pane.widthProperty());
        playerShape.fitHeightProperty().bind(tile.pane.heightProperty());
    }

    static void animateWinningLine(Pane surface, double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStrokeWidth(10);
        line.setStyle("-fx-stroke:yellow");

        ScaleTransition st = new ScaleTransition(Duration.millis(500), line);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1.1);
        st.setToY(1.1);
        st.play();

        surface.setDisable(false);
        surface.getChildren().add(line);
    }

    static void changeLabel(Label label, String text, String colorCode) {
        label.setText(text);
        label.setStyle("-fx-background-color: " + colorCode);
    }
}
