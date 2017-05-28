package Controller;

import Adapter.SQLAdapter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Andrei
 */
public class LogInController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    SQLAdapter adapter = new SQLAdapter();

    public void logInMethod(ActionEvent ac) throws Exception {
        String password = passwordField.getText();
        String username = usernameField.getText();

        ResultSet result = adapter.logIn(username,password);
        if (!result.isBeforeFirst()) {
            alert("Error", "Wrong username or password!");
        } else {
            result.next();
            String answer = result.getString(1);
            switch (answer) {
                case "admin":
                    adminMenu();
                    ((Node) (ac.getSource())).getScene().getWindow().hide();
                    break;
                case "automecanic":
                    automecanicMenu();
                    ((Node) (ac.getSource())).getScene().getWindow().hide();
                    break;
                case "salesstaff":
                    salesstaffMenu();
                    ((Node) (ac.getSource())).getScene().getWindow().hide();
                    break;
                case "bookkeper":
                    bookkeperMenu();
                    ((Node) (ac.getSource())).getScene().getWindow().hide();
                    break;
                case "cleaningstaff":
                    cleaningstaffMenu();
                    ((Node) (ac.getSource())).getScene().getWindow().hide();
                    break;
            }
        }
    }
    private void adminMenu(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/main.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            MainController contr = fxmlLoader.getController();
            stage.setTitle("Nordic Motorhome - Admin");
            contr.adminMenu();
            stage.hide();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }
    private void automecanicMenu(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/main.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            MainController contr = fxmlLoader.getController();
            stage.setTitle("Nordic Motorhome - Automechanic");
            contr.automecanicMenu();
            stage.hide();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void salesstaffMenu(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/main.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            MainController contr = fxmlLoader.getController();
            stage.setTitle("Nordic Motorhome - Sales Staff");
            contr.salesstaffMenu();
            stage.hide();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void cleaningstaffMenu(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/main.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            MainController contr = fxmlLoader.getController();
            stage.setTitle("Nordic Motorhome - Cleaning staff");
            contr.cleaningstaffMenu();
            stage.hide();
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void bookkeperMenu(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/main.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            MainController contr = fxmlLoader.getController();
            stage.setTitle("Nordic Motorhome - Booke");
            contr.bookkeperMenu();
            stage.hide();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
