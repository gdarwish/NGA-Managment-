package controllers;


import Database.DatabaseConnection;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static de.jensd.fx.glyphs.GlyphsDude.createIcon;

public class MainController implements Initializable {
    @FXML
    VBox tableContainer;

    @FXML
    JFXButton projectsButton;

    @FXML
    Pane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Default Screen
        changeTable("Project");
    }


    /**
     * Call changeTable() and pass view name
     * @param event
     * @author Ali Dali
     */
    public void switchTable(ActionEvent event) {
        Button button = (Button) event.getSource();

        switch (button.getId()) {
            case "projectsButton":
                changeTable("Project");
                break;
            case "tasksButton":
                changeTable("Task");
                break;
            case "categoriesButton":
                changeTable("Category");
                break;
            case "statusButton":
                changeTable("Status");
                break;
            case "prioritiesButton":
                changeTable("Priority");
                break;
            case "statisticButton":
                changeTable("Statistic");
                break;
            case "settingsButton":
                changeTable("Settings");
                break;
            case "creditButton":
                changeTable("Credit");
                break;
        }
    }

    /**
     * Navigate between different screens
     * @param view name
     * @author Ali Dali
     */
    private void changeTable(String view) {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("../views/" + view + "View.fxml"));
            tableContainer.getChildren().retainAll();
            tableContainer.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logout and open Database Login Form
     * @param actionEvent
     * @author Ali Dali
     */
    public void logout(ActionEvent actionEvent) {
        try {
            DatabaseConnection.getInstance().closeConnection();
            Pane pane = FXMLLoader.load(getClass().getResource("../views/DBLoginView.fxml"));
            Scene scene = new Scene(pane);
            scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Ubuntu");
            scene.setFill(Color.TRANSPARENT);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
