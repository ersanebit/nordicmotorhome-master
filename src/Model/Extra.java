package Model;

import java.text.DecimalFormat;

/**
 * Created by lamtr on 02-May-17.
 */
public class Extra {
    private int id;
    private String name;
    private int quantity;
    private double price;

    public Extra(int id,String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getId(){
        return this.id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(price * quantity));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString(){
        return name;
    }
}
