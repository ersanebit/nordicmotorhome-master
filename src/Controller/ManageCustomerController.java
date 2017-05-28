package Controller;

import Adapter.SQLAdapter;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rolis on 5/23/2017.
 */

public class ManageCustomerController {

    private SQLAdapter adapter = new SQLAdapter();
    private Customer customer;
    @FXML private Button cancelBtn, btnCreateCustomer, btnUpdateCustomer;
    @FXML private TextField nameIn, cprIn, addressIn, emailIn, phoneIn;

          @FXML
    private void cancelAction() throws IOException {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }


    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }

    public void createCustomer(javafx.event.ActionEvent event) {

        try {

            if (nameIn.getText().isEmpty()) {
                alert("Error", "Name field is empty!");
            }

            if (cprIn.getText().isEmpty()) {
                alert("Error", "Empty Cpr field!");
            } else if (!isValidCPR(cprIn.getText())) {
                alert("Error", "Wrong CPR format !\nTry this format 123456-7890");
            }

            else if (addressIn.getText().isEmpty()) {
                alert("Error", "Address field is empty!");
            }

            else if (emailIn.getText().isEmpty()) {
                alert("Error", "Email field is empty!");
            }

            else if (!isValidEmail(emailIn.getText())) {
                alert("Error", "Email field wrong type");
            }

            else if (phoneIn.getText().isEmpty()) {
                alert("Error", "Phone field is empty");
            }
            else if (!isValidPhoneNumber(phoneIn.getText())) {
                alert("Error", "Phone field wrong type");
            }

            else {
                Customer customer = new Customer(0, nameIn.getText(), cprIn.getText(), addressIn.getText(), emailIn.getText(), phoneIn.getText());
                adapter.addCustomer(customer);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isInt(String s){
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    @FXML
    public void updateCustomer(javafx.event.ActionEvent event) throws SQLException {
        if (nameIn.getText().isEmpty()) {
            alert("Error", "Name field is empty!");
        }

        if (cprIn.getText().isEmpty()) {
            alert("Error", "Empty Cpr field!");
        } else if (!isValidCPR(cprIn.getText())) {
                alert("Error", "Wrong CPR format !\nTry this format 123456-7890");
          }

        else if (addressIn.getText().isEmpty()) {
                alert("Error", "Address field is empty!");
        }

        else if (emailIn.getText().isEmpty()) {
                alert("Error", "Email field is empty!");
        }

        else if (!isValidEmail(emailIn.getText())) {
                alert("Error", "Email field wrong type");
        }

        else if (phoneIn.getText().isEmpty()) {
                alert("Error", "Phone field is empty");
        }
        else if (!isValidPhoneNumber(phoneIn.getText())) {
                alert("Error", "Phone field wrong type");
        }

        else {
                customer.setName(nameIn.getText());
                customer.setCpr(cprIn.getText());
                customer.setAddress(addressIn.getText());
                customer.setEmail(emailIn.getText());
                customer.setPhoneNumber(phoneIn.getText());
                adapter.updateCustomer(customer);
                ((Node) (event.getSource())).getScene().getWindow().hide();
        }

    }
    public void setCustomer(Customer customer){
        this.customer = customer;
        nameIn.setText(customer.getName());
        cprIn.setText(customer.getCpr());
        emailIn.setText(customer.getEmail());
        addressIn.setText(customer.getAddress());
        phoneIn.setText(customer.getPhoneNumber());
    }

    public boolean isValidCPR(String cpr){
        Pattern pattern = Pattern.compile("\\d{6}-\\d{4}");
        Matcher matcher = pattern.matcher(cpr);
        return matcher.matches();
    }

    public boolean isValidPhoneNumber(String phoneNumber){
        return phoneNumber.matches("\\d+");
    }

    public boolean isValidEmail(String email){
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}