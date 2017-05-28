package Model;

import java.time.LocalDate;

/**
 * Created by Hieu on 17-May-17.
 */
public class OccupiedPeriod {
    private String startDate;
    private String endDate;

    public OccupiedPeriod(String startDate, String endDate){
        this.startDate =startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        LocalDate sDate = LocalDate.parse(startDate);
        return sDate;
    }
    public LocalDate getEndDate() {
        LocalDate eDate = LocalDate.parse(endDate);
        return eDate;
    }
}
