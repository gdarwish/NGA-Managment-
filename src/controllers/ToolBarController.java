package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * App Tool Bar
 * @author Ali Dali
 */
public class ToolBarController {

    Stage stage;
    double deltaX;
    double deltaY;

    @FXML
    HBox root;

    public void close(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void dragged(MouseEvent mouseEvent) {
        stage.setX(mouseEvent.getScreenX() + deltaX);
        stage.setY(mouseEvent.getScreenY() + deltaY);
    }

    public void pressed(MouseEvent mouseEvent) {
        if(stage == null)
            stage = (Stage) root.getScene().getWindow();

        root.getStyleClass().add("drag");
        deltaX = stage.getX() - mouseEvent.getScreenX();
        deltaY = stage.getY() - mouseEvent.getScreenY();
    }

    public void released(MouseEvent mouseEvent) {
        root.getStyleClass().remove("drag");
    }

    public void minimize(MouseEvent mouseEvent) {
        if(stage == null)
            stage = (Stage) root.getScene().getWindow();
        stage.setIconified(true);
    }
}
