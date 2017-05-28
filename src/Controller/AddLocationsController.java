package Controller;
import Adapter.SQLAdapter;
import Model.Location;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Ersan on 5/17/2017.
 */
public class AddLocationsController {
    private SQLAdapter adapter = new SQLAdapter();
    private Location location;

    @FXML private TextField addressTextField, kmTextField;
    @FXML private Button cancelBtn;

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

    public void createLocations(javafx.event.ActionEvent event) {

         try {
             if(addressTextField.getText().trim().isEmpty()){
                 alert("Error", "Address field is empty!");
             }else {
                 if(kmTextField.getText().trim().isEmpty()){
                    alert("Error","Km field is empty");
                 }else {
                     if(!isInt(kmTextField.getText())){
                         alert("Error","Km field wrong type");
                     }else {
                         Location location = new Location(0, addressTextField.getText(),
                                 Integer.parseInt(kmTextField.getText()), false);
                         adapter.addLocations(location);
                         addressTextField.clear();
                         kmTextField.clear();
                         ((Node) (event.getSource())).getScene().getWindow().hide();
                     }
                 }
             }

         } catch (Exception ex) {
             ex.printStackTrace();
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

    public void updateLocation(javafx.event.ActionEvent event) {
        try {
            if (addressTextField.getText().trim().isEmpty()) {
                alert("Error", "Address field is empty!");
            } else {
                if (kmTextField.getText().trim().isEmpty()) {
                    alert("Error", "Km field is empty");
                } else {
                    if (!isInt(kmTextField.getText())){
                        alert("Error", "Km field wrong type");
                    } else {
                        location.setAddress(addressTextField.getText());
                        location.setKm(Integer.parseInt(kmTextField.getText()));
                        adapter.editLocation(location);

                        addressTextField.clear();
                        kmTextField.clear();
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    }
                }
            }

            }catch(Exception ex){
                ex.printStackTrace();
            }
    }

    public void setLocation(Location location){
        this.location = location;
        addressTextField.setText(location.getAddress());
        kmTextField.setText(Integer.toString(location.getKm()));

    }

}
