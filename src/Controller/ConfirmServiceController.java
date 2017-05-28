package Controller;

import Adapter.FileWrapper;
import Adapter.SQLAdapter;
import Model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Hieu on 18-May-17.
 */
public class ConfirmServiceController {
    @FXML
    private Label lblCpr, lblName, lblAddress, lblEmail, lblStartDate, lblEndDate, lblPriceMot, lblPriceExtra, lblPickup, lblPricePickup,lblDropoff,lblPriceDropoff,lblTotalPrice, lblConfirmationPage, lblPhoneNumber;
    @FXML private Button btnBack, btnRegister;
    @FXML private TableView tblViewMot, tblViewExtra;

    private SQLAdapter adapter = new SQLAdapter();
    private FileWrapper fileWrapper = new FileWrapper();
    private Service service;

    @FXML public void initialize() throws Exception {

    }

    public void setService(Service service){
        this.service = service;
        lblCpr.setText(service.getCustomer().getCpr());
        lblName.setText(service.getCustomer().getName());
        lblAddress.setText(service.getCustomer().getAddress());
        lblEmail.setText(service.getCustomer().getEmail());
        lblPhoneNumber.setText(service.getCustomer().getPhoneNumber());

        lblStartDate.setText(service.getStartDate());
        lblEndDate.setText(service.getEndDate());

        TableColumn<Extra, String> exNameColumn = new TableColumn<>("Name");
        exNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Extra, Integer> exQuantityColumn = new TableColumn<>("Quantity");
        exQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Extra, Double> exPriceColumn = new TableColumn<>("Price per each");
        exPriceColumn.setMinWidth(80);
        exPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tblViewExtra.getColumns().addAll(exNameColumn, exQuantityColumn, exPriceColumn);
        tblViewExtra.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Motorhome, Integer> motIdColumn = new TableColumn<>("ID");
        motIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Motorhome, String> motBrandColumn = new TableColumn<>("Brand");
        motBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Motorhome, String> motModelColumn = new TableColumn<>("Model");
        motModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Motorhome, Double> motPriceColumn = new TableColumn<>("Price per each");
        motPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tblViewMot.getColumns().addAll(motIdColumn,motBrandColumn, motModelColumn, motPriceColumn);
        tblViewMot.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tblViewMot.setItems(FXCollections.observableArrayList(service.getMotorhomes()));
        tblViewExtra.setItems(FXCollections.observableArrayList(service.getExtras()));

        lblPriceMot.setText(service.getMotorhomesPrice() + " €");
        lblPriceExtra.setText(service.getExtrasPrice() + " €");
        lblPickup.setText(service.getPickUp().getAddress());
        lblDropoff.setText(service.getDropOff().getAddress());
        lblPricePickup.setText(service.getPickUp().getPrice() + " €");
        lblPriceDropoff.setText(service.getDropOff().getPrice() + " €");

        lblTotalPrice.setText(service.getTotalPrice() + " €");

        if (service instanceof Reservation) {
            btnRegister.setText("Register Reservation");
            lblConfirmationPage.setText("Reservation Confirmation Page");
        }
    }

    public void registerService(ActionEvent actionEvent) throws SQLException, FileNotFoundException {
        if (service instanceof Rental){
            Rental rental = (Rental) service;
            alert("Payment", "Total amount: " + rental.getTotalPrice() + " €. Waiting for payment... Click OK to Continue");
            alert("Confirmation", "Payment succeeded. Contract has been printed out.");
            adapter.addRental(rental);
            fileWrapper.createContract(rental);
        } else if (service instanceof Reservation){
            Reservation reservation = (Reservation) service;
            adapter.addReservation(reservation);
            alert("Information", "Reservation has been registered");
        }
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }

    public void btnBackOnClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/addService.fxml"));
        Parent root = loader.load();
        AddServiceController controller = (AddServiceController) loader.getController();
        controller.setData(service);

        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(new Scene( root));
        stage.show();
    }

}
