package Controller;

import Adapter.SQLAdapter;
import Model.Motorhome;
import Model.Reservation;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Created by Hieu on 22-May-17.
 */
public class CancelReservationController {
    private Reservation reservation;
    private SQLAdapter adapter = new SQLAdapter();

    @FXML
    TableView tblViewMot;
    @FXML
    Label lblReservationDate,lblStartDate,lblEndDate, lblDays, lblReservationPrice,lblPercentage,lblTotalPrice;
    public void setReservation(Reservation reservation){
        this.reservation = reservation;

        makeMotorhomeTable();
        tblViewMot.setItems(FXCollections.observableArrayList(reservation.getMotorhomes()));

        lblReservationDate.setText(reservation.getReservationDate());
        lblStartDate.setText(reservation.getStartDate());
        lblEndDate.setText(reservation.getEndDate());
        lblDays.setText(reservation.getDayToRental() + " days");
        lblReservationPrice.setText(reservation.getTotalPrice()  + " €");
        lblPercentage.setText((reservation.getPercentage() * 100) + " %");
        lblTotalPrice.setText(reservation.getCancellationPrice() + " €");
    }

    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }

    private void makeMotorhomeTable(){
        TableColumn<Motorhome, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Motorhome, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Motorhome, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Motorhome, Integer> capacityColumn = new TableColumn<>("Capacity");
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Motorhome, Double> basePriceColumn = new TableColumn<>("Base Prise");
        basePriceColumn.setCellValueFactory(new PropertyValueFactory<>("basePrice"));

        TableColumn<Motorhome, Double> priceColumn = new TableColumn<>("Current Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Motorhome, Integer> kilometerColumn = new TableColumn<>("Kilometer");
        kilometerColumn.setCellValueFactory(new PropertyValueFactory<>("kmDriven"));

        tblViewMot.getColumns().addAll(idColumn, brandColumn, modelColumn, capacityColumn, basePriceColumn, priceColumn, kilometerColumn);
    }

    public void btnConfirmOnClick(ActionEvent actionEvent) throws SQLException{
        adapter.deleteReservation(reservation);
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        alert(null, "Reservation has been canceled");
    }

    public void btnCancelOnClick(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
