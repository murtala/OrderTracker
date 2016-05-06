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
    private Notification notification3;
    private Notification notification4;
    private Notification notification2;
    private ArrayList<Integer> canceledOrders;
    private Notification notification5;
    private Notification notification6;

    @Override
    public void onReceive(final Context contxt, Intent intent) {

        this.context = contxt;
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification = intent.getParcelableExtra(NOTIFICATION);
        final int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        updatedOrders =new ArrayList<Integer>();;
        newOrders =new ArrayList<Integer>();;
        inProgressOrders =new ArrayList<Integer>();
        completedOrders =new ArrayList<Integer>();
        canceledOrders =new ArrayList<Integer>();
        //start the notifier service
        //load data from server for orders in progress
        try {
            switch (id){
                case 1://find new orders
                    url = new URL("http://moortala.com/services/capstone/orders/getNewOrders");
                    break;
                case 2://view
                    url = new URL("http://moortala.com/services/capstone/orders");
                    break;
                case 3: //updated orders
                    url = new URL("http://moortala.com/services/capstone/orders/findByStatus/2");
                    break;
                case 4://canceled orders
                    url = new URL("http://moortala.com/services/capstone/orders/findByStatus/3");
                    break;
                case 5://completed orders
                    url = new URL("http://moortala.com/services/capstone/orders/findByStatus/4");
                    break;
                case 6://completed orders
                    url = new URL("http://moortala.com/services/capstone/orders/findByStatus/5");
                    break;
                case 7://for chef: find new, updated and canceled orders
                    url = new URL("http://moortala.com/services/capstone/orders/findNewAndUpdatedOrders");
                    break;
                case 8://find current, updated and completed orders (mainly for waiter)
                    url = new URL("http://moortala.com/services/capstone/orders/findCurrentAndCompletedOrders");
                    break;
            }

            //connect
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

                            //get new orders
                            if (jsonobj.getInt("status") ==0){
                                newOrders.add(jsonobj.getInt("id"));
                                Log.d("newOrders", "" + newOrders.toString());
                            }
                            //get updated orders
                            if (jsonobj.getInt("status") ==2){
                                updatedOrders.add(jsonobj.getInt("id"));
                                Log.d("updatedOrders", "" + updatedOrders.toString());
                            }
                            //get canceled orders
                            if (jsonobj.getInt("status") ==3){
                                canceledOrders.add(jsonobj.getInt("id"));
                                Log.d("Canceled", "" + canceledOrders.toString());
                            }
                            //get completed orders
                            if (jsonobj.getInt("status") ==4){
                                completedOrders.add(jsonobj.getInt("id"));
                                Log.d("completedOrders", "" + completedOrders.toString());
                            }
                            //get in progress orders
                            if (jsonobj.getInt("status") ==5){
                                inProgressOrders.add(jsonobj.getInt("id"));
                                Log.d("inProgressOrders", ""+ inProgressOrders.toString());
                            }
                        }

                        inProgressSize = jsonArray.length();

                        if (inProgressSize > inProgressVal){
                            inProgressVal = inProgressSize;
                            Log.d("ProgressSize", ""+ inProgressSize);
                            Log.d("ProgressVal", ""+ inProgressVal);
                            AlarmManager alarmManager =null;
                            AlarmManager alarmManager2 =null;

                            if (newOrders.isEmpty()){

                            }else{
                                notification1 = new Notification.Builder(context)
                                        .setContentTitle("ORDER(S)")
                                        .setContentText("New:" + newOrders.toString()).setSmallIcon(R.mipmap.ic_launcher).build();
                                notificationManager.notify(4, notification1);
                            }


                            if (updatedOrders.isEmpty()){

                            }else{
                                notification3 = new Notification.Builder(context)
                                        .setContentTitle("ORDER(S)")
                                        .setContentText("Updated:" + updatedOrders.toString()).setSmallIcon(R.mipmap.ic_launcher).build();
                                notificationManager.notify(3, notification3);

                            }

                            if (canceledOrders.isEmpty()){

                            }else{
                                notification4 = new Notification.Builder(context)
                                        .setContentTitle("ORDER(S)")
                                        .setContentText("Canceled:" + canceledOrders.toString()).setSmallIcon(R.mipmap.ic_launcher).build();
                                notificationManager.notify(3, notification4);

                            }

                            if (completedOrders.isEmpty()){

                            }else{
                                notification5 = new Notification.Builder(context)
                                        .setContentTitle("ORDER(S)")
                                        .setContentText("DONE:" + completedOrders.toString()).setSmallIcon(R.mipmap.ic_launcher).build();
                                notificationManager.notify(2, notification5);
                            }

                            if (inProgressOrders.isEmpty()){

                            }else{
                                notification6 = new Notification.Builder(context)
                                        .setContentTitle("ORDER(S)")
                                        .setContentText("In Progress:" + inProgressOrders.toString()).setSmallIcon(R.mipmap.ic_launcher).build();
                                notificationManager.notify(1, notification6);
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    newOrders.clear();
                    updatedOrders.clear();
                    inProgressOrders.clear();
                    completedOrders.clear();
                }
            });
            cm1.execute(url);
            // cm1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
