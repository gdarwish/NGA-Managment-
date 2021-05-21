package controllers.forms;

import Const.Const;
import Database.CSP.Status.Status;
import Database.CSP.Status.StatusDAO;
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
 * * @author Ghaith Darwish
 * * Creating the StatusFormController that handles the Status form for add  and update
 */
public class StatusFormController implements Initializable {

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
    public static Status editingStatus;
    String colorNameStr;
    String statusNameStr;

    public void processForm(ActionEvent actionEvent) {
        LinkedList<String> errors = new LinkedList<>();
        colorNameStr = toRGBCode(color.getValue());
        StatusDAO statusDAO = StatusDAO.getInstance();
        List<String> strings = statusDAO.getAll().stream()
                .map(object -> Objects.toString(object.toString().toLowerCase(), null))
                .collect(Collectors.toList());
        if (!updateForm) {
            if (strings.contains(name.getText().toLowerCase())) {
                errors.add(name.getText() + " Already exists. Try to edit it or add another one with different name.");
                name.setStyle("-fx-border-color: red;");
            }
        }
        if (name.getText().isEmpty() || name.getText().length() < 3) {
            errors.add("Status should contain 2 or more letters");
            name.setStyle("-fx-border-color: red;");
        } else {
            name.setStyle("-fx-border-color: none;");
            statusNameStr = name.getText();
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
                editingStatus.setName(name.getText());
                editingStatus.setColor(colorNameStr);
                if (statusDAO.update(editingStatus) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label(statusNameStr + " updated successfully."));
                }
            } else {
                Status status = new Status(statusNameStr, colorNameStr);
                if (statusDAO.create(status) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label(statusNameStr + " added successfully."));
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (updateForm) {
            name.setText(editingStatus.getName());
            java.awt.Color awtColor = HexToColor(editingStatus.getColor());
            int r = awtColor.getRed();
            int g = awtColor.getGreen();
            int b = awtColor.getBlue();
            int a = awtColor.getAlpha();
            double opacity = a / 255.0;
            javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.rgb(r, g, b, opacity);
            color.setValue(fxColor);
            title.setText("Editing Status: " + editingStatus.getName());
            submitButton.setText("Update");
        }
    }
}