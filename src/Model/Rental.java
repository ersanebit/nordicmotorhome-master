package Model;

/**
 * Created by Quanticus on 5/12/2017.
 */
public class Rental extends Service{
    public Rental(int id, Customer customer, String startDate, String endDate, double price) {
        super(id,customer,startDate,endDate,price);
        this.id = id;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;

    }
    public Rental(int id, String startDate, String endDate) {
        super(id,startDate,endDate);
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
