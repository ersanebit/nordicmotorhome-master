package Controller;


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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rolis on 5/13/2017.
 */

public class AddServiceController {
    private SQLAdapter adapter = new SQLAdapter();

    @FXML private TextField txtCpr, txtName, txtAddress, txtEmail, txtPickUpAdd, txtPickUpKm, txtDropOffAdd, txtDropOffKm,phoneNumber;
    @FXML private DatePicker startDate, endDate;
    @FXML private ComboBox<Motorhome> cBoxMotorhome;
    @FXML private ComboBox<Location> cBoxPickUp, cBoxDropOff;
    @FXML private ComboBox<Extra> cBoxExtras;
    @FXML private ComboBox<Integer> cBoxQuantity;
    @FXML private TableView<Motorhome> tViewMotorhome;
    @FXML private TableView<Extra> tViewExtras;
    @FXML private Label lblAddExtra, lblAddMot, lblPickUpAddress, lblDropOffAddress;
    @FXML private CheckBox checkBoxPickUp, checkBoxDropOff;
    @FXML private Pane pickUpPane, dropOffPane;
    @FXML private Button btnAddPickUp, btnAddDropOff, btnRemoveMotorhome, btnRemoveExtra, btnAddMotorhome, btnAddExtra, btnConfirmPeriod, btnNext;

    private ArrayList<Motorhome> motorhomes;
    private ArrayList<Location> locations;
    private ArrayList<Extra> extras;
    private ArrayList<Customer> customerList;
    private ArrayList<Extra> selectedExtras;
    private ArrayList<Motorhome> selectedMotorhomes;
    private Customer customer;
    private Location pickUp;
    private Location dropOff;
    private String serviceType;
    Service service;


    @FXML public void initialize() throws Exception {
        locations = adapter.getLocationList();
        extras = adapter.getExtrasList();
        customerList = adapter.getAllCustomers();
        selectedExtras = new ArrayList<>();
        selectedMotorhomes = new ArrayList<>();
        cBoxPickUp.getItems().addAll(locations);
        cBoxDropOff.getItems().addAll(locations);
        cBoxExtras.getItems().addAll(extras);
        cBoxExtras.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> refreshExtraQuantity(newValue));
        cBoxPickUp.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> pickUp = newValue);
        cBoxDropOff.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> dropOff = newValue);
        cBoxMotorhome.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                btnAddMotorhome.setDisable(false);
            } else {
                btnAddMotorhome.setDisable(true);
            }

        });
        cBoxQuantity.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                btnAddExtra.setDisable(false);
            } else {
                btnAddExtra.setDisable(true);
            }
        });

        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());
        setDisabledPeriod(startDate);
        setDisabledPeriod(endDate);

        TableColumn<Extra, String> exNameColumn = new TableColumn<>("Name");
        exNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Extra, Integer> exQuantityColumn = new TableColumn<>("Quantity");
        exQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tViewExtras.getColumns().addAll(exNameColumn, exQuantityColumn);
        tViewExtras.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tViewExtras.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnRemoveExtra.setDisable(false);
            } else
                btnRemoveExtra.setDisable(true);
        });

        TableColumn<Motorhome, Integer> motIdColumn = new TableColumn<>("ID");
        motIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Motorhome, String> motBrandColumn = new TableColumn<>("Brand");
        motBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Motorhome, String> motModelColumn = new TableColumn<>("Model");
        motModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        tViewMotorhome.getColumns().addAll(motIdColumn,motBrandColumn, motModelColumn);
        tViewMotorhome.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tViewMotorhome.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnRemoveMotorhome.setDisable(false);
            } else
                btnRemoveMotorhome.setDisable(true);
        });
    }

    public void setData(Service service){
        this.service = service;
        if (service instanceof Rental)
            setServiceType("Rental");
        else if (service instanceof Reservation)
            setServiceType("Reservation");

        txtCpr.setText(service.getCustomer().getCpr());
        txtName.setText(service.getCustomer().getName());
        txtEmail.setText(service.getCustomer().getEmail());
        txtAddress.setText(service.getCustomer().getAddress());
        phoneNumber.setText(service.getCustomer().getPhoneNumber());
        startDate.setValue(LocalDate.parse(service.getStartDate()));
        endDate.setValue(LocalDate.parse(service.getEndDate()));

        cBoxMotorhome.setDisable(false);
        motorhomes = adapter.getAvailableMotorhomeList(startDate.getValue(), endDate.getValue());
        cBoxMotorhome.getItems().addAll(motorhomes);
        btnConfirmPeriod.setText("Remove Period");
        startDate.setDisable(true);
        endDate.setDisable(true);

        selectedMotorhomes = service.getMotorhomes();
        tViewMotorhome.setItems(FXCollections.observableArrayList(selectedMotorhomes));

        selectedExtras = service.getExtras();
        tViewExtras.setItems(FXCollections.observableArrayList(selectedExtras));

        for (int i = 0; i < cBoxPickUp.getItems().size(); i++) {
            if (cBoxPickUp.getItems().contains(service.getPickUp())) {
                cBoxPickUp.setValue(service.getPickUp());
            } else {
                checkBoxPickUp.setSelected(true);
                txtPickUpAdd.setText(service.getPickUp().getAddress());
                txtPickUpKm.setText(Integer.toString(service.getPickUp().getKm()));
                pickUp = service.getPickUp();
                pickUpPane.setDisable(false);
                btnAddPickUp.setDisable(true);
                lblPickUpAddress.setText(pickUp.getAddress() + ", " + pickUp.getKm() + " km");
                cBoxPickUp.setDisable(true);
            }
        }
        for (int i = 0; i < cBoxDropOff.getItems().size(); i++) {
            if (cBoxDropOff.getItems().contains(service.getDropOff())) {
                cBoxDropOff.setValue(service.getDropOff());
            } else {
                checkBoxDropOff.setSelected(true);
                txtDropOffAdd.setText(service.getDropOff().getAddress());
                txtDropOffKm.setText(Integer.toString(service.getDropOff().getKm()));
                dropOff = service.getDropOff();
                dropOffPane.setDisable(false);
                btnAddDropOff.setDisable(true);
                lblDropOffAddress.setText(dropOff.getAddress() + ", " + dropOff.getKm() + " km");
                cBoxDropOff.setDisable(true);
            }

        }
    }

    private void setDisabledPeriod(DatePicker datePicker){
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    public void setServiceType(String serviceType){
        this.serviceType = serviceType;
        if (serviceType.equals("Reservation"))
            startDate.setDisable(false);
    }

    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }

    public void searchCPR(ActionEvent event) {
        String cprValue = txtCpr.getText();
        if(cprValue.isEmpty()){
            alert("Error","Empty Cpr field!");
        } else if (!isValidCPR(cprValue)) {
            alert("Error", "Wrong CPR format !\nTry this format 123456-7890");
        } else{
        customer = getCustomerByCPR(cprValue);
            if (customer != null){
                txtCpr.clear();
                txtCpr.setText(customer.getCpr());
                txtName.setText(customer.getName());
                txtAddress.setText(customer.getAddress());
                txtEmail.setText(customer.getEmail());
                phoneNumber.setText(customer.getPhoneNumber());
            } else {
                txtAddress.clear();
                txtEmail.clear();
                txtName.clear();
                phoneNumber.clear();
                alert(null, "Found no match result for this cpr");
            }
        }
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

    public void addMotorhome(ActionEvent event) {
        Motorhome motorhomeSelected = cBoxMotorhome.getSelectionModel().getSelectedItem();
        boolean isInMotorhomeList = checkSelectedMotorhomes(motorhomeSelected);
        if ( isInMotorhomeList == true){
            alert(null, "Motorhome is already in list!");
        } else {
            selectedMotorhomes.add(motorhomeSelected);
            tViewMotorhome.setItems(FXCollections.observableArrayList(selectedMotorhomes));
            cBoxMotorhome.setValue(null);
        }
    }

    public void removeMotorhome(ActionEvent event) {
        selectedMotorhomes.remove(tViewMotorhome.getSelectionModel().getSelectedItem());
        tViewMotorhome.getItems().clear();
        tViewMotorhome.setItems(FXCollections.observableArrayList(selectedMotorhomes));
    }

    public void addExtra(ActionEvent event) {
        Extra extraItem = cBoxExtras.getSelectionModel().getSelectedItem();
        boolean isInList = checkSelectedExtras(extraItem);
        if ( isInList == true){
            alert(null, "Item is already in list!");
        }  else {
            int quantity = cBoxQuantity.getValue();
            int newQuantity = extraItem.getQuantity() - quantity;
            Extra requestedExtraItem = new Extra(extraItem.getId(),extraItem.getName(),quantity,extraItem.getPrice());
            selectedExtras.add(requestedExtraItem);
            tViewExtras.setItems(FXCollections.observableArrayList(selectedExtras));
            extraItem.setQuantity(newQuantity);
            cBoxExtras.setValue(null);
        }
    }

    public void removeExtra(ActionEvent event) {
        Extra requestedExtraItem = tViewExtras.getSelectionModel().getSelectedItem();
        selectedExtras.remove(requestedExtraItem);
        tViewExtras.getItems().clear();
        tViewExtras.setItems(FXCollections.observableArrayList(selectedExtras));

        for (int i = 0; i < extras.size(); i++) {
            if (requestedExtraItem.getId() == extras.get(i).getId()) {
                extras.get(i).setQuantity(extras.get(i).getQuantity() + requestedExtraItem.getQuantity());
                cBoxExtras.getItems().clear();
                cBoxExtras.getItems().addAll(extras);
            }
        }

    }

    private void refreshExtraQuantity(Extra extra){
        cBoxQuantity.getItems().clear();
        if (extra != null) {
            ArrayList<Integer> quantity = new ArrayList<>();
            for (int i = 1; i <= extra.getQuantity(); i++) {
                quantity.add(i);
            }
            cBoxQuantity.getItems().addAll(quantity);
        }
    }

    public void makeService(ActionEvent event) throws IOException {
        if (txtCpr.getText().isEmpty() || txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtEmail.getText().isEmpty() || phoneNumber.getText().isEmpty() ){
            alert("Error","Please fill in all information for customer!");
        } else if (!isValidCPR(txtCpr.getText())) {
            alert("Error", "Wrong CPR format !\nTry this format 123456-7890");
        } else if (!isValidPhoneNumber(phoneNumber.getText())){
            alert("Error", "Phone number should contains only numbers. Replace + with 00 if another country");
        } else if (!isValidEmail(txtEmail.getText())){
            alert("Error", "Invalid email format!");
        } else if (selectedMotorhomes.size() == 0) {
            alert("Error", "Please add at least 1 motorhome");
        } else if (pickUp == null || dropOff == null) {
            alert("Error", "Please choose a drop off and a pick up point");

        } else{
            Service newService = null;
            String cprInput = txtCpr.getText();
            customer = getCustomerByCPR(cprInput);
            if (customer == null) {
                customer = new Customer(0, txtName.getText(),txtCpr.getText(), txtAddress.getText(), txtEmail.getText(),phoneNumber.getText());
            }
            if (serviceType.equals("Rental"))
                newService = new Rental(0, customer, getStartDate(), getEndDate(), 0);
            else if (serviceType.equals("Reservation"))
                newService = new Reservation(0,customer,LocalDate.now().toString(),getStartDate(), getEndDate(),0);

            for (int i = 0; i < selectedMotorhomes.size(); i++){
                newService.addMotorhome(selectedMotorhomes.get(i));
            }
            for (int i = 0 ;i < selectedExtras.size(); i++){
                newService.addExtra(selectedExtras.get(i));
            }
            newService.setPickUp(pickUp);
            newService.setDropOff(dropOff);
            newService.setPrice(newService.getTotalPrice());
            service = newService;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/confirmService.fxml"));
            Parent root = loader.load();
            ConfirmServiceController controller = (ConfirmServiceController) loader.getController();
            controller.setService(service);

            Stage stage = (Stage) btnNext.getScene().getWindow();
            stage.setScene(new Scene( root));
            stage.show();
        }
    }

    private Customer getCustomerByCPR(String cprInput){
        for (int i = 0; i < customerList.size(); i++) {
            if (cprInput.equals(customerList.get(i).getCpr())) {
                return customerList.get(i);
            }
        }
        return null;
    }

    private boolean checkSelectedMotorhomes(Motorhome motorhomeSel){
        boolean isInList = false;
        for (int i = 0; i < selectedMotorhomes.size(); i++) {
            if (selectedMotorhomes.get(i).getId() == motorhomeSel.getId()) {
                isInList = true;
            }
        }
        return isInList;
    }

    private boolean checkSelectedExtras(Extra extra){
        boolean isInList = false;
        for (int i = 0; i < selectedExtras.size(); i++) {
            if (selectedExtras.get(i).getId() == extra.getId()) {
                isInList = true;
            }
        }
        return isInList;
    }

    //Could not parse local date, so I change the method
    private String getStartDate() {
        return startDate.getValue().toString();
    }

    private String getEndDate() {
        return endDate.getValue().toString();
    }

    public void btnCancelOnClick(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void checkBoxPickUpSelected(ActionEvent actionEvent) {
        if (checkBoxPickUp.isSelected()) {
            pickUpPane.setDisable(false);
            cBoxPickUp.setValue(null);
            cBoxPickUp.setDisable(true);
            pickUp = null;
        }
        else {
            pickUpPane.setDisable(true);
            pickUp = null;
            cBoxPickUp.setDisable(false);
            btnAddPickUp.setDisable(false);
            lblPickUpAddress.setText("Please type new address....");
        }
    }

    public void addAnotherPickUp(ActionEvent actionEvent) {
        if (txtPickUpAdd.getText().isEmpty() && !txtPickUpKm.getText().isEmpty()) {
            alert("Error", "Please fill in enough information for pick-up!");
        } else if(!isInt(txtPickUpKm.getText())) {
            alert("Error", "Invalid kilometer type. Kilometer should be integer!");
        } else {
            pickUp = new Location(0, txtPickUpAdd.getText(), Integer.parseInt(txtPickUpKm.getText()), true);
            lblPickUpAddress.setText(pickUp.getAddress() + ", " + pickUp.getKm() + " km");
            btnAddPickUp.setDisable(true);
        }

    }

    public void removeAnotherPickUp(ActionEvent actionEvent) {
        pickUp = null;
        lblPickUpAddress.setText("Please type new address....");
        txtPickUpAdd.clear();
        txtPickUpKm.clear();
        btnAddPickUp.setDisable(false);
    }

    public void checkBoxDropOffSelected(ActionEvent actionEvent) {
        if (checkBoxDropOff.isSelected()) {
            dropOffPane.setDisable(false);
            cBoxDropOff.setValue(null);
            cBoxDropOff.setDisable(true);
            dropOff = null;
        }
        else {
            dropOffPane.setDisable(true);
            dropOff = null;
            cBoxDropOff.setDisable(false);
            btnAddDropOff.setDisable(false);
            lblDropOffAddress.setText("Please type new address....");
        }
    }

    public void addAnotherDropOff(ActionEvent actionEvent) {
        if (txtDropOffAdd.getText().isEmpty() && !txtDropOffKm.getText().isEmpty()) {
            alert("Error", "Please fill in enough information for drop-off!");
        } else if(!isInt(txtDropOffKm.getText())) {
            alert("Error", "Invalid kilometer type. Kilometer should be integer!");
        } else {
            dropOff = new Location(0, txtDropOffAdd.getText(), Integer.parseInt(txtDropOffKm.getText()), true);
            lblDropOffAddress.setText(dropOff.getAddress() + ", " + dropOff.getKm() + " km");
            btnAddDropOff.setDisable(true);
        }
    }

    public void removeAnotherDropOff(ActionEvent actionEvent) {
        dropOff = null;
        lblDropOffAddress.setText("Please type new address....");
        txtDropOffAdd.clear();
        txtDropOffKm.clear();
        btnAddDropOff.setDisable(false);
    }

    public void confirmPeriod(ActionEvent actionEvent) {
        if (btnConfirmPeriod.getText().equals("Confirm Period")) {
            if (startDate.getValue().isBefore(endDate.getValue()) || startDate.getValue().isEqual(endDate.getValue())) {
                cBoxMotorhome.setDisable(false);
                motorhomes = adapter.getAvailableMotorhomeList(startDate.getValue(), endDate.getValue());
                cBoxMotorhome.getItems().addAll(motorhomes);
                btnConfirmPeriod.setText("Remove Period");
                startDate.setDisable(true);
                endDate.setDisable(true);
            } else
                alert("Error", "Start date should be before or equal to end date!");
        }
        else if (btnConfirmPeriod.getText().equals("Remove Period")){
            if (serviceType.equals("Reservation")) {
                startDate.setDisable(false);
            }
            endDate.setDisable(false);
            endDate.setValue(LocalDate.now());
            cBoxMotorhome.getItems().clear();
            selectedMotorhomes.clear();
            cBoxMotorhome.setDisable(true);
            btnConfirmPeriod.setText("Confirm Period");
            setServiceType(serviceType);
            tViewMotorhome.getItems().clear();
        }
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
}