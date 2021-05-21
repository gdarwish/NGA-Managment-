package controllers;

import Const.Const;
import Start.Start;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.DBLoginModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Database Login
 * @author Ali Dali
 */
public class DBLoginController implements Initializable {

    DBLoginModel model = DBLoginModel.getInstance();

    @FXML
    JFXTextField dbHost;
    @FXML
    JFXTextField dbName;
    @FXML
    JFXTextField dbUsername;
    @FXML
    JFXPasswordField dbPassword;
    @FXML
    JFXCheckBox checkBox;

    @FXML
    VBox root;

    private Preferences preferences;
    boolean rememberMe = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        preferences = Preferences.userRoot();

        String host = preferences.get(Const.DB_HOST, "");
        String database = preferences.get(Const.DB_NAME, "");
        String username = preferences.get(Const.DB_USER, "");
        String password = preferences.get(Const.DB_PASS, "");
        rememberMe = preferences.getBoolean("remember", false);

        dbHost.setText(host);
        dbName.setText(database);
        dbUsername.setText(username);
        dbPassword.setText(password);
        checkBox.setSelected(rememberMe);
    }

    public void connect(ActionEvent actionEvent) {
        String host = dbHost.getText();
        String database = dbName.getText();
        String username = dbUsername.getText();
        String password = dbPassword.getText();
        if (host.isEmpty() || database.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You must fill all fields!");

            String message = "";
            message += host.isEmpty() ? "Host is missing\n" : "";
            message += database.isEmpty() ? "Database is missing\n" : "";
            message += username.isEmpty() ? "Username is missing\n" : "";
            message += password.isEmpty() ? "Password is missing" : "";

            alert.setContentText(message);
            alert.show();
            return;
        }

        boolean connected = model.establishConnection(host, database, username, password);
        if (connected) {
            if (rememberMe)
                saveCredentials();
            else
                clearCredentials();
            try {
                Pane pane = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
                Scene scene = new Scene(pane);
                scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Ubuntu");
                scene.setFill(Color.TRANSPARENT);
                Stage stage = Start.stage;
                stage.setScene(scene);
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to connect to Database");
            alert.setContentText(model.errorMessage());
            alert.show();
        }    }

    public void rememberMe(ActionEvent actionEvent) {
        rememberMe = !rememberMe;
    }

    private void saveCredentials() {
        preferences.put(Const.DB_HOST, dbHost.getText());
        preferences.put(Const.DB_NAME, dbName.getText());
        preferences.put(Const.DB_USER, dbUsername.getText());
        preferences.put(Const.DB_PASS, dbPassword.getText());
        preferences.putBoolean("remember", true);
    }

    private void clearCredentials() {
        try {
            preferences.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
}
