package Controller;


import Adapter.SQLAdapter;
import Model.Maintenance;
import Model.Motorhome;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


/**
 * Created by Andrei
 */
public class AddMaintenanceController {
    private SQLAdapter adapter = new SQLAdapter();
    @FXML VBox vBox;
    Label label = new Label("Select Motorhome");
    ComboBox<Motorhome> motorList = new ComboBox<>();
    ComboBox<String> cleanList = new ComboBox<>();
    ComboBox<String> brokenList = new ComboBox<>();
    TextField costField = new TextField();
    TextArea text = new TextArea();

    public void updateMaintenace(Motorhome motorhome, Maintenance maintenance){
        label.setText("Modify wisely");
        motorList.setValue(motorhome);
        motorList.setDisable(true);
        motorList.setOpacity(10000);
        cleanList.setValue(maintenance.isCleaned() ? "Clean" : "Not Clean");
        brokenList.setValue(maintenance.isBroken() ? "Broken" : "Not Broken");
        costField.setText(maintenance.getRepairCost()+"");
        text.setText(maintenance.getMessage());

    }
    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }

    @FXML public void initialize(){

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        HBox hBox2 = new HBox();
        costField.setPromptText("cost");
        costField.setPrefWidth(50);

        text.setPromptText("Message: ");
        text.setPrefWidth(180);
        text.setPrefHeight(100);
        ArrayList<Motorhome> newMotorList = new ArrayList<>();
        try {
            for(Motorhome motorhome: adapter.getMotorhome()){
                if(!motorhome.isBroken() &&motorhome.isCleaned())
                    newMotorList.add(motorhome);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        motorList.getItems().addAll(newMotorList);
        motorList.setPromptText("Motorhome");
        motorList.setPrefWidth(180);
        cleanList.getItems().addAll("Clean","Not Clean");
        cleanList.setPromptText("Choose");
        cleanList.setPrefWidth(180);
        brokenList.getItems().addAll("Broken","Not Broken");
        brokenList.setPromptText("Choose");
        brokenList.setPrefWidth(180);

        vBox.getChildren().addAll(label,motorList,cleanList,brokenList,costField,text,hBox2);
        vBox.setSpacing(25);
        vBox.setAlignment(Pos.CENTER);
        hBox2.getChildren().addAll(cancelButton,saveButton);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.setSpacing(100);
        cancelButton.setOnAction(actionEvent ->{
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        });
        saveButton.setOnAction(ac ->{
            double cost;
            if(motorList.getSelectionModel().isEmpty() && !motorList.isDisable()){
                alert("Error","Select a motorhome");
            }else
            if(cleanList.getSelectionModel().isEmpty()) {
                alert("Warning", "Pick a Clean Status!");
            }else
            if (brokenList.getSelectionModel().isEmpty()) {
                alert("Warning", "Pick a Broke Status");
            }else
            if (cleanList.getSelectionModel().getSelectedItem().equals("Clean")&&
                    brokenList.getSelectionModel().getSelectedItem().equals("Not Broken")) {
                alert("Warning", "No need for maintenance!");
            }else
            if (costField.getText().trim().isEmpty()) {
                alert("Error", "Cost field is empty");
            }else
            if(!isDouble(costField.getText())){
                alert("Error","Wrong type for Cost filed");
            }else
                {
                cost = Double.parseDouble("0"+costField.getText());
                int carId = motorList.getSelectionModel().getSelectedItem().getId();
                int cleaned = cleanList.getSelectionModel().getSelectedItem().equals("Clean") ? 1 : 0;
                int broken = brokenList.getSelectionModel().getSelectedItem().equals("Broken") ? 1 : 0;
                boolean updated = false;
                try {
                    ArrayList<Maintenance> maintenanceList = adapter.getMaintenances();
                    for (Maintenance maintenance : maintenanceList) {
                        if (maintenance.getCarId() == carId) {
                            adapter.updateMaintenance(cost, text.getText(), carId);
                            updated = true;
                            break;
                        }
                    }
                    if (!updated) {
                        adapter.insertMaintenance(cost, text.getText(), carId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            adapter.updateMotorhomeMaintenance(cleaned, broken, carId);
            ((Node) (ac.getSource())).getScene().getWindow().hide();

            }
        });
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
    }
