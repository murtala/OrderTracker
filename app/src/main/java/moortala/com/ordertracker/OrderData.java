package moortala.com.ordertracker;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

/**
 * Created by moortala on 2/2/2016.
 */
public class OrderData {
    private static OrderData waiterInputs;
    private static String orderStatus;
    private static Long id;
    private static Long tableNumber;
    private static Long status;
    private static String notes;
    private static Long meal;
    private static Long breakfast;
    private static Long lunch;
    private static Long dinner;
    private static Long dessert;
    private static Long breakfast_qty;
    private static Long lunch_qty;
    private static Long dinner_qty;
    private static Long dessert_qty;
    private static String name;
    private static Long start_time;
    private static Long end_time;
    private static Long user_type;
    private static String password;
    private static String user;
    private static String device_id;
    private static JSONObject jsonObject;
    public static OrderData getOrder;
    private static String orderStatusString;
    public String waiter_name;
    public String chef_name;
    //private final  BufferedReader reader;

    public OrderData(ConnectionManager cm) {
        //reader = cm.getJSONData();
        jsonObject = cm.getJsonObject();
    }

    public OrderData(JSONObject jsonObj) {
        this.jsonObject = jsonObj;
    }


    public static void setOrder(OrderData getOrder) {
        OrderData.getOrder = getOrder;
    }

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

    public static OrderData getOrder() {

        return getOrder;
    }

    public static String getOrderStatus() {
        try {
            orderStatus = jsonObject.getString("status");
            Log.d("status", orderStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderStatus;
    }

    public static String getOrderStatusStrings() {

        orderStatusString = null;
        try {
            jsonObject.getInt("status");

            switch (jsonObject.getInt("status")) {
                case 0://new order
                    orderStatusString = "NEW";
                    break;
                case 1://view order
                    orderStatusString = "VIEW ONLY";
                    break;
                case 2://edit order
                    orderStatusString = "UPDATED";
                    break;
                case 3://cancel order
                    orderStatusString = "CANCELED";
                    break;
                case 4://complete order
                    orderStatusString = "COMPLETED";
                    break;
                case 5://complete order
                    orderStatusString = "IN PROGRESS";
                    break;
                default:
                    orderStatusString = "INVALID";
                    Log.d("???? ", "nothing valid selected");
                    break;
            }
            Log.d("status", orderStatusString);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderStatusString;
    }

    public static long getTableNumber() {
        try {
            tableNumber = Long.parseLong(jsonObject.getString("table_number"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tableNumber;
    }

    public static String getNotes() {
        try {
            notes = jsonObject.getString("notes");
            Log.d("notes", notes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notes;
    }

    //get a device's mac address
    public static String getDeviceMacAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wm.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Mac_Address_not_found";
        }
        setDevice_id(macAddress);
        return macAddress;
    }

    //get the id
    public static long getId() {
        try {
            id = Long.parseLong(jsonObject.getString("id"));
            Log.d("id", id.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    //set the id
    public static void setId(long id) {
        OrderData.id = id;
    }

    //get the meal
    public static Long getMeal() {
        try {
            meal = Long.valueOf(jsonObject.getString("meal"));
            Log.d("meal", meal.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return meal;
    }

    //set meal
    public static void setMeal(Long meal) {
        OrderData.meal = meal;
    }

    //get the name
    public static String getName() {
        try {
            name = jsonObject.getString("name");
            Log.d("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    //set name
    public static void setName(String name) {
        OrderData.name = name;
    }

    //get password
    public static String getPassword() {
        try {
            password = jsonObject.getString("password");
            Log.d("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return password;
    }

    //set password
    public static void setPassword(String password) {
        OrderData.password = password;
    }

    //get device id
    public static String getDevice_id() {
        try {
            device_id = jsonObject.getString("device_id");
            Log.d("device_id", device_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return device_id;
    }

    //set device id
    public static void setDevice_id(String device_id) {
        OrderData.device_id = device_id;
    }

    //get the status
    public static long getStatus() {
        try {
            status = Long.parseLong(jsonObject.getString("status"));
            Log.d("status", status.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    //set the status
    public static void setStatus(long status) {
        OrderData.status = status;
    }

    //get dessert quantity
    public static Long getDessert_qty() {
        try {
            dessert_qty = Long.parseLong(jsonObject.getString("dessert_qty"));
            Log.d("dessert_qty", dessert_qty.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dessert_qty;
    }

    //set dessert quqntity
    public static void setDessert_qty(Long dessert_qty) {
        OrderData.dessert_qty = dessert_qty;
    }

    //get dinner qunatity
    public static Long getDinner_qty() {
        try {
            dinner_qty = Long.parseLong(jsonObject.getString("dinner_qty"));
            Log.d("dinner_qty", dinner_qty.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dinner_qty;
    }

    //set dinner quantity
    public static void setDinner_qty(Long dinner_qty) {
        OrderData.dinner_qty = dinner_qty;
    }

    //get lunch quantity
    public static Long getLunch_qty() {
        try {
            lunch_qty = Long.parseLong(jsonObject.getString("lunch_qty"));
            Log.d("lunch_qty", lunch_qty.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lunch_qty;
    }

    //set lunch quantity
    public static void setLunch_qty(Long lunch_qty) {
        OrderData.lunch_qty = lunch_qty;
    }

    //get breakfast quantity
    public static Long getBreakfast_qty() {
        try {
            breakfast_qty = Long.parseLong(jsonObject.getString("breakfast_qty"));
            Log.d("breakfast_qty", breakfast_qty.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return breakfast_qty;
    }

    //set breakfast quantity
    public static void setBreakfast_qty(Long breakfast_qty) {
        OrderData.breakfast_qty = breakfast_qty;
    }

    //get dessert
    public static Long getDessert() {
        try {
            dessert = Long.parseLong(jsonObject.getString("dessert"));
            Log.d("dessert", dessert.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dessert;
    }

    //set dessert
    public static void setDessert(Long dessert) {
        OrderData.dessert = dessert;
    }

    //get dinner
    public static Long getDinner() {
        try {
            dinner = Long.parseLong(jsonObject.getString("dinner"));
            Log.d("dinner", dinner.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dinner;
    }

    //set dinner
    public static void setDinner(Long dinner) {
        OrderData.dinner = dinner;
    }

    //get lunch
    public static Long getLunch() {
        try {
            lunch = Long.parseLong(jsonObject.getString("lunch"));
            Log.d("lunch", lunch.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lunch;
    }

    //set lunch
    public static void setLunch(Long lunch) {
        OrderData.lunch = lunch;
    }

    public static Long getBreakfast() {
        try {
            breakfast = Long.parseLong(jsonObject.getString("breakfast"));
            Log.d("breakfast", breakfast.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return breakfast;
    }

    //set breakfast
    public static void setBreakfast(Long breakfast) {
        OrderData.breakfast = breakfast;
    }

    //set status
    public static void setStatus(Long status) {
        OrderData.status = status;
    }

    //set table number
    public static void setTableNumber(Long tableNumber) {
        OrderData.tableNumber = tableNumber;
    }

    //set id
    public static void setId(Long id) {
        OrderData.id = id;
    }

    //get user type
    public static Long getUser_type() {

        try {
            user_type = Long.parseLong(jsonObject.getString("user_type"));
            Log.d("user_type", user_type.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user_type;
    }

    //set ser type
    public static void setUser_type(Long user_type) {
        OrderData.user_type = user_type;
    }

    public static Long getStart_time() {
        try {
            start_time = Long.parseLong(jsonObject.getString("start_time"));
            Log.d("start_time", start_time.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return start_time;
    }

    //set start time
    public static void setStart_time(Long start_time) {
        OrderData.start_time = start_time;
    }

    //get end time
    public static Long getEnd_time() {
        try {
            end_time = Long.parseLong(jsonObject.getString("end_time"));
            Log.d("end_time", end_time.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return end_time;
    }

    //set end time
    public static void setEnd_time(Long end_time) {
        OrderData.end_time = end_time;
    }

    //get waiter name
    public String getWaiter_name() {
        try {
            waiter_name = (jsonObject.getString("waiter_name"));
            Log.d("waiter_name", waiter_name.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return waiter_name;
    }

    //set waiter name
    public void setWaiter_name(String waiter_name) {
        this.waiter_name = waiter_name;
    }

    //get chef name
    public String getChef_name() {
        try {
            chef_name = (jsonObject.getString("chef_name"));
            Log.d("chef_name", chef_name.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chef_name;
    }

    //set chef name
    public void setChef_name(String chef_name) {
        this.chef_name = chef_name;
    }

    //get user
    public static String getUser() {
        try {
            user = (jsonObject.getString("user"));
            Log.d("user", user.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    //set user
    public static void setUser(String user) {
        OrderData.user = user;
    }
}
