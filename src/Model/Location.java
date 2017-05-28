package Model;

import java.text.DecimalFormat;

/**
 * Created by Hieu on 13-May-17.
 */
public class Location {
    private int id;
    private String address;
    private int km;
    private boolean isPrivateAddress;
    private String isPrivateString;

    public Location(int id, String address, int km, boolean isPrivateAddress) {
        this.id = id;
        this.address = address;
        this.km = km;
        this.isPrivateAddress = isPrivateAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public boolean isPrivateAddress() {
        return isPrivateAddress;
    }

    public double getPrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(km * 0.7));
    }

    public String getIsPrivateString() {
        return isPrivateAddress ? "Private Address" : "Nordic office/Recommended";
    }

    public String toString(){
        return address;
    }

}
