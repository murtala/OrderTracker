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

            switch (jsonObject.getInt("status")){
                case 0://new order
                    orderStatusString ="NEW";
                    break;
                case 1://view order
                    orderStatusString = " - ";
                    break;
                case 2://edit order
                    orderStatusString = " - ";
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
                    Log.d("???? " , "nothing valid selected");
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

    public static String getDeviceMacAddress(Context context) {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String macAddress = wm.getConnectionInfo().getMacAddress();
            if (macAddress == null) {
                macAddress = "No mac/wifi-disabled";
            }
        setDevice_id(macAddress);
            return macAddress;
        }

    public static long getId() {
        try {
            id = Long.parseLong(jsonObject.getString("id"));
            Log.d("id", id.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void setId(long id) {
        OrderData.id = id;
    }

    public static Long getMeal() {
        try {
            meal = Long.valueOf(jsonObject.getString("meal"));
            Log.d("meal", meal.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return meal;
    }

    public static void setMeal(Long meal) {
        OrderData.meal = meal;
    }

    public static String getName() {
        try {
            name = jsonObject.getString("name");
            Log.d("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static void setName(String name) {
        OrderData.name = name;
    }

    public static String getPassword() {
        try {
            password = jsonObject.getString("password");
            Log.d("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return password;
    }

    public static void setPassword(String password) {
        OrderData.password = password;
    }

    public static String getDevice_id() {
        try {
            device_id = jsonObject.getString("device_id");
            Log.d("device_id", device_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return device_id;
    }

    public static void setDevice_id(String device_id) {
        OrderData.device_id = device_id;
    }

    public static long getStatus() {
        try {
            status = Long.parseLong(jsonObject.getString("status"));
            Log.d("status", status.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static void setStatus(long status) {
        OrderData.status = status;
    }


    public static Long getDessert_qty() {
        try {
            dessert_qty = Long.parseLong(jsonObject.getString("dessert_qty"));
            Log.d("dessert_qty", dessert_qty.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dessert_qty;
    }

    public static void setDessert_qty(Long dessert_qty) {
        OrderData.dessert_qty = dessert_qty;
    }

    public static Long getDinner_qty() {
        try {
            dinner_qty = Long.parseLong(jsonObject.getString("dinner_qty"));
            Log.d("dinner_qty", dinner_qty.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dinner_qty;
    }

    public static void setDinner_qty(Long dinner_qty) {
        OrderData.dinner_qty = dinner_qty;
    }

    public static Long getLunch_qty() {
        try {
            lunch_qty = Long.parseLong(jsonObject.getString("lunch_qty"));
            Log.d("lunch_qty", lunch_qty.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lunch_qty;
    }

    public static void setLunch_qty(Long lunch_qty) {
        OrderData.lunch_qty = lunch_qty;
    }

    public static Long getBreakfast_qty() {
        try {
            breakfast_qty = Long.parseLong(jsonObject.getString("breakfast_qty"));
            Log.d("breakfast_qty", breakfast_qty.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return breakfast_qty;
    }

    public static void setBreakfast_qty(Long breakfast_qty) {
        OrderData.breakfast_qty = breakfast_qty;
    }

    public static Long getDessert() {
        try {
            dessert = Long.parseLong(jsonObject.getString("dessert"));
            Log.d("dessert", dessert.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dessert;
    }

    public static void setDessert(Long dessert) {
        OrderData.dessert = dessert;
    }

    public static Long getDinner() {
        try {
            dinner = Long.parseLong(jsonObject.getString("dinner"));
            Log.d("dinner", dinner.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dinner;
    }

    public static void setDinner(Long dinner) {
        OrderData.dinner = dinner;
    }

    public static Long getLunch() {
        try {
            lunch = Long.parseLong(jsonObject.getString("lunch"));
            Log.d("lunch", lunch.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lunch;
    }

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

    public static void setBreakfast(Long breakfast) {
        OrderData.breakfast = breakfast;
    }

    public static void setStatus(Long status) {
        OrderData.status = status;
    }

    public static void setTableNumber(Long tableNumber) {
        OrderData.tableNumber = tableNumber;
    }

    public static void setId(Long id) {
        OrderData.id = id;
    }

    public static Long getUser_type() {

        try {
            user_type = Long.parseLong(jsonObject.getString("user_type"));
            Log.d("user_type", user_type.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user_type;
    }

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

    public static void setStart_time(Long start_time) {
        OrderData.start_time = start_time;
    }

    public static Long getEnd_time() {
        try {
            end_time = Long.parseLong(jsonObject.getString("end_time"));
            Log.d("end_time", end_time.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return end_time;
    }

    public static void setEnd_time(Long end_time) {
        OrderData.end_time = end_time;
    }


    public String getWaiter_name() {
        try {
            waiter_name = (jsonObject.getString("waiter_name"));
            Log.d("waiter_name", waiter_name.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return waiter_name;
    }

    public void setWaiter_name(String waiter_name) {
        this.waiter_name = waiter_name;
    }

    public String getChef_name() {
        try {
            chef_name = (jsonObject.getString("chef_name"));
            Log.d("chef_name", chef_name.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chef_name;
    }

    public void setChef_name(String chef_name) {
        this.chef_name = chef_name;
    }

    public static String getUser() {
        try {
            user = (jsonObject.getString("user"));
            Log.d("user", user.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void setUser(String user) {
        OrderData.user = user;
    }
}
