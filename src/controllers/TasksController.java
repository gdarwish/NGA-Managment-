package controllers;

import Database.Project.ProjectDAO;
import Database.Task.Task;
import Database.Task.TaskDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Ghaith Darwish
 * Creating the CategoriesContoller that handles all Categories Tables
 */
public class TasksController implements Initializable {

    @FXML
    private TableView taskTable;
    @FXML
    private TableColumn<Task, String> name;
    @FXML
    private TableColumn<Task, String> project;
    @FXML
    private TableColumn<Task, Integer> open;
    @FXML
    TableColumn<Task, Task> edit;
    @FXML
    private HBox topBar;
    @FXML
    private VBox replaceable;
    ProjectDAO projectDAO = ProjectDAO.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        // Adding the an instance of the AddButton class to get an add button
        topBar.getChildren().addAll(region1, new AddButton(replaceable, "Task"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        project.setCellValueFactory(e -> new SimpleStringProperty(projectDAO.get(e.getValue().getProject()).get().getTitle()));
        open.setCellValueFactory(new PropertyValueFactory<>("open"));

        // Adding an instance of the EditButton on each table row and make it open the Edit project Form
        edit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        edit.setCellFactory(param -> new EditButton(replaceable, "Task"));
        TaskDAO taskDAO = TaskDAO.getInstance();
        taskTable.setItems(FXCollections.observableArrayList((ArrayList<Task>) taskDAO.getAll()));
    }
}