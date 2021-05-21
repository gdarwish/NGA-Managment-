package controllers;

import Database.CSP.Status.Status;
import Database.CSP.Status.StatusDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * @author Ghaith Darwish
 * Creating the CategoriesContoller that handles all Categories Tables
 */
public class StatusContrller implements Initializable {

    @FXML
    private TableView statusTable;
    @FXML
    private TableColumn<Status, Status> name;
    @FXML
    private TableColumn<Status, Status> edit;
    @FXML
    private VBox replaceable;

    @FXML
    private HBox topBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        // Adding the an instance of the AddButton class to get an add button
        topBar.getChildren().addAll(region1, new AddButton(replaceable, "Status"));
        name.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        name.setCellFactory(param -> new TableCell<Status, Status>() {
            @Override
            public void updateItem(Status status, boolean empty) {
                super.updateItem(status, empty);
                if (!empty) {
                    getStyleClass().add("whiteText");
                    setStyle("-fx-background-color: " + status.getColor());
                    setText(status.getName());
                }
            }
        });

        // Adding an instance of the EditButton on each table row and make it open the Edit project Form
        edit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        edit.setCellFactory(param -> new EditButton(replaceable, "Status"));
        StatusDAO statusDAO = StatusDAO.getInstance();
        statusTable.setItems(FXCollections.observableArrayList((ArrayList<Status>) statusDAO.getAll()));
    }
}
