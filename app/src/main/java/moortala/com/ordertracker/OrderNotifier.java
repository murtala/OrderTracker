package moortala.com.ordertracker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by moortala on 3/2/2016.
 */
public class OrderNotifier extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    private ArrayList<Integer> updatedOrders;
    private ArrayList<Integer> newOrders;
    private URL url;
    private JSONArray jsonArray;
    private int inProgressSize;
    private int inProgressVal;
    private Notification notification;
    private Context context;
    private NotificationManager notificationManager;
    private Notification notification1;
    private ArrayList<Integer> inProgressOrders;
    private ArrayList<Integer> completedOrders;

    @Override
    public void onReceive(final Context contxt, Intent intent) {

        this.context = contxt;
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification = intent.getParcelableExtra(NOTIFICATION);
        final int id = intent.getIntExtra(NOTIFICATION_ID, 0);



        updatedOrders =new ArrayList<Integer>();;
        newOrders =new ArrayList<Integer>();;
        inProgressOrders =new ArrayList<Integer>();;
        completedOrders =new ArrayList<Integer>();;

        
     //   int  id = (int)System.currentTimeMillis();

       switch (id){
           case 1:
               break;
           case 2:
               break;
           case 3: //updated orders
               break;
           case 4:
               break;
       }





        ///////////////////////



        //start the notifier service
        //load data from server for orders in progress
        try {
            //  url = new URL("http://www.moortala.com/services/capstone/orders");
            url = new URL("http://moortala.com/services/capstone/orders/findNewAndUpdatedOrders");

          //  Log.d("URL", url.toString());
            //connect
            //  cm = new ConnectionManager(url);
            //  jsonArray = new JSONArray(cm.execute(url).get());
            ConnectionManager   cm1 = new ConnectionManager(new Callback() {
                @Override
                public void run(Object result) {
                    Log.d("Orders Notifiers", "-" + result.toString());

                    try {
                        Log.d("inProgressSize", ""+ inProgressSize);

                        jsonArray = new JSONArray(result.toString());
                        Log.d("jsonArray = ", jsonArray.toString());
                        //get the list of all the order ids
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = jsonArray.getJSONObject(i);
                            //insert the list to the spinner
                            //    listIds.add(jsonobj.getInt("id"));


                            //get in progress orders
                            if (jsonobj.getInt("status") ==2){
                                updatedOrders.add(jsonobj.getInt("id"));
                            }
                            //get completed orders
                            if (jsonobj.getInt("status") ==0){
                                newOrders.add(jsonobj.getInt("id"));
                            }


                        }

                        Log.d("updatedOrders", "" + updatedOrders.toString());
                        Log.d("newOrders", "" + newOrders.toString());
                        //    Log.d("listIds = ", listIds.toString());
                        inProgressSize = jsonArray.length();

                        if (inProgressSize > inProgressVal){
                            inProgressVal = inProgressSize;
                            Log.d("ProgressSize", ""+ inProgressSize);
                            Log.d("ProgressVal", ""+ inProgressVal);
                            AlarmManager alarmManager =null;
                            AlarmManager alarmManager2 =null;

                            if (updatedOrders.isEmpty()){

                            }else{
                                Log.d("PupdatedOrders ", "PupdatedOrders");
                           //     scheduleNotification(getNotification("Order(s) " + updatedOrders.toString() + " updated"), 3, alarmManager);
                         //     notification = new ChefPage().getNotification("Order(s) " + updatedOrders.toString() + " updated");
                                notification = new Notification.Builder(context)
                                        .setContentTitle("ORDER(S) UPDATES")
                                        .setContentText("Order(s) " + updatedOrders.toString() + " updated").setSmallIcon(R.mipmap.ic_launcher).build();


                                Log.d("PupdatedOrders2 ", "PupdatedOrders2");
                                //update the data
                            //    Log.d("id", "" + id);
                                notificationManager.notify(3, notification);

                            }
                            if (newOrders.isEmpty()){

                            }else{
                                Log.d("PnewOrders ", "PnewOrders");


                             //   notification = new ChefPage().getNotification("Order(s) " + newOrders.toString() + " updated");

                                //   scheduleNotification(getNotification("New order(s) " + newOrders.toString()), 4, alarmManager2);

                                notification1 = new Notification.Builder(context)
                                        .setContentTitle("ORDER(S) UPDATES")
                                        .setContentText("New order(s) " + newOrders.toString()).setSmallIcon(R.mipmap.ic_launcher).build();


                                Log.d("PnewOrders2 ", "PnewOrders2");
                                //update the data
                              //  Log.d("id", "" + id);
                                notificationManager.notify(4, notification1);
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    newOrders.clear();
                    updatedOrders.clear();


                }
            });
            cm1.execute(url);
            // cm1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //////////////////////


    }


}
