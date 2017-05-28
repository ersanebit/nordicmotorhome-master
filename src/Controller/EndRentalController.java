package Controller;

import Model.Motorhome;
import Model.Rental;
import Model.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;


/**
 * Created by Hieu on 19-May-17.
 */
public class EndRentalController {
    private Rental rental;
    private ObservableList<Motorhome> motorhomes;
    private Motorhome motorhome;

    @FXML TableView tblViewMot;
    @FXML TextField txtKm, txtMaintenanceCost;
    @FXML ComboBox cbTankStatus;
    @FXML CheckBox checkBoxBroken;
    @FXML TextArea txtComment;
    @FXML Pane infoPane;
    @FXML Button btnNext;

    @FXML
    public void initialize() throws Exception {
        cbTankStatus.getItems().addAll("Almost Full", "More than half" ,"Less than half", "Almost empty");

        tblViewMot.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            motorhome = (Motorhome) newSelection;
            infoPane.setDisable(false);
            cbTankStatus.setValue(null);
            txtKm.clear();
            txtComment.clear();
            txtMaintenanceCost.clear();
            checkBoxBroken.setSelected(false);
        });

        checkBoxBroken.selectedProperty().addListener(e -> {
            if (checkBoxBroken.isSelected()){
                txtComment.setDisable(false);
                txtMaintenanceCost.setDisable(false);
            }
            else{
                txtComment.setDisable(true);
                txtMaintenanceCost.setDisable(true);
            }
        });

        showMotorhomeTable();
    }

    public void setRental(Rental rental){
        this.rental = rental;
        motorhomes = FXCollections.observableArrayList(rental.getMotorhomes());
        tblViewMot.setItems(motorhomes);
    }

    private void showMotorhomeTable(){
        TableColumn<Motorhome, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Motorhome, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Motorhome, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Motorhome, Integer> kmColumn = new TableColumn<>("Old Km");
        kmColumn.setCellValueFactory(new PropertyValueFactory<>("kmDriven"));

        TableColumn<Motorhome, Integer> newKmColumn = new TableColumn<>("New Km");
        newKmColumn.setCellValueFactory(new PropertyValueFactory<>("newKmDriven"));

        TableColumn<Motorhome, String> statusColumn = new TableColumn<>("Tank Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("tankStatus"));

        TableColumn<Motorhome, String> isBrokenColumn = new TableColumn<>("Broken");
        isBrokenColumn.setCellValueFactory(new PropertyValueFactory<>("brokenString"));

        TableColumn<Motorhome, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setMinWidth(200);
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("note"));

        tblViewMot.getColumns().addAll(idColumn, brandColumn, modelColumn, kmColumn, newKmColumn, statusColumn, isBrokenColumn, commentColumn);
    }

    public void updateMotCondition(ActionEvent actionEvent) {
        if (!txtKm.getText().isEmpty() && !cbTankStatus.getSelectionModel().isEmpty()) {
            if  (isInt(txtKm.getText())) {
                int newKm = Integer.parseInt(txtKm.getText());
                if (motorhome.getKmDriven() <= newKm) {
                    motorhome.setNewKmDriven(newKm);
                    motorhome.setTankStatus(cbTankStatus.getSelectionModel().getSelectedItem().toString());
                    if (checkBoxBroken.isSelected()) {
                        if (isDouble(txtMaintenanceCost.getText())) {
                            motorhome.setNote(txtComment.getText());
                            motorhome.setMaintenancePrice(Integer.parseInt(txtMaintenanceCost.getText()));
                            motorhome.setBroken(true);
                        } else {
                            alert("Error", "Invalid maintenance cost. Cost should be a number!");
                            return;
                        }
                    } else
                        motorhome.setBroken(false);
                    tblViewMot.refresh();
                    txtKm.clear();
                    cbTankStatus.setValue(null);
                    txtComment.clear();
                    txtMaintenanceCost.clear();
                } else
                    alert("Error", "Kilometer should be higher than the old one!");
            } else
                alert("Error", "Invalid kilometer type. Kilometer should be integer!");
        } else {
            alert("Error", "Please fill in enough information!");
        }
    }

    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }

    public void btnCancelOnClick(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void btnNextOnClick(ActionEvent actionEvent) throws IOException{
        if (checkMotorhomeInfo()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/confirmEndRental.fxml"));
            Parent root = loader.load();
            ConfirmEndRentalController controller = (ConfirmEndRentalController) loader.getController();
            controller.setRental(rental);

            Stage stage = (Stage) btnNext.getScene().getWindow();
            stage.setScene(new Scene( root));
            stage.show();

        } else {
            alert("Error", "Please update new status for all motorhomes!");
        }
    }

    private boolean checkMotorhomeInfo(){
        for (Motorhome motorhome : motorhomes){
            if (motorhome.getNewKmDriven() == 0 || motorhome.getTankStatus().equals(""))
                return false;
        }
        return true;
    }
    public boolean isInt(String s)
    {
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
}
