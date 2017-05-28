package Model;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by Quanticus on 5/12/2017.
 */
public class Reservation extends Service{
    private String reservationDate;
    private int dayToRental;

    public Reservation(int id, Customer customer,String reservationDate, String startDate, String endDate, double price) {
        super(id,customer,startDate,endDate,price);
        this.id = id;
        this.customer = customer;
        this.reservationDate = reservationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;

    }
    public Reservation(int id,String reservationDate, String startDate, String endDate) {
        super(id,startDate,endDate);
        this.id = id;
        this.reservationDate = reservationDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public long getDayToRental(){
        return ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(startDate));
    }

    public double getPercentage(){
        if (getDayToRental() >= 50)
            return 0.2;
        else if (getDayToRental() >= 15)
            return 0.5;
        else if (getDayToRental() > 0)
            return 0.8;
        else if (getDayToRental() == 0)
            return 0.95;
        return 0;
    }

    public double getCancellationPrice(){
        double price = getTotalPrice() * getPercentage();
        if (getPercentage() == 0.2 && price < 200)
            return 200;
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(price));
    }

    public Rental toRental(){
        Rental rental = new Rental(0, this.customer, this.startDate,this.endDate, this.price);
        rental.setDropOff(this.getDropOff());
        rental.setPickUp(this.getPickUp());
        for (int i = 0; i < this.getMotorhomes().size(); i++)
            rental.getMotorhomes().add(this.getMotorhomes().get(i));
        for (int i = 0; i < this.getExtras().size(); i++)
            rental.getExtras().add(this.getExtras().get(i));
        return rental;
    }
}

