package controllers.forms;

import Const.Const;
import Database.CSP.Category.Category;
import Database.CSP.Category.CategoryDAO;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static Const.Const.toRGBCode;

/**
 * @author Ghaith Darwish
 * Creating the CategoriesFormController that handles the Category form for add  and update
 */
public class CategoriesFormController implements Initializable {

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
    public static Category editingCategory;
    // setting the Update form to false to get the Add form when clicked on the add button
    public static boolean updateForm = false;
    String categoryNameStr;
    String colorNameStr;


    public void processCategoryForm(ActionEvent actionEvent) {
        LinkedList<String> errors = new LinkedList<>();
        colorNameStr = toRGBCode(color.getValue());
        CategoryDAO categoryDAO = CategoryDAO.getInstance();
        List<String> strings = categoryDAO.getAll().stream()
                .map(object -> Objects.toString(object.toString().toLowerCase(), null))
                .collect(Collectors.toList());
        if (!updateForm) {
            if (strings.contains(name.getText().toLowerCase())) {
                errors.add(name.getText() + " Already exists. Try to edit it or add another one with different name.");
                name.setStyle("-fx-border-color: red;");
            }
        }
        if (name.getText().isEmpty() || name.getText().length() < 3) {
            errors.add("Category should contain 2 or more letters");
            name.setStyle("-fx-border-color: red;");
        } else {
            name.setStyle("-fx-border-color: none;");
            categoryNameStr = name.getText();
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
                editingCategory.setName(categoryNameStr);
                editingCategory.setColor(colorNameStr);
                if (categoryDAO.update(editingCategory) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label(categoryNameStr + " updated successfully."));
                }
            } else {
                Category category = new Category(categoryNameStr, colorNameStr);
                if (categoryDAO.create(category) == Const.SUCCESS) {
                    errorDisplay.getChildren().add(new Label(categoryNameStr + " added successfully."));
                }
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (updateForm) {

            name.setText(editingCategory.getName());
            java.awt.Color awtColor = HexToColor(editingCategory.getColor());
            int r = awtColor.getRed();
            int g = awtColor.getGreen();
            int b = awtColor.getBlue();
            int a = awtColor.getAlpha();
            double opacity = a / 255.0;
            javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.rgb(r, g, b, opacity);
            color.setValue(fxColor);
            title.setText("Editing Category: " + editingCategory.getName());
            submitButton.setText("Update");
        }
    }

    /**
     * Method to Convert from String to Color
     *
     * @param hex a string value of the HEX color
     * @return Color object
     */
    public static Color HexToColor(String hex) {
        hex = hex.replace("#", "");
        switch (hex.length()) {
            case 6:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16));
            case 8:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16),
                        Integer.valueOf(hex.substring(6, 8), 16));
        }
        return null;
    }
}