package moortala.com.ordertracker;

/**
 * Created by moortala on 2/2/2016.
 */
public class OrderData {
    private static OrderData waiterInputs;
    private static String orderStatus;
    private static long tableNumber;
    private static String notes;

    public static void setOrderStatus(String orderStatus) {
        OrderData.orderStatus = orderStatus;
    }

    public static void setTableNumber(long tableNumber) {
        OrderData.tableNumber = tableNumber;
    }

    public static void setNotes(String notes) {
        OrderData.notes = notes;
    }

    public static OrderData getWaiterInputs() {
        return waiterInputs;
    }

    public static String getOrderStatus() {
        return orderStatus;
    }

    public static long getTableNumber() {
        return tableNumber;
    }

    public static String getNotes() {
        return notes;
    }
}
