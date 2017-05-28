package Model;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by lamtr on 02-May-17.
 */
public class Motorhome {
    private int id;
    private String brand;
    private String model;
    private boolean cleaned = true;
    private boolean broken = false;
    private boolean reserved = false;
    private boolean rented = false;
    private int capacity;
    private double price;
    private int kmDriven;
    private String note = "";
    private String tankStatus = "";
    private int newKmDriven = 0;
    private double maintenancePrice = 0;
    private int rentalDays;
    private ArrayList<Rental> rentals = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();

    public Motorhome(int id, String brand, String model, int capacity, double price, int kmDriven, boolean cleaned, boolean broken, boolean reserved, boolean rented){
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.capacity = capacity;
        this.price = price;
        this.kmDriven = kmDriven;
        this.cleaned = cleaned;
        this.broken = broken;
        this.reserved = reserved;
        this.rented = rented;
    }

    public Motorhome(String brand, String model, int capacity, double price, int kmDriven) {
        this.brand = brand;
        this.model = model;
        this.capacity = capacity;
        this.price = price;
        this.kmDriven = kmDriven;
    }

    public Motorhome(int id, String brand, String model, int capacity, double price, int kmDriven) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.capacity = capacity;
        this.price = price;
        this.kmDriven = kmDriven;
    }

    public Motorhome() {
        this.id = 0;
        this.brand = null;
        this.model = null;
        this.cleaned = true;
        this.broken = false;
        this.reserved = false;
        this.rented = false;
        this.capacity = 0;
        this.price = 0;
        this.kmDriven = 0;
        this.note = "";
        this.tankStatus = "";
        this.newKmDriven = 0;
        this.maintenancePrice = 0;
        this.rentalDays = 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getCleanString() {
        return cleaned ? "yes" : "no";
    }

    public String getBrokenString() {
        return broken ? "yes" : "no";
    }

    public String getReservedString() {
        return reserved ? "yes" : "no";
    }

    public String getRentedString() {
        return rented ? "yes" : "no";
    }

    public boolean isCleaned() {
        return cleaned;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken){this.broken = broken;}

    public boolean isReserved() {
        return reserved;
    }

    public boolean isRented() {
        return rented;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPrice() {
        double price = this.price;
        ArrayList<Double> percentages = Season.getPercentage(LocalDate.now());
        for (int i = 0; i < percentages.size(); i++){
            price += price * percentages.get(i);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(price));
    }

    public double getKmPrice(){
        return getKmExcess() * 1;
    }

    public int getKmExcess(){
        return (getKmPerday() <= 400) ? 0 : (getKmPerday() - 400);
    }

    public int getKmPerday(){
        return (newKmDriven - kmDriven) / rentalDays;
    }

    public double getFuelPrice(){
        return (tankStatus.equals("Less than half") || tankStatus.equals("Almost empty")) ? 70.0 : 0.0;
    }

    public double getBasePrice(){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(this.price));
    }

    public double getTotalAdditionalPrice() {
        return getFuelPrice() + getKmPrice() + getMaintenancePrice();
    }

    public int getKmDriven() {
        return kmDriven;
    }

    public ArrayList<OccupiedPeriod> getOccupiedPeriods() {
        ArrayList<OccupiedPeriod> occupiedPeriods = new ArrayList<>();
        for (Rental rental : rentals)
            occupiedPeriods.add(new OccupiedPeriod(rental.getStartDate(), rental.getEndDate()));
        for (Reservation reservation : reservations)
            occupiedPeriods.add(new OccupiedPeriod(reservation.getStartDate(), reservation.getEndDate()));
        return occupiedPeriods;
    }

    public ArrayList<Rental> getRentals() {
        return rentals;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTankStatus() {
        return tankStatus;
    }

    public void setTankStatus(String tankStatus) {
        this.tankStatus = tankStatus;
    }

    public int getNewKmDriven() {
        return newKmDriven;
    }

    public void setNewKmDriven(int newKmDriven) {
        this.newKmDriven = newKmDriven;
    }

    public double getMaintenancePrice() {
        return maintenancePrice;
    }

    public void setMaintenancePrice(double maintenancePrice) {
        this.maintenancePrice = maintenancePrice;
    }

    public void setRentalDays(int days){
        this.rentalDays = days;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setKmDriven(int kmDriven) {
        this.kmDriven = kmDriven;
    }

    public void addRentals(Rental rental){
        this.rentals.add(rental);
    }

    public void addReservations(Reservation reservation){
        this.reservations.add(reservation);
    }

    public String toString(){
        return brand + ", " + model + ", " + capacity + " people";
    }
}


