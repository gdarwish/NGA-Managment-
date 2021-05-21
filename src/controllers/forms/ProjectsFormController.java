package controllers.forms;

import Const.Const;
import Database.CSP.Category.Category;
import Database.CSP.Category.CategoryDAO;
import Database.CSP.Priority.Priority;
import Database.CSP.Priority.PriorityDAO;
import Database.CSP.Status.Status;
import Database.CSP.Status.StatusDAO;
import Database.Project.Project;
import Database.Project.ProjectDAO;
import Database.Task.Task;
import Database.Task.TaskDAO;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.util.*;


import java.net.URL;

/**
 * * @author Ghaith Darwish
 * * Creating the ProjectsFormController that handles the Project form for add  and update
 */
public class ProjectsFormController implements Initializable {

    @FXML
    VBox TasksHBox;
    @FXML
    Label taskOneLabel;
    HBox vBox;
    @FXML
    JFXTextField task1;
    @FXML
    private JFXTextField projectName;
    @FXML
    private JFXTextArea projectDescription;
    @FXML
    JFXComboBox<Category> category;
    @FXML
    JFXComboBox<Priority> priority;
    @FXML
    JFXComboBox<Status> status;
    @FXML
    private JFXDatePicker startDatePicker;
    @FXML
    private JFXDatePicker dueDatePicker;
    @FXML
    private VBox errorDisplay;
    public static boolean updateForm = false;
    // setting the Update form to false to get the Add form when clicked on the add button
    private String projectNameStr = "";
    private String projectDescriptionStr = "";
    private int categoryStr;
    private int priorityStr;
    private int statusStr;
    private String startDateStr = "";
    private String endDateStr = "";
    public static Project editingProject;
    @FXML
    private Label projectTitle;
    @FXML
    private Button submitButton;
    @FXML
    private ToggleButton closeProject;

    CategoryDAO categoryDAO = CategoryDAO.getInstance();
    PriorityDAO priorityDAO = PriorityDAO.getInstance();
    StatusDAO statusDAO = StatusDAO.getInstance();

    public void addNewTask() {
        newTask(null);
    }

    LinkedList<String> tasksList = new LinkedList<>();

    public void processProjectsForm(ActionEvent actionEvent) {
        LinkedList<String> errors = new LinkedList<>();
        //check each field if the data is entered
        if (projectName.getText().isEmpty() || projectName.getText().length() < 10) {
            errors.add("Project name should contain at least 10 letters");
            projectName.setStyle("-fx-border-color: red;");
        } else {
            projectName.setStyle("-fx-border-color: none;");
            projectNameStr = projectName.getText();
        }
        if (projectDescription.getText().isEmpty() || projectDescription.getText().length() < 10) {
            errors.add("Project description should contain at least 10 letters");
            projectDescription.setStyle("-fx-border-color: red;");
        } else {
            projectDescription.setStyle("-fx-border-color: none;");
            projectDescriptionStr = projectDescription.getText();
        }
        for (Node child : TasksHBox.getChildren()) {
            HBox box = (HBox) child;
            for (Node achild : box.getChildren()) {
                if (achild instanceof TextField) {
                    if (((TextField) achild).getText().isEmpty() || ((TextField) achild).getText().length() < 10) {
                        errors.add("Each Task should contain at least 10 letters");
                        achild.setStyle("-fx-border-color: red;");
                    } else {
                        achild.setStyle("-fx-border-color: none;");
                        tasksList.add(((TextField) achild).getText());
                    }
                }
            }
        }
        if (category.getValue() == null) {
            errors.add("Select a Category");
            HBox catParent = (HBox) category.getParent();
            catParent.getChildren().add(new Label("Select a Category"));
            category.setStyle("-fx-border-color: red;");
        } else {
            category.setStyle("-fx-border-color: none;");
            categoryStr = category.getSelectionModel().getSelectedItem().getId();
        }
        if (priority.getValue() == null) {
            errors.add("Select a Priority");
            priority.setStyle("-fx-border-color: red;");
        } else {
            priority.setStyle("-fx-border-color: none;");
            priorityStr = priority.getSelectionModel().getSelectedItem().getId();
        }

        if (status.getValue() == null) {
            errors.add("Select a Status");
            status.setStyle("-fx-border-color: red;");
        } else {
            status.setStyle("-fx-border-color: none;");
            statusStr = status.getSelectionModel().getSelectedItem().getId();
        }
        if (startDatePicker.getValue() == null) {
            errors.add("Select a Start Date for your project");
            startDatePicker.setStyle("-fx-border-color: red;");
        } else {
            startDatePicker.setStyle("-fx-border-color: none;");
            startDateStr = startDatePicker.getValue().toString();
        }
        if (dueDatePicker.getValue() == null) {
            errors.add("Select a Start Date for your project");
            dueDatePicker.setStyle("-fx-border-color: red;");
        } else {
            dueDatePicker.setStyle("-fx-border-color: none;");
            endDateStr = dueDatePicker.getValue().toString();
        }

        //cleaning the Error container each click to remove old cache
        errorDisplay.getChildren().clear();
        if (errors.size() > 0) {
            for (int i = 0; i < errors.size(); i++) {
                Label errorLabel = new Label(errors.get(i));
                errorDisplay.getChildren().add(errorLabel);
            }
        } else {
            ProjectDAO projectDAO = ProjectDAO.getInstance();
            TaskDAO taskDAO = TaskDAO.getInstance();

            Date startdate = Date.valueOf(startDateStr);
            Date duedate = Date.valueOf(endDateStr);
            if (updateForm) {
                editingProject.setTitle(projectNameStr);
                editingProject.setDescription(projectDescription.getText());
                editingProject.setStatus(statusStr);
                editingProject.setCategory(categoryStr);
                editingProject.setPriority(priorityStr);
                editingProject.setStartDate(startdate);
                editingProject.setDueDate(duedate);
                byte open = 1;
                if (closeProject.isSelected()) {
                    open = 0;
                }
                editingProject.setOpen(open);
                if (projectDAO.update(editingProject) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label("Project Updated Successfully"));
                }
                //if the number of tasks of the project from database is same as the number of task in the form(the user didn\t delete or add a task)
                //update all of the DB task with those in form
                if (tasksList.size() == taskDAO.getTasksByPojectID(editingProject.getId()).size()) {
                    for (int i = 0; i < tasksList.size(); i++) {
                        Task updateTask = new Task(taskDAO.getTasksByPojectID(editingProject.getId()).get(i).getId(), tasksList.get(i), editingProject.getId(), (byte) 1);
                        taskDAO.update(updateTask);
                    }
                } else if (tasksList.size() < taskDAO.getTasksByPojectID(editingProject.getId()).size()) {
                    //if the form size is less than db size (user deleted a task from the form)
                    //firstly update those who exists in both places
                    for (int i = 0; i < tasksList.size(); i++) {
                        Task updateTask = new Task(taskDAO.getTasksByPojectID(editingProject.getId()).get(i).getId(), tasksList.get(i), editingProject.getId(), (byte) 1);
                        taskDAO.update(updateTask);
                    }
                    //then delete the tasks who was deleted from form
                    for (int i = taskDAO.getTasksByPojectID(editingProject.getId()).size(); i > tasksList.size(); i--) {
                        Optional<Task> deleteTask = (Optional<Task>) taskDAO.get(taskDAO.getTasksByPojectID(editingProject.getId()).get(i - 1).getId());
                        if (deleteTask.isPresent())
                            taskDAO.delete(deleteTask.get());
                    }
                } else if (tasksList.size() > taskDAO.getTasksByPojectID(editingProject.getId()).size()) {
                    //if form has more tasks than database (user added task while editing project)
                    //firstly update those who are in db with those who are in form
                    for (int i = 0; i < taskDAO.getTasksByPojectID(editingProject.getId()).size(); i++) {
                        Task updateTask = new Task(taskDAO.getTasksByPojectID(editingProject.getId()).get(i).getId(), tasksList.get(i), editingProject.getId(), (byte) 1);
                        taskDAO.update(updateTask);
                    }
                    //then perform an insert for new tasks
                    for (int i = taskDAO.getTasksByPojectID(editingProject.getId()).size(); i < tasksList.size(); i++) {
                        Task insertTask = new Task(tasksList.get(i), editingProject.getId(), (byte) 1);
                        taskDAO.create(insertTask);
                    }
                }
            } else {
                Project project = new Project(projectNameStr, projectDescriptionStr, statusStr, categoryStr, priorityStr, startdate, duedate, (byte) 1);
                if (projectDAO.create(project) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label("Project Created Successfully"));
                }
                int lastInsertedId = projectDAO.getAll().get(projectDAO.getAll().size() - 1).getId();
                for (int i = 0; i < tasksList.size(); i++) {
                    Task task = new Task(tasksList.get(i), lastInsertedId, (byte) 1);
                    taskDAO.create(task);
                }
            }
        }
        category.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                }
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeProject.setStyle("visibility: hidden;");

        //populate the combobox from database
        category.setItems(FXCollections.observableArrayList((ArrayList<Category>) categoryDAO.getAll()));
        priority.setItems(FXCollections.observableArrayList((ArrayList<Priority>) priorityDAO.getAll()));
        status.setItems(FXCollections.observableArrayList((ArrayList<Status>) statusDAO.getAll()));

        if (updateForm) {
            TaskDAO taskDAO = TaskDAO.getInstance();
            projectName.setText(editingProject.getTitle());
            projectDescription.setText(editingProject.getDescription());
            newTask(taskDAO.getTasksByPojectID(editingProject.getId()));
            category.getSelectionModel().select(editingProject.getCategory() - 1);
            priority.getSelectionModel().select(editingProject.getPriority() - 1);
            status.getSelectionModel().select(editingProject.getStatus() - 1);
            dueDatePicker.setValue(editingProject.getDueDate().toLocalDate());
            startDatePicker.setValue(editingProject.getStartDate().toLocalDate());

            projectTitle.setText("Editing Project: " + editingProject.getTitle());

            category.setItems(FXCollections.observableArrayList((ArrayList<Category>) categoryDAO.getAll()));
            priority.setItems(FXCollections.observableArrayList((ArrayList<Priority>) priorityDAO.getAll()));
            status.setItems(FXCollections.observableArrayList((ArrayList<Status>) statusDAO.getAll()));

            submitButton.setText("Update");
            closeProject.setStyle("visibility: visible;");
        }
    }

    public void closeTheProject(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning!");
        alert.setContentText("Are you sure you want to close the project?");
        if (closeProject.isSelected()) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                projectTitle.setText("Closed!"); // test
                // database query should go here!
            } else {
                closeProject.setSelected(false);
            }
        }
    }

    /**
     * Method to add new task on the fly
     * The task is added when you click the +
     * Each HBox added, has a - which help to delete that task on the fly
     *
     * @param tasks
     */
    public void newTask(ArrayList<Task> tasks) {
        int loopSize = 0;
        if (tasks == null) {
            loopSize = 2;
        } else {
            if (tasks.size() != 0) {
                task1.setText(tasks.get(0).getName());
                loopSize = tasks.size();
            }
        }
        for (int i = 1; i < loopSize; i++) {
            vBox = new HBox();
            JFXTextField newTask = new JFXTextField();
            if (tasks != null) {
                newTask.setText(tasks.get(i).getName());
            }
            JFXButton remove = new JFXButton();
            remove.setText("-");
            remove.getStyleClass().add("removeTaskBtn");
            /**
             * Remove the task row,when - clicked
             */
            remove.setOnAction(event -> {
                HBox thisParent = (HBox) remove.getParent();
                TasksHBox.getChildren().remove(thisParent);
                HBox parent = (HBox) remove.getParent();
                TasksHBox.getChildren().remove(parent);
            });
            taskOneLabel.setText("Task #1");
            Label label = new Label("Task #" + (TasksHBox.getChildren().size() + 1));
            label.setMinHeight(40);
            label.setMinWidth(150);
            newTask.setMinWidth(300);
            newTask.setMinHeight(40);

            vBox.getChildren().addAll(label, newTask, remove);
            if (TasksHBox.getChildren().size() < 5) {
                TasksHBox.getChildren().add(vBox);
            }
        }

    }
}