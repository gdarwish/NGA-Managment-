package controllers.forms;


import Const.Const;
import Database.CSP.Priority.Priority;
import Database.CSP.Priority.PriorityDAO;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static Const.Const.toRGBCode;
import static controllers.forms.CategoriesFormController.HexToColor;

/**
 * @author Ghaith Darwish
 * Creating the PrioritiesFormController that handles the Priority form for add  and update
 */
public class PrioritiesFormController implements Initializable {

    @FXML
    Button submitButton;
    @FXML
    Label title;
    @FXML
    JFXTextField name;
    @FXML
    JFXColorPicker color;
    @FXML
    VBox errorDisplay;
    public static boolean updateForm = false;
    // setting the Update form to false to get the Add form when clicked on the add button
    public static Priority editingPriority;
    String colorNameStr;
    String priorityNameStr;

    public void processForm(ActionEvent actionEvent) {
        LinkedList<String> errors = new LinkedList<>();
        colorNameStr = toRGBCode(color.getValue());
        PriorityDAO priorityDAO = PriorityDAO.getInstance();
        List<String> strings = priorityDAO.getAll().stream()
                .map(object -> Objects.toString(object.toString().toLowerCase(), null))
                .collect(Collectors.toList());
        if (!updateForm) {
            if (strings.contains(name.getText().toLowerCase())) {
                errors.add(name.getText() + " Already exists. Try to edit it or add another one with different name.");
                name.setStyle("-fx-border-color: red;");
            }
        }
        if (name.getText().isEmpty() || name.getText().length() < 3) {
            errors.add("Priority should contain 2 or more letters");
            name.setStyle("-fx-border-color: red;");
        } else {
            name.setStyle("-fx-border-color: none;");
            priorityNameStr = name.getText();
        }

        if (colorNameStr.isEmpty() || colorNameStr.equals("#FFFFFF")) {
            errors.add("white color is not that nice :)");
            color.setStyle("-fx-border-color: red;");
        } else {
            color.setStyle("-fx-border-color: none;");
            colorNameStr = toRGBCode(color.getValue());
        }
        errorDisplay.getChildren().clear();
        if (errors.size() > 0) {
            for (int i = 0; i < errors.size(); i++) {
                Label errorLabel = new Label(errors.get(i));
                errorDisplay.getChildren().add(errorLabel);
            }
        } else {
            if (updateForm) {
                editingPriority.setName(name.getText());
                editingPriority.setColor(colorNameStr);
                if (priorityDAO.update(editingPriority) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label(priorityNameStr + " updated successfully."));
                }
            } else {
                Priority priority = new Priority(priorityNameStr, colorNameStr);
                if (priorityDAO.create(priority) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label(priorityNameStr + " added successfully."));
                }
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (updateForm) {
            title.setText("Editing Priority: " + editingPriority.getName());
            submitButton.setText("Update");
            name.setText(editingPriority.getName());
            java.awt.Color awtColor = HexToColor(editingPriority.getColor());
            int r = awtColor.getRed();
            int g = awtColor.getGreen();
            int b = awtColor.getBlue();
            int a = awtColor.getAlpha();
            double opacity = a / 255.0;
            javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.rgb(r, g, b, opacity);
            color.setValue(fxColor);
        }
    }
}