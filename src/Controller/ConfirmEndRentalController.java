package Controller;

import Adapter.SQLAdapter;
import Model.Motorhome;
import Model.Rental;
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
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Hieu on 20-May-17.
 */
public class ConfirmEndRentalController {
    private Rental rental;
    private ObservableList<Motorhome> motorhomes;
    private SQLAdapter adapter = new SQLAdapter();

    @FXML TableView tblViewMot;
    @FXML Label lblKm,lblPriceKm, lblPriceFuel,lblMaintenanceCost,lblPriceEachMot,lblTotalPrice;
    @FXML Button btnBack;

    @FXML
    public void initialize() throws Exception {
        tblViewMot.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Motorhome motorhome = (Motorhome) newSelection;
            lblKm.setText(motorhome.getKmExcess() + " km");
            lblPriceKm.setText(motorhome.getKmPrice() + " €");
            lblMaintenanceCost.setText(motorhome.getMaintenancePrice() + " €");
            lblPriceFuel.setText(motorhome.getFuelPrice() + " €");
            lblPriceEachMot.setText(motorhome.getTotalAdditionalPrice() + " €");
        });

        showMotorhomeTable();
    }

    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }

    public void setRental(Rental rental){
        this.rental = rental;
        rental.setRentalDaysToMotorhome();
        motorhomes = FXCollections.observableArrayList(rental.getMotorhomes());
        tblViewMot.setItems(motorhomes);
        tblViewMot.getSelectionModel().selectFirst();

        lblTotalPrice.setText(rental.getMotorhomesAdditionalPrice() + " €");

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

    public void btnEndOnClick(ActionEvent actionEvent) throws SQLException {
        adapter.deleteRental(rental);
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        alert(null, "Rental has been ended");
    }

    public void btnBackOnClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/endRental.fxml"));
        Parent root = loader.load();
        EndRentalController controller = (EndRentalController) loader.getController();
        controller.setRental(rental);

        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(new Scene( root));
        stage.show();
    }
}
