package Adapter;

import Model.Customer;
import Model.Extra;
import Model.Motorhome;
import Model.Rental;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Hieu on 23-May-17.
 */
public class FileWrapper {
    private Rental rental;

    public void createContract(Rental rental) throws FileNotFoundException{
        this.rental = rental;
        String fileName = rental.getCustomerName() + "_" + LocalDate.now() + "_" + LocalDateTime.now().hashCode() + ".txt";
        Scanner input = new Scanner(new File("contracts/template/contract_template.txt"));
        PrintStream output = new PrintStream(new File("contracts/" + fileName));
        while(input.hasNextLine()){
            String line = input.nextLine();
            String processedLine = processLine(line);
            output.println(processedLine);
        }
        output.close();
    }
    private String processLine(String line){
        Scanner word = new Scanner(line);
        line = "";
        while (word.hasNext()) {
            String info = word.next();
            if (info.equals("[CUS_NAME]")) {
                info = rental.getCustomerName();
            } else if (info.equals("[CAR_INFO]")) {
                info =  processCarInfo(rental.getMotorhomes());
            } else if (info.equals("[CUS_INFO]")){
                info = processCusInfo(rental.getCustomer());
            } else if (info.equals("[RENTAL_PERIOD]")) {
                info = processRentalPeriod(rental);
            } else if (info.equals("[TOTAL_FEE]")) {
                info = Double.toString(rental.getTotalPrice());
            } else if (info.equals("[CAR_FEE]")){
                info = processCarPrice(rental.getMotorhomes());
            } else if (info.equals("[EXTRA_FEE]")){
                info = processExtraPrice(rental.getExtras());
            }
            line += info + " ";
        }
        return line;
    }

    private String processCarInfo(ArrayList<Motorhome> motorhomes){
        String info = "";
        for (int i = 0; i < motorhomes.size(); i++){
            info += "Brand: " + motorhomes.get(i).getBrand() + " \r\n";
            info += "Model: " + motorhomes.get(i).getModel() + " \r\n";
            info += "Capacity: " + motorhomes.get(i).getCapacity() + " \r\n\r\n";
        }
        return info;
    }

    private String processRentalPeriod(Rental rental){
        String info = "";
        info += "Start Date: " + rental.getStartDate() + " \r\n";
        info += "End Date: " + rental.getEndDate() + " \r\n";
        info += "Total Days: " + rental.getRentalDays() + " days \r\n";
        return info;
    }

    private String processCarPrice(ArrayList<Motorhome> motorhomes){
        String info = "";
        for (int i = 0; i < motorhomes.size(); i++){
            info += motorhomes.get(i).toString() + ": " + motorhomes.get(i).getPrice() + " € per day \r\n";
        }
        return info;
    }

    private String processExtraPrice(ArrayList<Extra> extras){
        String info = "";
        for (int i = 0; i < extras.size(); i++){
            info += extras.get(i).getName() + ", number: " + extras.get(i).getQuantity() + " , " +
                    extras.get(i).getPrice() + " € per each" + ", " + extras.get(i).getTotalPrice()+ " € in total \r\n";
        }
        return info;
    }

    private String processCusInfo(Customer customer){
        String info = "";
        info += "Customer name: " + customer.getName() + " \r\n";
        info += "Customer cpr: " + customer.getCpr() + " \r\n";
        info += "Customer phone: " + customer.getPhoneNumber() + " \r\n";
        info += "Customer address: " + customer.getAddress() + " \r\n";
        info += "Customer email: " + customer.getEmail() + " \r\n";
        return info;
    }
}
