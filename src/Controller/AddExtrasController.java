package Controller;

import Adapter.SQLAdapter;
import Model.Extra;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Ersan on 5/17/2017.
 */
public class AddExtrasController {

    /*here are the Extra and SQL adapter objects declared
    * also along with the cancel button and some textfields for the fxml files*/
    private SQLAdapter adapter = new SQLAdapter();
    private Extra extra;
    @FXML private Button cancelBtn;
    @FXML private TextField extraName, extraQuantity, extraPrice;


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

    //method for creating extras
    public void createExtras(javafx.event.ActionEvent event) {

        try {
            if(extraName.getText().isEmpty()){
                alert("Error", "Name field is empty!");
            }else {
                if (extraQuantity.getText().isEmpty()) {
                    alert("Error", "Quantity field is empty!");
                } else {
                    if (!isInt(extraQuantity.getText())) {
                        alert("Error", "Quantity field wrong type!");
                    } else {
                        if (extraPrice.getText().isEmpty()) {
                            alert("Error","Price field is empty!");
                        }else {
                            if(!isDouble(extraPrice.getText())){
                                alert("Error","Price field wrong type");
                            }else {
                                Extra extra = new Extra(0, extraName.getText(), Integer.parseInt(extraQuantity.getText()), Double.parseDouble(extraPrice.getText()));
                                adapter.addExtras(extra);
                                ((Node) (event.getSource())).getScene().getWindow().hide();
                            }
                        }

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
    public boolean isDouble(String s)
    {
        try
        {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
    @FXML
    public void updateExtras(javafx.event.ActionEvent event) {

        try {
            if(extraName.getText().trim().isEmpty()){
                alert("Error", "Name field is empty!");
            }else {
                if (extraQuantity.getText().trim().isEmpty()) {
                    alert("Error", "Quantity field is empty!");
                } else {
                    if (!isInt(extraQuantity.getText())) {
                        alert("Error", "Quantity field wrong type!");
                    } else {
                        if (extraPrice.getText().trim().isEmpty()) {
                            alert("Error", "Price field is empty!");
                        } else {
                            if (!isDouble(extraPrice.getText())) {
                                alert("Error", "Price field wrong type");
                            } else {
                                extra.setName(extraName.getText());
                                extra.setQuantity(Integer.parseInt(extraQuantity.getText()));
                                extra.setPrice(Double.parseDouble(extraPrice.getText()));
                                adapter.editExtra(extra);
                                ((Node) (event.getSource())).getScene().getWindow().hide();
                            }
                        }
                    }
                }
            }


        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void setExtra(Extra extra){
        this.extra = extra;
        extraName.setText(extra.toString());
        extraPrice.setText(String.valueOf(extra.getPrice()));
        extraQuantity.setText(String.valueOf(extra.getQuantity()));


    }



}
