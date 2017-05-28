package Model;

/**
 * Created by lamtr on 02-May-17.
 */
public class Maintenance {
    private int id;
    private int carId;
    private String carBrand;
    private String carModel;
    private String message;
    private boolean cleaned;
    private boolean broken;
    private boolean checked;
    private String cleanedString;
    private String brokenString;
    private String checkedString;
    private double repairCost;


    public Maintenance(int id, int carId, String carBrand, String carModel, boolean isClean, boolean isBroken, String message, double repairCost) {
        this.id = id;
        this.carId = carId;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.message = message;
        this.cleaned = isClean;
        this.broken = isBroken;
        this.repairCost = repairCost;
    }

    public int getCarId() {
        return carId;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getMessage() {
        return message;
    }

    public boolean isCleaned() {
        return cleaned;
    }

    public int getId() {
        return id;
    }

    public boolean isBroken() {
        return broken;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getCleanedString() {
        return cleaned ? "yes" : "no";
    }

    public String getBrokenString() {
        return broken ? "yes" : "no";
    }

    public String getCheckedString() {
        return checked ? "yes" : "no";
    }

    public double getRepairCost() {
        return repairCost;
    }

    public String toString(){
        return this.carId+" "+this.carBrand+" "+this.carModel;
    }
}
