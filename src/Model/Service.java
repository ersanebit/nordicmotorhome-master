package Model;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Created by lamtr on 02-May-17.
 */
public abstract class Service  {
    protected int id;
    protected Customer customer;
    protected String startDate;
    protected String endDate;
    protected double price;
    protected ArrayList<Extra> extras = new ArrayList<>();
    protected ArrayList<Motorhome> motorhomes = new ArrayList<>();
    protected Location dropOff;
    protected Location pickUp;
    protected String cusPhoneNumber;

    public Service(int id, Customer customer, String startDate, String endDate, double price) {

    }

    public Service(int id, String startDate, String endDate) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Location getDropOff() {
        return dropOff;
    }

    public void setDropOff(Location dropOff) {
        this.dropOff = dropOff;
    }

    public Location getPickUp() {
        return pickUp;
    }

    public void setPickUp(Location pickUp) {
        this.pickUp = pickUp;
    }

    public ArrayList<Motorhome> getMotorhomes() {
        return motorhomes;
    }

    public void addMotorhome(Motorhome motorhome){
        this.motorhomes.add(motorhome);
    }

    public ArrayList<Extra> getExtras() {
        return extras;
    }

    public void addExtra(Extra extra){
        this.extras.add(extra);
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        totalPrice += getMotorhomesPrice();
        totalPrice += getExtrasPrice();
        totalPrice += pickUp.getPrice();
        totalPrice += dropOff.getPrice();
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(totalPrice));
    }
    public double getMotorhomesPrice(){
        double price = 0;
        for (int i = 0; i < motorhomes.size(); i++){
            price += motorhomes.get(i).getPrice() * getRentalDays();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(price));
    }

    public double getMotorhomesAdditionalPrice(){
        double price = 0;
        for (int i = 0; i < motorhomes.size(); i++){
            price += motorhomes.get(i).getTotalAdditionalPrice();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(price));
    }

    public double getExtrasPrice(){
        double price = 0;
        for (int i = 0; i < extras.size(); i++){
            price += extras.get(i).getTotalPrice();
        }

        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(price));
    }

    public long getRentalDays(){
        return ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate)) + 1;
    }

    public void setRentalDaysToMotorhome(){
        for (int i = 0; i < motorhomes.size(); i++){
            motorhomes.get(i).setRentalDays((int) getRentalDays());
        }
    }

    public double getPrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(price));
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCusPhoneNumber() { return customer.getPhoneNumber();}

    public String getDropOffAdd(){
        return dropOff.getAddress();
    }

    public String getPickUpAdd(){
        return pickUp.getAddress();
    }

    public String getCustomerName() {
        return customer.getName();
    }

    public String getCustomerCpr() {
        return customer.getCpr();
    }

    public String toString(){
        return this.getCustomerName()+" "+this.getCustomerCpr();
    }

}
