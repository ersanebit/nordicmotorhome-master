package Controller;

import Adapter.FileWrapper;
import Adapter.SQLAdapter;
import Model.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;


public class MainController {
    @FXML private TableView tblView, tblViewBottomLeft, tblViewBottomRight;
    @FXML private ComboBox<String> cbBox;
    @FXML private HBox hBoxBottom, toolBox;
    @FXML private VBox vBoxBottomLeft, vBoxBottomRight;
    @FXML private Label lblBottomLeft, lblBottomRight, lblUser, lblSeason;
    @FXML private TextField searchField;


    private ObservableList<Motorhome> motorhomes;
    private ObservableList<Service> rentals;
    private ObservableList<Service> reservations;
    private ObservableList<Maintenance> maintenances;
    private ObservableList<Extra> extras;
    private ObservableList<Location> locations;
    private ObservableList<Customer> customers;
    private SQLAdapter adapter = new SQLAdapter();
    private FileWrapper fileWrapper = new FileWrapper();
    Timer timer = new Timer(true);

    private void refreshTimer(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    customers = FXCollections.observableArrayList(adapter.getAllCustomers());
                    rentals = FXCollections.observableArrayList(adapter.getRentals());
                    motorhomes = FXCollections.observableArrayList(adapter.getMotorhome());
                    reservations =FXCollections.observableArrayList(adapter.getReservations());
                    maintenances = FXCollections.observableArrayList(adapter.getMaintenances());
                    extras = FXCollections.observableArrayList(adapter.getExtras());
                    locations = FXCollections.observableArrayList(adapter.getLocations());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (cbBox.getSelectionModel().getSelectedItem().equals("Motorhomes")) {
                    //refreshMotorhomes();
                    tblView.setItems(motorhomes);
                }
                if (cbBox.getSelectionModel().getSelectedItem().equals("Rentals")) {
                   //refreshRental();
                    tblView.setItems(rentals);
                }
                if (cbBox.getSelectionModel().getSelectedItem().equals("Reservations")) {
                   //refreshReservation();
                    tblView.setItems(reservations);
                }
                if (cbBox.getSelectionModel().getSelectedItem().equals("Maintenances")) {
                   //refreshMaintenance();
                    tblView.setItems(maintenances);
                }
                if (cbBox.getSelectionModel().getSelectedItem().equals("Extras")) {
                   //refreshExtras();
                    tblView.setItems(extras);
                }
                if (cbBox.getSelectionModel().getSelectedItem().equals("Locations")) {
                   //refreshLocations();
                    tblView.setItems(locations);
                }
                if (cbBox.getSelectionModel().getSelectedItem().equals("Customers")) {
                    /*try {
                        tblView.setItems(FXCollections.observableArrayList(adapter.getAllCustomers()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    tblView.setItems(customers);
                }

            }
        }, 0, 60*1000);
    }

    @FXML public void initialize() throws Exception{
        /*customers = FXCollections.observableArrayList(adapter.getAllCustomers());
        rentals = FXCollections.observableArrayList(adapter.getRentals());
        motorhomes = FXCollections.observableArrayList(adapter.getMotorhome());
        reservations =FXCollections.observableArrayList(adapter.getReservations());
        maintenances = FXCollections.observableArrayList(adapter.getMaintenances());
        extras = FXCollections.observableArrayList(adapter.getExtras());
        locations = FXCollections.observableArrayList(adapter.getLocations());*/
        refreshTimer();

        cbBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (cbBox.getSelectionModel().getSelectedItem().equals("Motorhomes")) {
                        showMotorhomeTable();
                        searchFunction(motorhomes);
                    }
                    if (cbBox.getSelectionModel().getSelectedItem().equals("Rentals")) {
                        showServices(rentals);
                        searchFunction(rentals);
                    }
                    if (cbBox.getSelectionModel().getSelectedItem().equals("Reservations")) {
                        showServices(reservations);
                        searchFunction(reservations);
                    }
                    if (cbBox.getSelectionModel().getSelectedItem().equals("Maintenances")) {
                        showMaintenanceTable();
                        searchFunction(maintenances);
                    }
                    if (cbBox.getSelectionModel().getSelectedItem().equals("Extras")) {
                        showExtrasTable();
                        searchFunction(extras);
                    }
                    if (cbBox.getSelectionModel().getSelectedItem().equals("Locations")) {
                        showLocationsTable();
                        searchFunction(locations);
                    }
                    if (cbBox.getSelectionModel().getSelectedItem().equals("Customers")) {
                        showCustomersTable();
                        searchFunction(customers);
                    }
                    //refreshTimer();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        cbBox.getSelectionModel().selectFirst();

        tblView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (cbBox.getSelectionModel().getSelectedItem().equals("Rentals") ||cbBox.getSelectionModel().getSelectedItem().equals("Reservations")) {
                Service service = (Service) tblView.getSelectionModel().getSelectedItem();
                if (service != null) {
                    tblViewBottomLeft.setItems(FXCollections.observableArrayList(service.getMotorhomes()));
                    tblViewBottomRight.setItems(FXCollections.observableArrayList(service.getExtras()));
                }
            }
            if (cbBox.getSelectionModel().getSelectedItem().equals("Motorhomes")) {
                Motorhome motorhome = (Motorhome) tblView.getSelectionModel().getSelectedItem();
                if (motorhome != null) {
                    tblViewBottomLeft.setItems(FXCollections.observableArrayList(motorhome.getRentals()));
                    tblViewBottomRight.setItems(FXCollections.observableArrayList(motorhome.getReservations()));
                }
            }
            if (cbBox.getSelectionModel().getSelectedItem().equals("Customers")) {
                Customer customer = (Customer) tblView.getSelectionModel().getSelectedItem();
                ArrayList<Rental> rentalsOfACustomer = new ArrayList<>();
                ArrayList<Reservation> reservationsOfACustomer = new ArrayList<>();
                if (customer != null) {
                    for (int i = 0; i < rentals.size(); i++) {
                        if(customer.getId() == rentals.get(i).getCustomer().getId()){
                            Rental rental = (Rental) rentals.get(i);
                            rentalsOfACustomer.add(rental);
                        }
                    }
                    for (int i = 0; i < reservations.size(); i++) {
                        if(customer.getId() == reservations.get(i).getCustomer().getId()){
                            Reservation reservation = (Reservation) reservations.get(i);
                            reservationsOfACustomer.add(reservation);
                        }
                    }
                    if (rentalsOfACustomer != null) {
                        tblViewBottomLeft.setItems(FXCollections.observableArrayList(rentalsOfACustomer));
                    }
                    if (reservationsOfACustomer != null) {
                        tblViewBottomRight.setItems(FXCollections.observableArrayList(reservationsOfACustomer));
                    }
                }
            }
        });

        lblSeason.setText(Season.getCurrentSeason() + " Season");

    }

    private void showCustomersTable() throws Exception{
        makeCustomersTable();
        tblView.setItems(customers);
        customersMenu();
        vBoxBottomLeft.setPrefWidth(456);
        vBoxBottomRight.setPrefWidth(456);
        lblBottomLeft.setText("Rentals");
        lblBottomRight.setText("Reservations");
        hBoxBottom.setVisible(true);
    }

    private void showMotorhomeTable() throws Exception{
        makeMotorhomesTable();
        tblView.setItems(motorhomes);
        motorhomesMenu();
        vBoxBottomLeft.setPrefWidth(456);
        vBoxBottomRight.setPrefWidth(456);
        lblBottomLeft.setText("Rental Periods");
        lblBottomRight.setText("Reservation Periods");
        hBoxBottom.setVisible(true);
    }

    private void showServices(ObservableList<Service> services) throws Exception{
        makeServicesTable(services);
        tblView.setItems(services);
        if(cbBox.getSelectionModel().getSelectedItem().equals("Rentals")) {
            rentalMenu();
        }else if (cbBox.getSelectionModel().getSelectedItem().equals("Reservations")){
            reservationMenu();
        }
        vBoxBottomLeft.setPrefWidth(584);
        vBoxBottomRight.setPrefWidth(328);
        lblBottomLeft.setText("Motorhomes List");
        lblBottomRight.setText("Extras");
        hBoxBottom.setVisible(true);
    }

    private void showMaintenanceTable() throws Exception{
        makeMaintenanceTable();
        tblView.setItems(maintenances);
        maintenanceMenu();
        hBoxBottom.setVisible(false);
    }

    private void showExtrasTable() throws Exception{
        makeExtraTable();
        tblView.setItems(extras);
        extrasMenu();
        hBoxBottom.setVisible(false);
    }

    private void showLocationsTable() throws Exception{
        makeLocationTable();
        tblView.setItems(locations);
        locationMenu();
        hBoxBottom.setVisible(false);
    }

    private void motorhomesMenu(){

        toolBox.getChildren().clear();
        Button btnCreateMotorhome = new Button("Create Motorhome");
        Button btnUpdateMotorhome = new Button("Update Motorhome");
        Button btnDeleteMotorhome = new Button("Delete Motorhome");
        toolBox.getChildren().addAll(btnCreateMotorhome, btnUpdateMotorhome, btnDeleteMotorhome);
        btnCreateMotorhome.setOnAction(e -> {
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/addMotorhome.fxml"));
                Parent root = fxmlLoader.load();
                stage.setTitle("Create Motorhome");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.hide();
                stage.show();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent ev) {
                        refreshMotorhomes();
                        ev.consume();
                    }
                });
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        });


        btnUpdateMotorhome.setOnAction(e -> {
            if (tblView.getSelectionModel().isEmpty()) {
                alert("Error", "Select a row");
            }else {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/updateMotorhome.fxml"));
                    Parent root = fxmlLoader.load();
                    stage.setTitle("Update Motorhome");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(root));
                    AddMotorhomeController controller = fxmlLoader.getController();
                    stage.hide();
                    controller.setMotorhome((Motorhome) tblView.getSelectionModel().getSelectedItem());
                    stage.show();
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent ev) {
                            refreshMotorhomes();
                            ev.consume();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnDeleteMotorhome.setOnAction(e -> {

            Motorhome selection  = (Motorhome) tblView.getSelectionModel().getSelectedItem();
            if (selection != null) {/*
                int x = 0;
                int y = 0;
                try {
                    for (int i=0; i < rentals.size(); i++){
                        for (int j = 0; j < rentals.get(i).getMotorhomes().size(); j++){
                            if (selection.getId() == rentals.get(i).getMotorhomes().get(j).getId()){
                              alert("Warning","Motorhome can not be deleted. It is rented.");
                                x = 1;
                            }
                        }
                    }
                    for (int i=0; i < reservations.size(); i++){
                        for (int j = 0; j < reservations.get(i).getMotorhomes().size(); j++){
                            if (selection.getId() == reservations.get(i).getMotorhomes().get(j).getId()){
                                alert("Warning","Motorhome can not be deleted. It is reserved.");
                                y = 1;
                            }
                        }
                    }
                    if (x == 1 && y == 1){
                        alert("Warning","Motorhome can not be deleted. It is reserved and rented.");
                    }
                    if (x == 0 && y == 0) {
                        adapter.deleteMotorhome(selection.getId());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
*/
                adapter.deleteMotorhome(selection.getId());
            }else{
               alert("Error","Motorhome is not selected");
                //System.out.println("not selected");
            }

            refreshMotorhomes();

        });

    }

    private void rentalMenu(){
        toolBox.getChildren().clear();
        Button addBtn = new Button("Register Rental");
        Button endBtn = new Button("End Rental");
        toolBox.getChildren().addAll(addBtn, endBtn);
        addBtn.setOnAction(e -> {
            try{
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/addService.fxml"));
                Parent root = fxmlLoader.load();
                AddServiceController contr = fxmlLoader.getController();
                contr.setServiceType("Rental");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Register Rental");
                stage.hide();
                stage.show();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent ev) {
                        refreshRental();
                        ev.consume();
                    }
                });

            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        });
        endBtn.setOnAction(e -> {
            if (!tblView.getSelectionModel().isEmpty()) {
                try {
                    Rental rental = (Rental) tblView.getSelectionModel().getSelectedItem();
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/endRental.fxml"));
                    Parent root = fxmlLoader.load();
                    EndRentalController contr = fxmlLoader.getController();
                    contr.setRental(rental);
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("End Rental");
                    stage.hide();
                    stage.show();
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent ev) {
                            refreshRental();
                            ev.consume();
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                alert(null, "Select a row!");
            }
        });
    }

    private void reservationMenu(){
        toolBox.getChildren().clear();
        Button addBtn = new Button("Register Reservation");
        Button makeBtn = new Button("Make this to Rental");
        Button cancelBtn = new Button("Cancel Reservation");
        toolBox.getChildren().addAll(addBtn,makeBtn, cancelBtn);
        addBtn.setOnAction(e -> {
            try{
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/addService.fxml"));
                Parent root = fxmlLoader.load();
                AddServiceController contr = fxmlLoader.getController();
                contr.setServiceType("Reservation");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Register Reservation");
                stage.hide();
                stage.show();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent ev) {
                        refreshReservation();
                        ev.consume();
                    }
                });

            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        });
        makeBtn.setOnAction(e -> {
            if (!tblView.getSelectionModel().isEmpty()) {
                Reservation reservation = (Reservation) tblView.getSelectionModel().getSelectedItem();
                if (LocalDate.parse(reservation.getStartDate()).isEqual(LocalDate.now())){
                    try {
                        adapter.deleteReservation(reservation);
                        adapter.addRental(reservation.toRental());
                        fileWrapper.createContract(reservation.toRental());
                        JOptionPane.showMessageDialog(null, "Successfully make this reservation into rental");
                        refreshRental();
                        refreshReservation();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else
                    alert("Warning", "Rental start date must be today!");

            }else
                alert(null, "Select a row!");
        });
        cancelBtn.setOnAction(e -> {
            if (!tblView.getSelectionModel().isEmpty()) {
                try{
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/confirmCancelReservation.fxml"));
                    Parent root = fxmlLoader.load();
                    CancelReservationController contr = fxmlLoader.getController();
                    Reservation reservation = (Reservation) tblView.getSelectionModel().getSelectedItem();
                    contr.setReservation(reservation);
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Cancel Reservation");
                    stage.hide();
                    stage.show();
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent ev) {
                            refreshReservation();
                            ev.consume();
                        }
                    });

                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }else
                alert(null, "Select a row!");
        });
    }

    private void maintenanceMenu() {
        Button btnCreateMaintenance = new Button("Create Maintenance");
        Button btnUpdateMaintenance = new Button("Update Maintenance");
        Button btnDeleteMaintenance = new Button("Delete Maintenance");
        toolBox.getChildren().clear();
        toolBox.getChildren().addAll(btnCreateMaintenance, btnUpdateMaintenance, btnDeleteMaintenance);
        // Method for the New Maintenance Button, where i open another fxml file
        btnCreateMaintenance.setOnAction(e -> {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("../FXML/addMaintenance.fxml"));
                stage.setTitle("New Maintenance");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.hide();
                stage.show();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent ev) {
                        refreshMaintenance();
                        ev.consume();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnUpdateMaintenance.setOnAction(ee -> {
            if (!tblView.getSelectionModel().isEmpty()) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/addMaintenance.fxml"));
                    Parent root = fxmlLoader.load();
                    stage.setTitle("Update Selected");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(root));
                    AddMaintenanceController controller = fxmlLoader.getController();

                    Maintenance maintenance = (Maintenance)tblView.getSelectionModel().getSelectedItem();
                    for (Motorhome motorhome : adapter.getMotorhome()) {
                        if (motorhome.getId() == maintenance.getCarId()) {
                            controller.updateMaintenace(motorhome, maintenance);
                            break;
                        }
                    }
                    stage.hide();
                    stage.show();
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent ev) {
                            refreshMaintenance();
                            ev.consume();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                alert("Error", "Select a row!");
            }
        });

        btnDeleteMaintenance.setOnAction(eee -> {
            if (!tblView.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Are you sure you want to delete this row ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    int id = ((Maintenance)tblView.getSelectionModel().getSelectedItem()).getCarId();
                    adapter.deleteMaintenance(id);
                    adapter.updateMotorhomeMaintenance(1,0,id);
                    refreshMaintenance();
                } else {
                    tblView.getSelectionModel().clearSelection();
                }
            } else {
                alert("Error", "Select a row!");
            }
        });
    }

    private void locationMenu(){
        Button btnCreateLocation = new Button("Create Location");
        Button btnUpdateLocation = new Button("Update Location");
        Button btnDeleteLocation = new Button("Delete Location");
        toolBox.getChildren().clear();
        toolBox.getChildren().addAll(btnCreateLocation, btnUpdateLocation, btnDeleteLocation);
        btnCreateLocation.setOnAction(e -> {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("../FXML/addLocations.fxml"));
                stage.setTitle("New Location");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.hide();
                stage.show();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent ev) {
                        refreshLocations();
                        ev.consume();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnUpdateLocation.setOnAction(e -> {
            if (tblView.getSelectionModel().isEmpty()) {
                alert("Error","Select a row!");
            }else {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/updateLocations.fxml"));
                    Parent root = fxmlLoader.load();
                    stage.setTitle("New Location");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(root));
                    AddLocationsController controller = fxmlLoader.getController();
                    stage.hide();
                    controller.setLocation((Location) tblView.getSelectionModel().getSelectedItem());
                    stage.show();
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent ev) {
                            refreshLocations();
                            ev.consume();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnDeleteLocation.setOnAction(eee -> {
            if (!tblView.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Are you sure you want to delete this row ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    try{
                        Location location = (Location) tblView.getSelectionModel().getSelectedItem();
                        //locations.remove(location);
                        //tblView.getItems().remove(location);
                        adapter.deleteLocation(location);
                        refreshLocations(); //Hieu
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    tblView.getSelectionModel().clearSelection();
                }
            } else {
               alert("Error", "Select a row!");
            }
        });
    }

    @FXML private void logOut(ActionEvent a){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/logIn.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.hide();
            stage.show();
            ((Node)(a.getSource())).getScene().getWindow().hide();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    private void extrasMenu(){
        Button btnCreateExtras = new Button("Create Extras");
        Button btnUpdateExtras = new Button("Update Extras");
        Button btnDeleteExtras = new Button("Delete Extras");
        toolBox.getChildren().clear();
        toolBox.getChildren().addAll(btnCreateExtras, btnUpdateExtras, btnDeleteExtras);
        btnCreateExtras.setOnAction(e -> {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("../FXML/addExtras.fxml"));
                stage.setTitle("New Extra");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.hide();
                stage.show();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent ev) {
                        refreshExtras();
                        ev.consume();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnUpdateExtras.setOnAction(e -> {
            if (tblView.getSelectionModel().isEmpty()) {
                alert("Error","Select a row!");
            }else {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/updateExtras.fxml"));
                    Parent root = fxmlLoader.load();
                    stage.setTitle("Update Extra");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(root));
                    AddExtrasController controller = fxmlLoader.getController();
                    stage.hide();
                    controller.setExtra((Extra) tblView.getSelectionModel().getSelectedItem());
                    stage.show();
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent ev) {
                            refreshExtras();
                            ev.consume();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnDeleteExtras.setOnAction(eee -> {
            if (!tblView.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Are you sure you want to delete this row ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    try{
                        Extra extra = (Extra) tblView.getSelectionModel().getSelectedItem();
                        //extras.remove(extra);
                        //tblView.getItems().remove(extra);
                        adapter.deleteExtra(extra);
                        refreshExtras();
                    }  catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                   tblView.getSelectionModel().clearSelection();
                }

            } else {
                alert("Error", "Select a row!");
            }
        });
    }

    public void refreshMotorhomes() {
        try {
            //tblView.refresh();
            motorhomes = FXCollections.observableArrayList(adapter.getMotorhome());
            tblView.setItems(motorhomes);
            rentals = FXCollections.observableArrayList(adapter.getRentals());
            reservations = FXCollections.observableArrayList(adapter.getReservations());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshCustomers() {
        try {
            //tblView.refresh();
            customers = FXCollections.observableArrayList(adapter.getCustomers());
            tblView.setItems(customers);
            rentals = FXCollections.observableArrayList(adapter.getRentals());
            reservations = FXCollections.observableArrayList(adapter.getReservations());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshMaintenance() {
        try {
            maintenances = FXCollections.observableArrayList(adapter.getMaintenances());
            tblView.setItems(maintenances);
            motorhomes = FXCollections.observableArrayList(adapter.getMotorhome());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshLocations() {
        try {
            locations = FXCollections.observableArrayList(adapter.getLocations());
            tblView.setItems(locations);
            rentals = FXCollections.observableArrayList(adapter.getRentals());
            reservations = FXCollections.observableArrayList(adapter.getReservations());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshExtras() {
        try {
            extras = FXCollections.observableArrayList(adapter.getExtras());
            tblView.setItems(extras);
            rentals = FXCollections.observableArrayList(adapter.getRentals());
            reservations = FXCollections.observableArrayList(adapter.getReservations());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshRental() {
        try {
            rentals = FXCollections.observableArrayList(adapter.getRentals());
            tblView.setItems(rentals);
            customers = FXCollections.observableArrayList(adapter.getAllCustomers());
            motorhomes = FXCollections.observableArrayList(adapter.getMotorhome());
            extras = FXCollections.observableArrayList(adapter.getExtras());
            locations = FXCollections.observableArrayList(adapter.getLocations());
            maintenances = FXCollections.observableArrayList(adapter.getMaintenances());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshReservation() {
        try {
            reservations = FXCollections.observableArrayList(adapter.getReservations());
            tblView.setItems(reservations);
            customers = FXCollections.observableArrayList(adapter.getAllCustomers());
            motorhomes = FXCollections.observableArrayList(adapter.getMotorhome());
            locations = FXCollections.observableArrayList(adapter.getLocations());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adminMenu() {
        cbBox.getItems().addAll("Motorhomes", "Rentals", "Reservations", "Maintenances", "Extras", "Locations", "Customers");
        cbBox.getSelectionModel().selectFirst();
        lblUser.setText("admin");
    }

    public void automecanicMenu() {
        cbBox.getItems().add("Maintenances");
        cbBox.getSelectionModel().selectFirst();
        lblUser.setText("automecanic");
    }

    public void salesstaffMenu() {
        cbBox.getItems().addAll("Motorhomes", "Rentals", "Reservations", "Maintenances", "Extras", "Locations", "Customers");
        cbBox.getSelectionModel().selectFirst();
        lblUser.setText("salesstaff");
    }

    public void cleaningstaffMenu() {
        cbBox.getItems().add("Maintenances");
        cbBox.getSelectionModel().selectFirst();
        lblUser.setText("cleaningstaff");

    }

    public void bookkeperMenu() {
        cbBox.getItems().addAll("Rentals", "Reservations", "Maintenances");
        cbBox.getSelectionModel().selectFirst();
        lblUser.setText("bookkeper");
    }

    private void customersMenu(){
        Button btnCreateCustomer = new Button("Create Customer");
        Button btnUpdateCustomer = new Button("Update Customer");
        Button btnDeleteCustomer = new Button("Delete Customer");
        toolBox.getChildren().clear();
        toolBox.getChildren().addAll(btnCreateCustomer, btnUpdateCustomer, btnDeleteCustomer);

        btnCreateCustomer.setOnAction(e -> {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("../FXML/addCustomer.fxml"));
                stage.setTitle("New Customer");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.hide();
                stage.show();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent ev) {
                        refreshCustomers();
                        ev.consume();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnUpdateCustomer.setOnAction(e -> {
            if (tblView.getSelectionModel().isEmpty()) {
                alert("Error","Select a row!");
            }else {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/updateCustomer.fxml"));
                    Parent root = fxmlLoader.load();
                    stage.setTitle("Update Customer");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(root));
                    ManageCustomerController controller = fxmlLoader.getController();
                    stage.hide();
                    controller.setCustomer((Customer) tblView.getSelectionModel().getSelectedItem());
                    stage.show();
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent ev) {
                            refreshCustomers();
                            ev.consume();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnDeleteCustomer.setOnAction(eee -> {
            if (!tblView.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Are you sure you want to delete this row ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    Customer customerSelected  = (Customer) tblView.getSelectionModel().getSelectedItem();/*
                    if (customerSelected != null) {
                        lbInfo.setText("");
                        int x = 0;
                        int y = 0;
                        for (int i = 0; i < rentals.size(); i++) {
                            if (customerSelected.getId() == rentals.get(i).getCustomer().getId()) {
                                lbInfo.setText("Customer can not be deleted. It has rental.");
                                x = 1;
                            }
                        }
                        for (int i = 0; i < reservations.size(); i++) {
                            if (customerSelected.getId() == reservations.get(i).getCustomer().getId()) {
                                lbInfo.setText("Customer can not be deleted. It has reservation.");
                                x = 2;
                            }
                        }
                        if (x == 1 && y == 1) {
                            lbInfo.setText("Customer can not be deleted. It is reserved and rented.");
                        }
                        if (x == 0 && y == 0) {
                            Customer customer = (Customer) tblView.getSelectionModel().getSelectedItem();
                            customers.remove(customer);
                            tblView.getItems().remove(customer);
                            adapter.deleteCustomer(customerSelected.getId());
                        }
                    }*/
                        adapter.deleteCustomer(customerSelected.getId());
                } else {
                    tblView.getSelectionModel().clearSelection();
                }

            } else {
                alert("Error", "Select a row!");
            }
            refreshCustomers();
        });
    }

    private void makeMotorhomesTable(){
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

        TableColumn statusColumn = new TableColumn("Status");

        TableColumn<Motorhome, String> isCleanColumn = new TableColumn<>("Cleaned");
        isCleanColumn.setCellValueFactory(new PropertyValueFactory<>("cleanString"));

        TableColumn<Motorhome, String> isBrokenColumn = new TableColumn<>("Broken");
        isBrokenColumn.setCellValueFactory(new PropertyValueFactory<>("brokenString"));

        TableColumn<Motorhome, String> isReservedColumn = new TableColumn<>("Reserved");
        isReservedColumn.setCellValueFactory(new PropertyValueFactory<>("reservedString"));

        TableColumn<Motorhome, String> isRentedColumn = new TableColumn<>("Rented");
        isRentedColumn.setCellValueFactory(new PropertyValueFactory<>("rentedString"));

        statusColumn.getColumns().addAll(isCleanColumn,isBrokenColumn,isReservedColumn,isRentedColumn);

        TableColumn<Service, Integer> rentalIdColumn = new TableColumn<>("ID");
        rentalIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Service, String> rentalStartDateColumn = new TableColumn<>("Start Date");
        rentalStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Service, String> rentalEndDateColumn = new TableColumn<>("End Date");
        rentalEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Service, Integer> reservationIdColumn = new TableColumn<>("ID");
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Service, String> reservationStartDateColumn = new TableColumn<>("Start Date");
        reservationStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Service, String> reservationEndDateColumn = new TableColumn<>("End Date");
        reservationEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        //motorhomes = FXCollections.observableArrayList(adapter.getMotorhomes());

        tblView.getColumns().clear();
        tblView.getColumns().addAll(idColumn, brandColumn, modelColumn, capacityColumn, basePriceColumn, priceColumn, kilometerColumn, statusColumn);

        tblViewBottomLeft.getColumns().clear();
        tblViewBottomLeft.getColumns().addAll(rentalIdColumn, rentalStartDateColumn, rentalEndDateColumn);
        tblViewBottomRight.getColumns().clear();
        tblViewBottomRight.getColumns().addAll(reservationIdColumn,reservationStartDateColumn,reservationEndDateColumn);

        tblViewBottomLeft.getItems().clear();
        tblViewBottomRight.getItems().clear();
    }

    private void makeCustomersTable(){
        TableColumn<Customer, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> cprColumn = new TableColumn<>("CPR");
        cprColumn.setCellValueFactory(new PropertyValueFactory<>("cpr"));

        TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Customer, String> phoneNumberColumn = new TableColumn<>("Phone number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


        TableColumn<Service, Integer> rentalIdColumn = new TableColumn<>("ID");
        rentalIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Service, String> rentalStartDateColumn = new TableColumn<>("Start Date");
        rentalStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Service, String> rentalEndDateColumn = new TableColumn<>("End Date");
        rentalEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Service, Integer> reservationIdColumn = new TableColumn<>("ID");
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Service, String> reservationStartDateColumn = new TableColumn<>("Start Date");
        reservationStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Service, String> reservationEndDateColumn = new TableColumn<>("End Date");
        reservationEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        tblView.getColumns().clear();
        tblView.getColumns().addAll(idColumn, nameColumn, cprColumn, addressColumn, emailColumn, phoneNumberColumn );

        tblViewBottomLeft.getColumns().clear();
        tblViewBottomLeft.getColumns().addAll(rentalIdColumn, rentalStartDateColumn, rentalEndDateColumn);
        tblViewBottomRight.getColumns().clear();
        tblViewBottomRight.getColumns().addAll(reservationIdColumn,reservationStartDateColumn,reservationEndDateColumn);

        tblViewBottomLeft.getItems().clear();
        tblViewBottomRight.getItems().clear();
    }

    private void makeServicesTable(ObservableList<Service> services){
        TableColumn<Service, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Service, String> nameColumn = new TableColumn<>("Customer Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        TableColumn<Service, String> cprColumn = new TableColumn<>("CPR");
        cprColumn.setCellValueFactory(new PropertyValueFactory<>("customerCpr"));

        TableColumn<Service, String> phoneNumberColumn = new TableColumn<>("Phone");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cusPhoneNumber"));

        TableColumn<Service, String> reservationDateColumn = new TableColumn<>("Reservation Date");
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));

        TableColumn<Service, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Service, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Service, Integer> dropOffColumn = new TableColumn<>("Drop off");
        dropOffColumn.setMinWidth(150);
        dropOffColumn.setCellValueFactory(new PropertyValueFactory<>("dropOffAdd"));

        TableColumn<Service, Integer> pickUpColumn = new TableColumn<>("Pick Up");
        pickUpColumn.setMinWidth(150);
        pickUpColumn.setCellValueFactory(new PropertyValueFactory<>("pickUpAdd"));

        TableColumn<Service, Double> priceColumn = new TableColumn<>("Total Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Motorhome, Integer> motorhomeIdColumn = new TableColumn<>("ID");
        motorhomeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Motorhome, String> motorhomeBrandColumn = new TableColumn<>("Brand");
        motorhomeBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Motorhome, String> motorhomeModelColumn = new TableColumn<>("Model");
        motorhomeModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Motorhome, Integer> motorhomeCapacityColumn = new TableColumn<>("Capacity");
        motorhomeCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Motorhome, Double> motorhomePriceColumn = new TableColumn<>("Price/day");
        motorhomePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Motorhome, Integer> motorhomeKilometerColumn = new TableColumn<>("Kilometer");
        motorhomeKilometerColumn.setCellValueFactory(new PropertyValueFactory<>("kmDriven"));

        TableColumn<Extra, String> extraNameColumn = new TableColumn<>("Name");
        extraNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Extra, Integer> extraQuantityColumn = new TableColumn<>("Quantity");
        extraQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Extra, Double> extraPriceColumn = new TableColumn<>("Price/each");
        extraPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tblViewBottomLeft.getColumns().clear();
        tblViewBottomLeft.getColumns().addAll(motorhomeIdColumn,motorhomeBrandColumn,motorhomeModelColumn,motorhomeCapacityColumn,motorhomePriceColumn,motorhomeKilometerColumn);

        tblViewBottomRight.getColumns().clear();
        tblViewBottomRight.getColumns().addAll(extraNameColumn,extraQuantityColumn,extraPriceColumn);
        tblViewBottomLeft.getItems().clear();
        tblViewBottomRight.getItems().clear();

        tblView.getColumns().clear();
        if(services.equals(rentals)) {
            tblView.getColumns().addAll(idColumn, nameColumn, cprColumn,phoneNumberColumn, startDateColumn, endDateColumn,pickUpColumn,dropOffColumn, priceColumn);
        }else{
            tblView.getColumns().addAll(idColumn, nameColumn, cprColumn,phoneNumberColumn,reservationDateColumn, startDateColumn, endDateColumn,pickUpColumn,dropOffColumn, priceColumn);
        }
    }

    private void makeMaintenanceTable(){
        TableColumn<Maintenance, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Maintenance, Integer> carIDColumn = new TableColumn<>("Car ID");
        carIDColumn.setMinWidth(50);
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));

        TableColumn<Maintenance, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setMinWidth(50);
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("carBrand"));

        TableColumn<Maintenance, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setMinWidth(50);
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("carModel"));

        TableColumn<Maintenance, String> isCleanedColumn = new TableColumn<>("Cleaned");
        isCleanedColumn.setMinWidth(50);
        isCleanedColumn.setCellValueFactory(new PropertyValueFactory<>("cleanedString"));

        TableColumn<Maintenance, String> isBrokenColumn = new TableColumn<>("Broken");
        isBrokenColumn.setMinWidth(50);
        isBrokenColumn.setCellValueFactory(new PropertyValueFactory<>("brokenString"));

        TableColumn<Maintenance, String> isCheckedColumn = new TableColumn<>("Checked");
        isCheckedColumn.setMinWidth(50);
        isCheckedColumn.setCellValueFactory(new PropertyValueFactory<>("checkedString"));

        TableColumn<Maintenance, Double> repairCostColumn = new TableColumn<>("Repair Cost");
        repairCostColumn.setMinWidth(50);
        repairCostColumn.setCellValueFactory(new PropertyValueFactory<>("repairCost"));


        TableColumn<Maintenance, String> messageColumn = new TableColumn<>("Message");
        messageColumn.setMinWidth(50);
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

        tblView.getColumns().clear();
        tblView.getColumns().addAll(idColumn,carIDColumn,brandColumn,modelColumn,isCleanedColumn,isBrokenColumn,repairCostColumn, messageColumn);
    }

    private void makeExtraTable(){
        TableColumn<Extra, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Extra, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(50);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Extra, Integer> quanCol = new TableColumn<>("Quantity in Stock");
        quanCol.setMinWidth(150);
        quanCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Extra, Double> priceCol = new TableColumn<>("Price for each");
        priceCol.setMinWidth(150);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        tblView.getColumns().clear();
        tblView.getColumns().addAll(idColumn,nameCol, quanCol, priceCol);
    }

    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }

    private void makeLocationTable(){
        TableColumn<Location, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Location, String> addressCol = new TableColumn<>("Address");
        addressCol.setMinWidth(50);
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Location, Integer> kmCol = new TableColumn<>("Kilometer");
        kmCol.setMinWidth(50);
        kmCol.setCellValueFactory(new PropertyValueFactory<>("km"));

        TableColumn<Location, Integer> privateCol = new TableColumn<>("Address Type");
        privateCol.setMinWidth(200);
        privateCol.setCellValueFactory(new PropertyValueFactory<>("isPrivateString"));

        tblView.getColumns().clear();
        tblView.getColumns().addAll(idColumn,addressCol, kmCol,privateCol);
    }

    private <E> void searchFunction(ObservableList<E> list) {
        searchField.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                if(searchField.getText().isEmpty()) {
                    tblView.setItems(list);
                    return;
                }
                ObservableList<E> tableItems = FXCollections.observableArrayList();
                for(E item : list) {
                    String cellValue = item.toString().toLowerCase();
                    if(cellValue.contains(searchField.getText().toLowerCase())) {
                        tableItems.add(item);
                    }
                }
                tblView.setItems(tableItems);
            }
        });
    }
}
