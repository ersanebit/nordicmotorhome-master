package Model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Hieu on 16-May-17.
 */
public class Season {
    private static String seasons[] = {
            "Low", "Low",
            "Middle", "Middle",
            "Peak", "Peak", "Peak", "Peak",
            "Middle", "Middle",
            "Low", "Low"
    };

    public static ArrayList<Double> getPercentage(LocalDate date){
        ArrayList<Double> percentages = new ArrayList<>();
        if (seasons[date.getMonth().getValue() - 1].equals("Low")){
            percentages.add(0.0);
        } else if (seasons[date.getMonth().getValue() - 1].equals("Middle")) {
            percentages.add(0.0);
            percentages.add(0.3);
        } else if (seasons[date.getMonth().getValue() - 1].equals("Peak")) {
            percentages.add(0.0);
            percentages.add(0.3);
            percentages.add(0.6);
        }
        return percentages;
    }

    public static String getCurrentSeason(){
        LocalDate date = LocalDate.now();
        return seasons[date.getMonth().getValue() - 1];
    }
}
