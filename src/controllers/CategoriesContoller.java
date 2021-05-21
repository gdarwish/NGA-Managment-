package controllers;

import Database.CSP.Category.Category;
import Database.CSP.Category.CategoryDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class CategoriesContoller implements Initializable {

    @FXML
    private TableView categoriesTable;
    @FXML
    private TableColumn<Category, Category> name;
    @FXML
    private TableColumn<Category, Category> edit;
    @FXML
    private VBox replaceable;
    @FXML
    private HBox topBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        // Adding the an instance of the AddButton class to get an add button
        topBar.getChildren().addAll(region1, new AddButton(replaceable, "Category"));
        name.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        name.setCellFactory(param -> new TableCell<Category, Category>() {
            @Override
            public void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (!empty) {
                    getStyleClass().add("whiteText");
                    setStyle("-fx-background-color: " + category.getColor());
                    setText(category.getName());
                }
            }
        });

        // Adding an instance of the EditButton on each table row and make it open the Edit project Form
        edit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        edit.setCellFactory(param -> new EditButton(replaceable, "Category"));
        CategoryDAO categoryDAO = CategoryDAO.getInstance();
        categoriesTable.setItems(FXCollections.observableArrayList((ArrayList<Category>) categoryDAO.getAll()));
    }
}
