package controllers;


import Database.CSP.Category.CategoryDAO;
import Database.CSP.Priority.PriorityDAO;
import Database.CSP.Status.StatusDAO;
import Database.Project.ProjectDAO;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import Database.Project.Project;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ghaith Darwish
 * Creating the CategoriesContoller that handles all Categories Tables
 */
public class ProjectController implements Initializable {

    @FXML
    TableView table;
    @FXML
    TableColumn<Project, String> projectName;
    @FXML
    TableColumn<Project, Project> category;
    @FXML
    TableColumn<Project, String> description;
    @FXML
    TableColumn<Project, Project> status;
    @FXML
    TableColumn<Project, Project> priority;
    @FXML
    TableColumn<Project, String> startDate;
    @FXML
    TableColumn<Project, String> dueDate;
    @FXML
    TableColumn<Project, Project> edit;
    @FXML
    private VBox replaceable;
    @FXML
    private HBox topBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ProjectDAO projectDAO = ProjectDAO.getInstance();
        CategoryDAO categoryDAO = CategoryDAO.getInstance();
        StatusDAO statusDAO = StatusDAO.getInstance();
        PriorityDAO priorityDAO = PriorityDAO.getInstance();
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        // Adding the an instance of the AddButton class to get an add button
        topBar.getChildren().addAll(region1, new AddButton(replaceable, "Project"));
        projectName.setCellValueFactory(new PropertyValueFactory<>("title"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Adding an instance of the EditButton on each table row and make it open the Edit project Form
        edit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        edit.setCellFactory(param -> new EditButton(replaceable, "Project"));

        category.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        category.setCellFactory(param -> new TableCell<Project, Project>() {
            @Override
            public void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);
                if (!empty) {
                    getStyleClass().add("whiteText");
                    setStyle("-fx-background-color: " + categoryDAO.getItemById(project.getCategory()).getColor());
                    setText(categoryDAO.getItemById(project.getCategory()).getName());
                }
            }
        });

        status.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        status.setCellFactory(param -> new TableCell<Project, Project>() {
            @Override
            public void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);
                if (!empty) {
                    getStyleClass().add("whiteText");
                    setStyle("-fx-background-color: " + statusDAO.getItemById(project.getStatus()).getColor());
                    setText(statusDAO.getItemById(project.getStatus()).getName());
                }
            }
        });

        priority.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        priority.setCellFactory(param -> new TableCell<Project, Project>() {
            @Override
            public void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);
                if (!empty) {
                    getStyleClass().add("whiteText");
                    setStyle("-fx-background-color: " + priorityDAO.getItemById(project.getPriority()).getColor());
                    setText(priorityDAO.getItemById(project.getPriority()).getName());
                }
            }
        });

        ObservableList<Project> projectModel1 = FXCollections.observableArrayList(projectDAO.getAll());
        table.setItems(projectModel1);
    }
}

