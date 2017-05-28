package Controller;

import Adapter.SQLAdapter;
import Model.Motorhome;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Created by Rolis on 5/16/2017.
 */
public class AddMotorhomeController {
    private SQLAdapter adapter = new SQLAdapter();
    private Motorhome motorhome;

    @FXML
    private TextField brandIn, modelIn, capacityIn, priceIn, kmDrivenIn;
    @FXML
    private Button btnCreateMotorhome, btnCancelCreateMotorhome, btnClose, btnUpdateMotorhome, btnCancelUpdateMotorhome;
    @FXML
    private Label lbUpdateTitle, lbCreateTitle, lbBrandInfo, lbModelInfo, lbCapacityInfo, lbPriceInfo, lbKmDrivenInfo,
    lbBrand, lbModel, lbCapacity, lbPrice, lbKmDriven, lbBrandSC, lbModelSC, lbCapacitySC, lbPriceSC, lbKmDrivenSC;


    @FXML public void initialize() throws Exception {

        setInfoLabelsEmpty();
        forceNumbers(capacityIn);
        //forceNumbers(priceIn);
        forceNumbers(kmDrivenIn);

        lbBrandSC.setVisible(false);
        lbBrandSC.setText("");
        lbModelSC.setVisible(false);
        lbModelSC.setText("");
        lbCapacitySC.setVisible(false);
        lbCapacitySC.setText("");
        lbPriceSC.setVisible(false);
        lbPriceSC.setText("");
        lbKmDrivenSC.setVisible(false);
        lbKmDrivenSC.setText("");
        btnClose.setVisible(false);
        btnClose.setVisible(false);
       // startIt();
    }
    public void setInfoLabelsEmpty(){
        lbBrandInfo.setText("");
        lbModelInfo.setText("");
        lbCapacityInfo.setText("");
        lbPriceInfo.setText("");
        lbKmDrivenInfo.setText("");
    }
    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
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
    public void createMotorhome() {

        setInfoLabelsEmpty();

        if (brandIn.getText().trim().isEmpty()) {
            alert("Error", "Empty Brand field!");
        } else if (modelIn.getText().trim().isEmpty()) {
            alert("Error", "Empty Model field!");
        } else if (capacityIn.getText().trim().isEmpty()) {
            alert("Error", "Empty Capacity field!");
        }  else if (priceIn.getText().trim().isEmpty()) {
            alert("Error", "Empty Price field!");
        } else if (!isDouble(priceIn.getText().trim())) {
            alert("Error", "Wrong Price inserted type!");
        } else if (kmDrivenIn.getText().trim().isEmpty()) {
            alert("Error", "Empty KmDriven field!");
        }  else {

            String brand = brandIn.getText();
            String model = modelIn.getText();
            int capacity = Integer.parseInt(capacityIn.getText());
            double price = 0;
            int kmDriven = Integer.parseInt(kmDrivenIn.getText());

            try {
                price = Double.parseDouble(priceIn.getText());
                price = Math.round(price * 100);
                price = price / 100;
            } catch (NumberFormatException e) {
                lbPriceInfo.setText("It should be a number");
                System.out.println("price wrong number");
                price = 0;
            }
            Motorhome motorhome = new Motorhome(brand, model, capacity, price, kmDriven);
            try {
                adapter.addMotorhome(motorhome);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            btnCreateMotorhome.setVisible(false);
            btnCancelCreateMotorhome.setVisible(false);
            lbBrandInfo.setVisible(false);
            lbModelInfo.setVisible(false);
            lbCapacityInfo.setVisible(false);
            lbPriceInfo.setVisible(false);
            lbKmDrivenInfo.setVisible(false);
            brandIn.setVisible(false);
            modelIn.setVisible(false);
            capacityIn.setVisible(false);
            priceIn.setVisible(false);
            kmDrivenIn.setVisible(false);
            lbBrand.setVisible(false);
            lbModel.setVisible(false);
            lbCapacity.setVisible(false);
            lbPrice.setVisible(false);
            lbKmDriven.setVisible(false);


            lbBrandSC.setVisible(true);
            lbBrandSC.setText("Brand: " + motorhome.getBrand());
            lbModelSC.setVisible(true);
            lbModelSC.setText("Model: " + motorhome.getModel());
            lbCapacitySC.setVisible(true);
            lbCapacitySC.setText("Capacity: " + Integer.toString(motorhome.getCapacity()));
            lbPriceSC.setVisible(true);
            lbPriceSC.setText("Price: " + Double.toString(motorhome.getPrice()));
            lbKmDrivenSC.setVisible(true);
            lbKmDrivenSC.setText("KM Driven: " + Integer.toString(motorhome.getKmDriven()));
            btnClose.setVisible(true);

            lbCreateTitle.setText("MOTORHOME CREATED SUCCESSFULLY");
        }
    }



    public void cancelCreateMotorhome(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) btnCancelCreateMotorhome.getScene().getWindow();
        // closing
        stage.close();
    }
    public void cancelUpdateMotorhome(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) btnCancelUpdateMotorhome.getScene().getWindow();
        // closing
        stage.close();
        //((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void btnCloseAction(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) btnCancelCreateMotorhome.getScene().getWindow();
        // closing
        stage.close();
    }

    public void btnCloseUpdateAction(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) btnClose.getScene().getWindow();
        // closing
        stage.close();

    }

    private void forceNumbers(TextField txtField) {
        txtField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*"))
                {
                    txtField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    //UPDATE

    public void setMotorhome(Motorhome motorhome){
        this.motorhome = motorhome;
        brandIn.setText(motorhome.getBrand());
        modelIn.setText(motorhome.getModel());
        capacityIn.setText(String.valueOf(motorhome.getCapacity()));
        priceIn.setText(String.valueOf(motorhome.getBasePrice()));
        kmDrivenIn.setText(String.valueOf(motorhome.getKmDriven()));
    }

    public void updateMotorhome(ActionEvent event) throws SQLException {
        setInfoLabelsEmpty();
        if (brandIn.getText().trim().isEmpty()) {
            alert("Error", "Empty Brand field!");
        } else if (modelIn.getText().trim().isEmpty()) {
            alert("Error", "Empty Model field!");
        } else if (capacityIn.getText().trim().isEmpty()) {
            alert("Error", "Empty Capacity field!");
        } else if (!isInt(capacityIn.getText().trim())) {
            alert("Error", "Wrong Capacity inserted type!");
        } else if (priceIn.getText().trim().isEmpty()) {
            alert("Error", "Empty Price field!");
        } else if (!isDouble(priceIn.getText().trim())) {
            alert("Error", "Wrong Price inserted type!");
        } else if (kmDrivenIn.getText().trim().isEmpty()) {
            alert("Error", "Empty KmDriven field!");
        } else if (!isInt(kmDrivenIn.getText().trim())) {
            alert("Error", "Wrong KmDriven inserted type!");
        } else {
            String brand = brandIn.getText();
            String model = modelIn.getText();
            int id = motorhome.getId();
            int capacity = Integer.parseInt(capacityIn.getText());
            double price = 0;
            int kmDriven = Integer.parseInt(kmDrivenIn.getText());

            try {
                price = Double.parseDouble(priceIn.getText());
                price = Math.round(price * 100);
                price = price / 100;
            } catch (NumberFormatException e) {
                lbPriceInfo.setText("It should be a number");
                System.out.println("price wrong number");
                price = 0;
            }

            Motorhome motorhomeUpdated = new Motorhome(id, brand, model, capacity, price, kmDriven);
            adapter.updateMotorhome(motorhomeUpdated);


            btnUpdateMotorhome.setVisible(false);
            btnCancelUpdateMotorhome.setVisible(false);
            lbBrandInfo.setVisible(false);
            lbModelInfo.setVisible(false);
            lbCapacityInfo.setVisible(false);
            lbPriceInfo.setVisible(false);
            lbKmDrivenInfo.setVisible(false);
            brandIn.setVisible(false);
            modelIn.setVisible(false);
            capacityIn.setVisible(false);
            priceIn.setVisible(false);
            kmDrivenIn.setVisible(false);
            lbBrand.setVisible(false);
            lbModel.setVisible(false);
            lbCapacity.setVisible(false);
            lbPrice.setVisible(false);
            lbKmDriven.setVisible(false);


            lbBrandSC.setVisible(true);
            lbBrandSC.setText("Brand: " + motorhomeUpdated.getBrand());
            lbModelSC.setVisible(true);
            lbModelSC.setText("Model: " + motorhomeUpdated.getModel());
            lbCapacitySC.setVisible(true);
            lbCapacitySC.setText("Capacity: " + Integer.toString(motorhomeUpdated.getCapacity()));
            lbPriceSC.setVisible(true);
            lbPriceSC.setText("Price: " + Double.toString(motorhomeUpdated.getBasePrice()));
            lbKmDrivenSC.setVisible(true);
            lbKmDrivenSC.setText("KM Driven: " + Integer.toString(motorhomeUpdated.getKmDriven()));
            btnClose.setVisible(true);

            lbUpdateTitle.setText("MOTORHOME UPDATED SUCCESSFULLY");
        }

    }


}
