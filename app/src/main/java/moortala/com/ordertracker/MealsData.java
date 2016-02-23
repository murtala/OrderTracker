package moortala.com.ordertracker;

/**
 * Created by moortala on 2/9/2016.
 */
public class MealsData {

    private static String meal;

    public MealsData(int mealNumber) {

    }

    public MealsData() {

    }

    public static String getMeal(int mealNumber) {

        switch (mealNumber){
            case 1:  meal = "Salad";
                break;
            case 2:  meal = "Meat";
                break;
            case 3:  meal = "Fruits";
                break;
            case 4:  meal = "Other";
                break;
            case 5:  meal = "Special";

            default: meal = "Invalid";
                break;
        }
        return meal;
    }
}
