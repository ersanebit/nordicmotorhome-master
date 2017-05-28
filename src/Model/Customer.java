package Model;

/**
 * Created by Quanticus on 5/12/2017.
 */
public class Customer {
    private int id;
    private String name;
    private String cpr;
    private String address;
    private String email;
    private String phoneNumber;

    public Customer(int id, String name, String cpr, String address, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.cpr = cpr;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString(){
        return this.id+" "+this.name +" "+this.cpr;
    }


}
