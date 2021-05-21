package controllers.forms;

import Const.Const;
import Database.Project.Project;
import Database.Project.ProjectDAO;
import Database.Task.Task;
import Database.Task.TaskDAO;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * * @author Ghaith Darwish
 * * Creating the TaskFormController that handles the Task form for add  and update
 */
public class TaskFormController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Button submitButton;
    @FXML
    private JFXToggleButton closeTask;
    @FXML
    JFXTextField name;
    @FXML
    JFXComboBox<Project> project;
    @FXML
    VBox errorDisplay;
    @FXML
    HBox closeHBox;
    public static Task editingTask;
    public static boolean updateForm = false;
    // setting the Update form to false to get the Add form when clicked on the add button

    TaskDAO taskDAO = TaskDAO.getInstance();
    ProjectDAO projectDAO = ProjectDAO.getInstance();

    public void processForm(ActionEvent actionEvent) {
        LinkedList<String> errors = new LinkedList<>();
        //if selected project has less than 6 tasks
        if (!updateForm) {
            if (taskDAO.getTasksByPojectID(project.getSelectionModel().getSelectedItem().getId()).size() < 5) {
                if (name.getText().isEmpty() || name.getText().length() < 10) {
                    errors.add("Task name should contain at least 10 letters");
                    name.setStyle("-fx-border-color: red;");
                } else {
                    name.setStyle("-fx-border-color: none;");
                }
                if (project.getValue() == null) {
                    errors.add("Select a Project");
                    project.setStyle("-fx-border-color: red;");
                } else {
                    project.setStyle("-fx-border-color: none;");
                }
            } else {
                errors.add("Selected project has already the minimum number of tasks");
                project.setStyle("-fx-border-color: red;");
            }
        }

        errorDisplay.getChildren().clear();
        if (errors.size() > 0) {
            for (int i = 0; i < errors.size(); i++) {
                Label errorLabel = new Label(errors.get(i));
                errorDisplay.getChildren().add(errorLabel);
            }
        } else {
            if (updateForm) {
                editingTask.setName(name.getText());
                byte taskOpen = 1;
                if (closeTask.isSelected()) {
                    taskOpen = 0;
                }
                editingTask.setOpen(taskOpen);
                if (taskDAO.update(editingTask) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label("Task Updated"));
                }
            } else {
                Task task = new Task(name.getText(), project.getSelectionModel().getSelectedItem().getId(), (byte) 1);
                if (taskDAO.create(task) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label("Task Created"));
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeHBox.setStyle("visibility: hidden;");
        project.setItems(FXCollections.observableArrayList((ArrayList<Project>) projectDAO.getAll()));

        if (updateForm) {
            title.setText("Editing Task: " + editingTask.getName());
            name.setText(editingTask.getName());
            project.getSelectionModel().select(editingTask.getProject() - 1);
            project.setDisable(true);
            submitButton.setText("Update Task");
            closeHBox.setStyle("visibility: visible;");
        }
    }

    public void on(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning!");
        alert.setContentText("Are you sure you want to close this task?");
        if (closeTask.isSelected()) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                title.setText("Task: " + name.getText() + " is Closed!");
            } else {
                closeTask.setSelected(false);
            }
        } else {
            title.setText("Task: " + name.getText());
        }
    }
}