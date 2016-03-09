package moortala.com.ordertracker;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaiterPage extends AppCompatActivity {
    private int orderStatus;
    private int dishNumber;
    private int tableNumber;
    private EditText tableNumberTextField;
    private String notes;
    private static URL url;
    private String mealNumber;
    private int orderNumber;
    private int transactionNumber;
    private String userName;
    private String deviceId;
    private TextView userNameTextView;
    private EditText statusDetailEditText;
    private EditText statusDetail;
    private JSONObject jsonObject;
    private ConnectionManager cm;
    private RadioButton newOrderRadioButton;
    private EditText notesEditText;
    private  Spinner orderDetailsSpinner;
    private JSONArray jsonArray;
    private  List<Integer> listIds;
    private ArrayAdapter<Integer> spinnerArrayAdapter;
    private  ArrayAdapter<Integer> dataAdapter;
    private Integer orderId;
    private Spinner orderActionsSpinner;
    private Spinner breakfastSpinner;
    private EditText breakfastQtyEditText;
    private Spinner lunchSpinner;
    private EditText lunchQtyEditText;
    private Spinner dinnerSpinner;
    private EditText dinnerQtyEditText;
    private Spinner dessertSpinner;
    private EditText dessertQtyEditText;
    private Button submitButton;
    private Button clearButton;
    private String[] statuses;
    private String[] breakfasts;
    private String[] lunches;
    private String[] dinners;
    private String[] desserts;
    private Integer dessert;
    private Integer dinner;
    private Integer lunch;
    private Integer breakfast;
    private int breakfastQty;
    private int lunchQty;
    private int dinnerQty;
    private int dessertQty;
    private Messages msg;
    private TextView orderStatusTextView;
    private TextView timeTakenTextView;
    private OrderData od;
    private long userID;
    private int k;
    private int inProgressSize;
    private int inProgressVal;
    private List <Integer> inProgressOrders, completedOrders;


    @Override
    protected void onResume() {
        super.onResume();

        inProgressOrders =new ArrayList<Integer>();;
        completedOrders =new ArrayList<Integer>();;

        //start the notifier service
        //load data from server for orders in progress
        try {
            //  url = new URL("http://www.moortala.com/services/capstone/orders");
            url = new URL("http://moortala.com/services/capstone/orders/findCurrentAndCompletedOrders");

            Log.d("URL", url.toString());
            //connect
            //  cm = new ConnectionManager(url);
            //  jsonArray = new JSONArray(cm.execute(url).get());
            ConnectionManager   cm1 = new ConnectionManager(WaiterPage.this, new Callback() {
                @Override
                public void run(Object result) {
                    Log.d("Orders in progress", "done" + result.toString());

                    try {
                        Log.d("mememme2222", ""+ inProgressSize);

                        jsonArray = new JSONArray(result.toString());
                        Log.d("jsonArray = ", jsonArray.toString());
                        //get the list of all the order ids
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = jsonArray.getJSONObject(i);
                            //insert the list to the spinner
                        //    listIds.add(jsonobj.getInt("id"));


                            //get in progress orders
                            if (jsonobj.getInt("status") ==5){
                                inProgressOrders.add(jsonobj.getInt("id"));
                            }
                            //get completed orders
                            if (jsonobj.getInt("status") ==4){
                                completedOrders.add(jsonobj.getInt("id"));
                            }


                        }

                        Log.d("inProgressOrders", ""+ inProgressOrders.toString());
                        Log.d("completedOrders", ""+ completedOrders.toString());
                    //    Log.d("listIds = ", listIds.toString());
                        inProgressSize = jsonArray.length();

                        if (inProgressSize > inProgressVal){
                            inProgressVal = inProgressSize;
                            Log.d("ProgressSize", ""+ inProgressSize);
                            Log.d("ProgressVal", ""+ inProgressVal);
                            AlarmManager alarmManager =null;
                            AlarmManager alarmManager2 =null;
                            scheduleNotification(getNotification("Order(s) " + inProgressOrders.toString() +" started"),1,  alarmManager);
                            scheduleNotification(getNotification("Order(s) "+completedOrders.toString() +" completed"),2,alarmManager2 );
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            cm1.execute(url);
           // cm1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

        } catch (Exception e) {
            e.printStackTrace();
        }




        //get the user name from the login
        try {
            userNameTextView.setText(getIntent().getStringExtra("user"));
            userName = getIntent().getStringExtra("user");
            userID = getIntent().getLongExtra("user_id", 0);
        } catch (Exception e) {
         //   userNameTextView.setText("anonymous waiter");
           // e.printStackTrace();
        }

        //initiate resourcse
        orderStatusTextView = (TextView) findViewById(R.id.orderStatusTextView);
        listIds = new ArrayList<Integer>();
        timeTakenTextView =(TextView) findViewById(R.id.timeTakenTextView);


        //populate order status spinner

        ArrayAdapter<String> statusesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, statuses);
        //statusesAdapter.insert();

        orderActionsSpinner.setAdapter(statusesAdapter);
        statusesAdapter.notifyDataSetChanged();

      /*  orderActionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        //populate breakfast spinner
        ArrayAdapter<String> breakfastsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, breakfasts);
        breakfastSpinner.setAdapter(breakfastsAdapter);
        breakfastsAdapter.notifyDataSetChanged();

        breakfastSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("breakfast selected = ", (String) parent.getItemAtPosition(position));
                breakfast = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //populate lunch spinner
        ArrayAdapter<String> lunchesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, lunches);
        lunchSpinner.setAdapter(lunchesAdapter);
        lunchesAdapter.notifyDataSetChanged();

        lunchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("lunch selected = ", (String) parent.getItemAtPosition(position));
                lunch = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //populate dinner spinner
        ArrayAdapter<String> dinnersAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, dinners);
        dinnerSpinner.setAdapter(dinnersAdapter);
        dinnersAdapter.notifyDataSetChanged();

        dinnerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("dinner selected = ", (String) parent.getItemAtPosition(position));
                dinner = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //populate dessert spinner
        ArrayAdapter<String> dessertsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, desserts);
        dessertSpinner.setAdapter(dessertsAdapter);
        dessertsAdapter.notifyDataSetChanged();

        dessertSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("dessert selected = ", (String) parent.getItemAtPosition(position));
                dessert = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//TODO
        //listen to the status spinner for changes
        orderActionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                //start the notification service


            //    OrderNotifier on = new OrderNotifier();
             //   on.startNotifier();
             //   System.out.println(" status selected = " + parent.getItemAtPosition(position));
                orderId = position;
                Log.d("status selected = ", (String) parent.getItemAtPosition(position));
                orderStatus = orderId;

                if (orderId == 1 || orderId == 2 || orderId == 3){
                    submitButton.setEnabled(false);
                }else{
                    submitButton.setEnabled(true);
                }

                if (position == 0) { //new order radio button
                    //clear the list of order details numbers
                    listIds.clear();
                    //clear the status display message
                    orderStatusTextView.setText("");
                    //clear time
                    timeTakenTextView.setText("");

                    //set order status
                    orderStatus = 0;
                    //disable spinner
                    orderDetailsSpinner.setEnabled(false);
                    orderDetailsSpinner.setVisibility(View.INVISIBLE);
                    //enable other fields
                    tableNumberTextField.setEnabled(true);
                    // enable meal selections
                    breakfastSpinner.setEnabled(true);
                    lunchSpinner.setEnabled(true);
                    dinnerSpinner.setEnabled(true);
                    dessertSpinner.setEnabled(true);
                    //enable meal quantities
                    breakfastQtyEditText.setEnabled(true);
                    lunchQtyEditText.setEnabled(true);
                    dinnerQtyEditText.setEnabled(true);
                    dessertQtyEditText.setEnabled(true);
                    //enable notes
                    notesEditText.setEnabled(true);
                    //clear fields
                    resetFields();
                } else if (position == 1) { //view new orders
//clear the list of order details numbers
                    listIds.clear();

                    //clear the status display message
                    orderStatusTextView.setText("");
                    //clear time
                    timeTakenTextView.setText("");

                                        //set order status
                    orderStatus = 1;
                    //disable spinner
                    orderDetailsSpinner.setEnabled(true);
                    orderDetailsSpinner.setVisibility(View.VISIBLE);
                    //enable other fields
                    tableNumberTextField.setEnabled(false);
                    // enable meal selections
                    breakfastSpinner.setEnabled(false);
                    lunchSpinner.setEnabled(false);
                    dinnerSpinner.setEnabled(false);
                    dessertSpinner.setEnabled(false);
                    //enable meal quantities
                    breakfastQtyEditText.setEnabled(false);
                    lunchQtyEditText.setEnabled(false);
                    dinnerQtyEditText.setEnabled(false);
                    dessertQtyEditText.setEnabled(false);
                    //enable notes
                    notesEditText.setEnabled(false);

                    //load data from server
                    try {
                      //  url = new URL("http://www.moortala.com/services/capstone/orders");
                        url = new URL("http://www.moortala.com/services/capstone/orders/findByStatus/0");

                        Log.d("URL", url.toString());
                        //connect
                      //  cm = new ConnectionManager(url);
                      //  jsonArray = new JSONArray(cm.execute(url).get());
                        cm = new ConnectionManager(WaiterPage.this, new Callback() {
                            @Override
                            public void run(Object result) {
                                Log.d("callback", "done" + result.toString());

                                try {
                                    jsonArray = new JSONArray(result.toString());

                                    Log.d("jsonArray = ", jsonArray.toString());
                                    //get the list of all the order ids

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                                        //insert the list to the spinner
                                        listIds.add(jsonobj.getInt("id"));
                                    }
                                    Log.d("listIds = ", listIds.toString());


                                    ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, listIds);
                                    //   dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                                    orderDetailsSpinner.setAdapter(adp);
                                    orderDetailsSpinner.setSelection(orderId);
                                    adp.notifyDataSetChanged();
                                    orderDetailsSpinner.setEnabled(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        cm.execute(url);


                        /////////////

                   /*     jsonArray = new JSONArray(cm.execute(url).get());

                        Log.d("jsonArray = ", jsonArray.toString());
                        //get the list of all the order ids

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = jsonArray.getJSONObject(i);
                            //insert the list to the spinner
                            listIds.add(jsonobj.getInt("id"));
                        }
                        Log.d("listIds = ", listIds.toString());


                        ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, listIds);
                        //   dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                        orderDetailsSpinner.setAdapter(adp);
                        orderDetailsSpinner.setSelection(orderId);
                        adp.notifyDataSetChanged();*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                else if (position == 2) { //view orders in progress
                    //clear the list of order details numbers
                    listIds.clear();

                    //clear the status display message
                    orderStatusTextView.setText("");
                    //clear time
                    timeTakenTextView.setText("");


                    //set order status
                    orderStatus = 2;
                    //disable spinner
                    orderDetailsSpinner.setEnabled(true);
                    orderDetailsSpinner.setVisibility(View.VISIBLE);
                    //enable other fields
                    tableNumberTextField.setEnabled(false);
                    // enable meal selections
                    breakfastSpinner.setEnabled(false);
                    lunchSpinner.setEnabled(false);
                    dinnerSpinner.setEnabled(false);
                    dessertSpinner.setEnabled(false);
                    //enable meal quantities
                    breakfastQtyEditText.setEnabled(false);
                    lunchQtyEditText.setEnabled(false);
                    dinnerQtyEditText.setEnabled(false);
                    dessertQtyEditText.setEnabled(false);
                    //enable notes
                    notesEditText.setEnabled(false);

                    //load data from server
                    try {
                        //  url = new URL("http://www.moortala.com/services/capstone/orders/findCurrentAndCompletedOrders");
                        url = new URL("http://www.moortala.com/services/capstone/orders/findByStatus/5");


                        Log.d("URL", url.toString());
                        //connect
                        //  cm = new ConnectionManager(url);
                        //  jsonArray = new JSONArray(cm.execute(url).get());
                        cm = new ConnectionManager(WaiterPage.this, new Callback() {
                            @Override
                            public void run(Object result) {
                                Log.d("callback", "done" + result.toString());

                                try {
                                    jsonArray = new JSONArray(result.toString());

                                    Log.d("jsonArray = ", jsonArray.toString());
                                    //get the list of all the order ids

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                                        //insert the list to the spinner
                                        listIds.add(jsonobj.getInt("id"));
                                    }
                                    Log.d("listIds = ", listIds.toString());


                                    ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, listIds);
                                    //   dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                                    orderDetailsSpinner.setAdapter(adp);
                                    orderDetailsSpinner.setSelection(orderId);
                                    adp.notifyDataSetChanged();



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        cm.execute(url);


                        /////////////

                   /*     jsonArray = new JSONArray(cm.execute(url).get());

                        Log.d("jsonArray = ", jsonArray.toString());
                        //get the list of all the order ids

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = jsonArray.getJSONObject(i);
                            //insert the list to the spinner
                            listIds.add(jsonobj.getInt("id"));
                        }
                        Log.d("listIds = ", listIds.toString());


                        ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, listIds);
                        //   dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                        orderDetailsSpinner.setAdapter(adp);
                        orderDetailsSpinner.setSelection(orderId);
                        adp.notifyDataSetChanged();*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                else if (position == 3) { //view completed orders


                    //clear the list of order details numbers
                    listIds.clear();
                    //clear the status display message
                    orderStatusTextView.setText("");
                    //clear time
                    timeTakenTextView.setText("");

                    //set order status
                    orderStatus = 3;
                    //disable spinner
                    orderDetailsSpinner.setEnabled(true);
                    orderDetailsSpinner.setVisibility(View.VISIBLE);
                    //enable other fields
                    tableNumberTextField.setEnabled(false);
                    // enable meal selections
                    breakfastSpinner.setEnabled(false);
                    lunchSpinner.setEnabled(false);
                    dinnerSpinner.setEnabled(false);
                    dessertSpinner.setEnabled(false);
                    //enable meal quantities
                    breakfastQtyEditText.setEnabled(false);
                    lunchQtyEditText.setEnabled(false);
                    dinnerQtyEditText.setEnabled(false);
                    dessertQtyEditText.setEnabled(false);
                    //enable notes
                    notesEditText.setEnabled(false);

                    //load data from server
                    try {
                        //  url = new URL("http://www.moortala.com/services/capstone/orders");
                        url = new URL("http://www.moortala.com/services/capstone/orders/findByStatus/4");


                        Log.d("URL", url.toString());
                        //connect
                        //  cm = new ConnectionManager(url);
                        //  jsonArray = new JSONArray(cm.execute(url).get());
                        cm = new ConnectionManager(WaiterPage.this, new Callback() {
                            @Override
                            public void run(Object result) {
                                Log.d("callback", "done" + result.toString());

                                try {
                                    jsonArray = new JSONArray(result.toString());

                                    Log.d("jsonArray = ", jsonArray.toString());
                                    //get the list of all the order ids

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                                        //insert the list to the spinner
                                        listIds.add(jsonobj.getInt("id"));
                                    }
                                    Log.d("listIds = ", listIds.toString());


                                    ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, listIds);
                                    //   dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                                    orderDetailsSpinner.setAdapter(adp);
                                    orderDetailsSpinner.setSelection(orderId);
                                    adp.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        cm.execute(url);


                        /////////////

                   /*     jsonArray = new JSONArray(cm.execute(url).get());

                        Log.d("jsonArray = ", jsonArray.toString());
                        //get the list of all the order ids

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = jsonArray.getJSONObject(i);
                            //insert the list to the spinner
                            listIds.add(jsonobj.getInt("id"));
                        }
                        Log.d("listIds = ", listIds.toString());


                        ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, listIds);
                        //   dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                        orderDetailsSpinner.setAdapter(adp);
                        orderDetailsSpinner.setSelection(orderId);
                        adp.notifyDataSetChanged();*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }



                else if (position == 4) { //edit order

                    //set order status
                    orderStatus = 4;
                    //disable spinner
                    orderDetailsSpinner.setEnabled(true);
                    orderDetailsSpinner.setVisibility(View.VISIBLE);
                    //enable other fields
                    tableNumberTextField.setEnabled(true);
                    // enable meal selections
                    breakfastSpinner.setEnabled(true);
                    lunchSpinner.setEnabled(true);
                    dinnerSpinner.setEnabled(true);
                    dessertSpinner.setEnabled(true);
                    //enable meal quantities
                    breakfastQtyEditText.setEnabled(true);
                    lunchQtyEditText.setEnabled(true);
                    dinnerQtyEditText.setEnabled(true);
                    dessertQtyEditText.setEnabled(true);
                    //enable notes
                    notesEditText.setEnabled(true);


                } else if (position == 5) {//cancel order radio button

                    //set order status
                    orderStatus = 5;
                    //disable spinner
                    orderDetailsSpinner.setEnabled(true);
                    orderDetailsSpinner.setVisibility(View.VISIBLE);
                    //enable other fields
                    tableNumberTextField.setEnabled(false);
                    // enable meal selections
                    breakfastSpinner.setEnabled(false);
                    lunchSpinner.setEnabled(false);
                    dinnerSpinner.setEnabled(false);
                    dessertSpinner.setEnabled(false);
                    //enable meal quantities
                    breakfastQtyEditText.setEnabled(false);
                    lunchQtyEditText.setEnabled(false);
                    dinnerQtyEditText.setEnabled(false);
                    dessertQtyEditText.setEnabled(false);
                    //enable notes
                    notesEditText.setEnabled(true);


                    //load data from server
          /*          try {
                        url = new URL("http://www.moortala.com/services/capstone/orders");
                        Log.d("URL", url.toString());
                        //connect

                        cm = new ConnectionManager(WaiterPage.this, new Callback() {
                            @Override
                            public void run(Object result) {
                                try {
                                    jsonArray = new JSONArray(result.toString());
                                    Log.d("jsonArray = ", jsonArray.toString());
                                    //get the list of all the order ids

                                    //listIds.add(0);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                                        //insert the list to the spinner
                                        listIds.add(jsonobj.getInt("id"));
                                    }
                                    Log.d("listIds = ", listIds.toString());


                                    ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, listIds);
                                    //   dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                                    orderDetailsSpinner.setAdapter(adp);
                                    adp.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        cm.execute(url);

                       *//* cm = new ConnectionManager(url);
                        jsonArray = new JSONArray(cm.execute(url).get());
                        Log.d("jsonArray = ", jsonArray.toString());
                        //get the list of all the order ids

                        //listIds.add(0);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = jsonArray.getJSONObject(i);
                            //insert the list to the spinner
                            listIds.add(jsonobj.getInt("id"));
                        }
                        Log.d("listIds = ", listIds.toString());


                        ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, listIds);
                        //   dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                        orderDetailsSpinner.setAdapter(adp);
                        adp.notifyDataSetChanged();*//*
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }/* else if (position == 4) { //edit order radio button

                    //set order status
                    orderStatus = 4;
                    //disable spinner
                    orderDetailsSpinner.setEnabled(true);
                    orderDetailsSpinner.setVisibility(View.VISIBLE);
                    //enable other fields
                    tableNumberTextField.setEnabled(false);
                    // enable meal selections
                    breakfastSpinner.setEnabled(false);
                    lunchSpinner.setEnabled(false);
                    dinnerSpinner.setEnabled(false);
                    dessertSpinner.setEnabled(false);
                    //enable meal quantities
                    breakfastQtyEditText.setEnabled(false);
                    lunchQtyEditText.setEnabled(false);
                    dinnerQtyEditText.setEnabled(false);
                    dessertQtyEditText.setEnabled(false);
                    //enable notes
                    notesEditText.setEnabled(true);


                }*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner to get the value of current orders

        //TODO
        orderDetailsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(" item selected = " + parent.getItemAtPosition(position));
                orderId = (Integer) parent.getItemAtPosition(position);

                ///display selected order

                try {
                    msg = new Messages(WaiterPage.this);


                    //      msg.displayAlert();
                    //  msg.displayDialog("loading", "please wait");
                    orderNumber = orderId;
                    url = new URL("http://moortala.com/services/capstone/orders/getOrder/" + orderNumber);
                    //  Log.d("URL view order submit",url.toString());
                    //connect

                    cm = new ConnectionManager(WaiterPage.this, new Callback() {
                        @Override
                        public void run(Object result) {
                            Log.d("callback", "done" + result.toString());
                            try {
                                Log.d("result = ", result.toString());
                                //  jsonArray = new JSONArray(result.toString());
                                jsonObject = new JSONObject(result.toString());
                                Log.d("jsonObject = ", jsonObject.toString());

                                /// populate fields
                                od = new OrderData(jsonObject);
                                //table number
                                tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
                                tableNumberTextField.setText(Long.toString(od.getTableNumber()));

                                //display the status

                                orderStatusTextView.setText(od.getOrderStatusStrings());

                                // select default meals
                                Log.d("spinners", "" + od.getBreakfast() + "-" + od.getLunch() + "-" + od.getDinner() + "-" + od.getDessert());
                                breakfastSpinner.setSelection(od.getBreakfast().intValue());
                                lunchSpinner.setSelection(od.getLunch().intValue());
                                dinnerSpinner.setSelection(od.getDinner().intValue());
                                dessertSpinner.setSelection(od.getDessert().intValue());

                                Log.d("qty", "" + od.getBreakfast_qty() + "-" + od.getLunch_qty() + "-" + od.getDinner_qty() + "-" + od.getDessert_qty());

                                breakfastQtyEditText.setText(od.getBreakfast_qty().toString());
                                lunchQtyEditText.setText(od.getLunch_qty().toString());
                                dinnerQtyEditText.setText(od.getDinner_qty().toString());
                                dessertQtyEditText.setText(od.getDessert_qty().toString());

                                //clear the notes
                                notesEditText = (EditText) findViewById(R.id.notesEditText);
                                notesEditText.setText(od.getNotes());

                                //display the time taken to complete order
                                if (od.getEnd_time() == null || od.getEnd_time() == 0) {
                                } else {
                                    long minutes = (int) (((od.getEnd_time() - od.getStart_time()) / 1000));

                                    timeTakenTextView.setText("" + TimeUnit.MILLISECONDS.toMinutes(minutes));

                                }
                                msg.displayToast(WaiterPage.this, "Order loaded");

                                // submitButton.setEnabled(false);

                                //display the time taken to complete order
                                if (od.getEnd_time() == null || od.getEnd_time() == 0) {
                                } else {
                                    long minutes = (int) (((od.getEnd_time() - od.getStart_time())));
                                    timeTakenTextView = (TextView) findViewById(R.id.timeTakenTextView);
                                    timeTakenTextView.setText("" + TimeUnit.MILLISECONDS.toMinutes(minutes));
                                }

                                //now that its loaded, lets make the cancel and edit available


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    cm.execute(url);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //resources
        //name field
        userNameTextView = (TextView)findViewById(R.id.userNameTextView);
        //table number
        tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
        //order detail spinner
        orderDetailsSpinner = (Spinner) findViewById(R.id.orderDetailsSpinner);
        //order status spinner
        orderActionsSpinner = (Spinner) findViewById(R.id.orderActionsSpinner);
        //status details box
        statusDetailEditText = ((EditText) findViewById(R.id.notesEditText));
        //break fast spinner
        breakfastSpinner = (Spinner) findViewById(R.id.breakfastSpinner);
        //break fast qty box
        breakfastQtyEditText = (EditText) findViewById(R.id.breakfastQtyEditText);
        //lunch spinner
        lunchSpinner = (Spinner) findViewById(R.id.lunchSpinner);
        //lunch qty box
        lunchQtyEditText = (EditText) findViewById(R.id.lunchQtyEditText);
        //dinner spinner
        dinnerSpinner = (Spinner) findViewById(R.id.dinnerSpinner);
        //dinner qty box
        dinnerQtyEditText = (EditText) findViewById(R.id.dinnerQtyEditText);
        //dessert spinner
        dessertSpinner = (Spinner) findViewById(R.id.dessertSpinner);
        //dessert qty box
        dessertQtyEditText = (EditText) findViewById(R.id.dessertQtyEditText);
        // the notes
        notesEditText = ((EditText) findViewById(R.id.notesEditText));
        //submit  button
        submitButton =(Button)findViewById(R.id.submitButton);
        //clear button
        clearButton =(Button)findViewById(R.id.clearButton);


        //set up spinner for order status
        //set up spinner for order berakfast spinner
        statuses=new String[6];
        statuses[0]="0 - NEW ORDER";
        statuses[1]="1 - VIEW NEW ORDERS";
        statuses[2]="2 - VIEW IN PROGRESS";
        statuses[3]="3 - VIEW COMPLETED";
        statuses[4]="4 - EDIT";
        statuses[5]="5 - CANCEL";



        //set up spinner for order berakfast spinner
        breakfasts=new String[6];
        breakfasts[0]="0 - Make a selection";
        breakfasts[1]="1 - Omelette";
        breakfasts[2]="2 - Cereals";
        breakfasts[3]="3 - Pancakes";
        breakfasts[4]="4 - Toasts";
        breakfasts[5]="5 - Waffles";



        //set up spinner for order lunch
        lunches=new String[6];
        lunches[0]="0 - Make a selection";
        lunches[1]="1 - Salad";
        lunches[2]="2 - Lasagna";
        lunches[3]="3 - Fajitas";
        lunches[4]="4 - Burger";
        lunches[5]="5 - Pasta";


        //set up spinner for order dinner
        dinners=new String[6];
        dinners[0]="0 - Make a selection";
        dinners[1]="1 - Soup";
        dinners[2]="2 - French Fries";
        dinners[3]="3 - Chili";
        dinners[4]="4 - Crab Cake";
        dinners[5]="5 - Ciopino";



        //set up spinner for order dessert
        desserts=new String[6];
        desserts[0]="0 - Make a selection";
        desserts[1]="1 - Cheese Cake";
        desserts[2]="2 - Sodas";
        desserts[3]="3 - Chocolate Cake";
        desserts[4]="4 - Ice Cream";
        desserts[5]="5 - Wine";




        String[] values=new String[9];
        values[0]="0 - Make a selection";
        values[1]="1 - Fried Mozzarella";
        values[2]="2 - Stuffed Mushrooms";
        values[3]="3 - Fruit Salad";
        values[4]="4 - Pasta";
        values[5]="5 - Minestrone soup";
        values[6]="6 - Apple Pie";
        values[7]="7 - Lemon Cream Cake";
        values[8]="8 - Chocolate Fondue";




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waiter_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {

            Intent myIntent = new Intent(WaiterPage.this,LoginActivity.class);
           // myIntent.putExtra("userName",email);
            WaiterPage.this.startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //submit the order
    public void submitOrder(View view) {
        //get data from the inputs
            //get table number
        tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);

        statusDetailEditText = ((EditText) findViewById(R.id.notesEditText));
      //  orderNumber = orderId;

      //  userName= "sample";
        deviceId = OrderData.getDeviceMacAddress(this);

            //chose an action to perform
            switch (orderStatus){
                case 0://new order
                    if (tableNumberTextField.getText().toString()==null || statusDetailEditText ==null || (breakfast==0 && lunch==0 && dinner==0 && dessert ==0)){
                        Log.d("==>","please fill required fields");
                    }else {
                        //get current info from screen
                        tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                        mealNumber = MealsData.getMeal(dishNumber);
                        notesEditText = (EditText) findViewById(R.id.notesEditText);
                        notes = notesEditText.getText().toString();
                        //notes = notes.replaceAll("[^a-zA-Z]+", "").trim();
                        notes = notes.replaceAll("\\s+", "").trim();
                        Log.d("notes", notes);
                        userName = userNameTextView.getText().toString().replaceAll("\\s+", "").trim();

                        System.out.println( "Quantities " + breakfastQtyEditText.getText() + lunchQtyEditText.getText().toString() + dinnerQtyEditText.getText().toString()  +dessertQtyEditText.getText().toString());
                        System.out.println( "Quantities 2 " + breakfastQtyEditText.getText().toString().isEmpty() + lunchQtyEditText.getText().toString().isEmpty() + dinnerQtyEditText.getText().toString().isEmpty()  +dessertQtyEditText.getText().toString().isEmpty());
                      //  System.out.println( "Quantities 3" + breakfastQtyEditText.getText().toString().contentEquals(null) + lunchQtyEditText.getText().toString().contentEquals(null) + dinnerQtyEditText.getText().toString().contentEquals(null)  +dessertQtyEditText.getText().toString().contentEquals(null));
                        System.out.println( "Quantities 4 "  + breakfastQtyEditText.getText().toString().contentEquals("") + lunchQtyEditText.getText().toString().contentEquals("") + dinnerQtyEditText.getText().toString().contentEquals("")  +dessertQtyEditText.getText().toString().contentEquals(""));

                        String bf = breakfastQtyEditText.getText().toString();
                        String l = lunchQtyEditText.getText().toString();
                        String d = dinnerQtyEditText.getText().toString();
                        String dd = dessertQtyEditText.getText().toString();

                        if (bf.isEmpty()){
                            breakfastQty =0;
                        }else{
                            breakfastQty = Integer.parseInt(breakfastQtyEditText.getText().toString());
                        }

                        if (l.isEmpty()){
                            lunchQty =0;
                        }else{
                            lunchQty = Integer.parseInt(lunchQtyEditText.getText().toString());

                        }
                        if (d.isEmpty()) {
                            dinnerQty =0;
                        }else{
                            dinnerQty = Integer.parseInt(dinnerQtyEditText.getText().toString());

                        }
                        if (dd.isEmpty()){
                            dessertQty =0;
                        }else{
                            dessertQty = Integer.parseInt(dessertQtyEditText.getText().toString());

                        }



                      /*
                       else{
                            breakfastQty =Integer.parseInt(breakfastQtyEditText.getText().toString());
                            lunchQty =Integer.parseInt(lunchQtyEditText.getText().toString());
                            dinnerQty =Integer.parseInt(dinnerQtyEditText.getText().toString());
                            dessertQty =Integer.parseInt(dessertQtyEditText.getText().toString());
                            System.out.println( "Quantitiesv " + breakfastQty + lunchQty + dinnerQty  +dessertQty);

                        }
                        System.out.println( "Quantitiesv " + breakfastQty + lunchQty + dinnerQty  +dessertQty);*/


                        // Use the Builder class for convenient dialog construction
                        AlertDialog.Builder builder = new AlertDialog.Builder(WaiterPage.this);
                        builder.setTitle("CONFIRM ACTION");
                        builder.setMessage("Create order?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        /////
                                        try {
                                            msg = new Messages(WaiterPage.this);

//http://localhost:8080/capstone/orders/waiter/submit?tableNumber=2&breakfast=77&breakfastQty=2&lunch=777&lunchQty=22&dinner=3&dinnerQty=36&dessert=98&dessertQty=32&notes=%27coke%27&status=1&name=sampleer&deviceId=
                                            url = new URL("http://moortala.com/services/capstone/orders/waiter/submit?tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes="+notes+"&status="+orderStatus+"&waiterName="+userName+"&deviceId="+ deviceId+"&userId="+userID);
                                            //  Log.d("URL", url.toString());
                                            //connect

                                            cm = new ConnectionManager(WaiterPage.this, new Callback() {
                                                @Override
                                                public void run(Object result) {
                                                    try {
                                                        //jsonArray = new JSONArray(result.toString());
                                                        jsonObject = new JSONObject(result.toString());
                                                        Log.d("jsonObject = ", jsonObject.toString());
                                                        OrderData od = new OrderData(jsonObject);
                                                        msg.displayToast(WaiterPage.this, "Order #"+od.getId()+" created");
                                                        //reset fields for next order
                                                        resetFields();

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                           cm.execute(url);




                          /*  cm = new ConnectionManager(url);
                            jsonObject = new JSONObject(cm.execute(url).get());
                            Log.d("jsonObject = ", jsonObject.toString());

                            OrderData od = new OrderData(jsonObject);
                            msg.displayToast(WaiterPage.this, "Order #"+od.getId()+" created");
                            //reset fields for next order
                            resetFields();*/
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        ///////////////////////////
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        // Create the AlertDialog object and return it
                        builder.create().show();



                    }
                    break;
                case 1:  //view an order
                   /* try {
                       msg = new Messages(WaiterPage.this);



                  //      msg.displayAlert();
                      //  msg.displayDialog("loading", "please wait");
                        orderNumber = orderId;
                        url = new URL("http://moortala.com/services/capstone/orders/getOrder/"+orderNumber);
                      //  Log.d("URL view order submit",url.toString());
                        //connect

                        cm = new ConnectionManager(WaiterPage.this, new Callback() {
                            @Override
                            public void run(Object result) {
                                Log.d("callback", "done" + result.toString());
                                try {
                                    Log.d("result = ", result.toString());
                                   //  jsonArray = new JSONArray(result.toString());
                                    jsonObject = new JSONObject(result.toString());
                                    Log.d("jsonObject = ", jsonObject.toString());

                                    /// populate fields
                                    od = new OrderData(jsonObject);
                                    //table number
                                    tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
                                    tableNumberTextField.setText(Long.toString(od.getTableNumber()));

                                    //display the status
                                    orderStatusTextView = (TextView) findViewById(R.id.orderStatusTextView);
                                    orderStatusTextView.setText(od.getOrderStatusStrings());

                                    // select default meals
                                    Log.d("spinners", "" + od.getBreakfast() + "-" + od.getLunch() + "-" + od.getDinner() + "-" + od.getDessert());
                                    breakfastSpinner.setSelection(od.getBreakfast().intValue());
                                    lunchSpinner.setSelection(od.getLunch().intValue());
                                    dinnerSpinner.setSelection(od.getDinner().intValue());
                                    dessertSpinner.setSelection(od.getDessert().intValue());

                                    Log.d("qty", "" + od.getBreakfast_qty() + "-" + od.getLunch_qty() + "-" + od.getDinner_qty() + "-" + od.getDessert_qty());

                                    breakfastQtyEditText.setText(od.getBreakfast_qty().toString());
                                    lunchQtyEditText.setText(od.getLunch_qty().toString());
                                    dinnerQtyEditText.setText(od.getDinner_qty().toString());
                                    dessertQtyEditText.setText(od.getDessert_qty().toString());

                                    //clear the notes
                                    notesEditText = (EditText) findViewById(R.id.notesEditText);
                                    notesEditText.setText(od.getNotes());

                                    //display the time taken to complete order
                                    if (od.getEnd_time() ==null || od.getEnd_time()==0){
                                    }else{
                                        long minutes = (int) (((od.getEnd_time() - od.getStart_time()) / 1000));
                                        timeTakenTextView =(TextView) findViewById(R.id.timeTakenTextView);
                                        timeTakenTextView.setText(""+ TimeUnit.MILLISECONDS.toMinutes(minutes) );

                                    }
                                    msg.displayToast(WaiterPage.this, "Order loaded");

                                    //now that its loaded, lets make the cancel and edit available


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        cm.execute(url);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                     break;
                case 4://edit order
                    try {

                        //get current info from screen
                        tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                        breakfastQty =Integer.parseInt(breakfastQtyEditText.getText().toString());
                        lunchQty =Integer.parseInt(lunchQtyEditText.getText().toString());
                        dinnerQty =Integer.parseInt(dinnerQtyEditText.getText().toString());
                        dessertQty =Integer.parseInt(dessertQtyEditText.getText().toString());
                       // notes = ((EditText) findViewById(R.id.notesEditText)).getText().toString();
                       // notes = notes.replaceAll("[^a-zA-Z]+", "").trim();

                        notes = notesEditText.getText().toString();
                        //notes = notes.replaceAll("[^a-zA-Z]+", "").trim();
                        notes = notes.replaceAll("\\s+", "").trim();

                        userName = userNameTextView.getText().toString().replaceAll("\\s+", "").trim();


                        orderNumber = orderId;
                        orderNumber = Integer.parseInt(orderDetailsSpinner.getSelectedItem().toString());
                        //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
                        url = new URL("http://moortala.com/services/capstone/orders/waiter/edit?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes="+notes+"&status=2&waiterName="+userName+"&deviceId="+ deviceId+"&userId="+userID);
                       // url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&meal="+dishNumber+"&notes="+notes+ "&status="+orderStatus+"&name="+"&deviceId=");
                  //      Log.d("URL",url.toString());


                        // Use the Builder class for convenient dialog construction
                        AlertDialog.Builder builder = new AlertDialog.Builder(WaiterPage.this);
                        builder.setTitle("CONFIRM ACTION");
                        builder.setMessage("Edit order?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!   //connect


                                        cm = new ConnectionManager(WaiterPage.this, new Callback() {
                                            @Override
                                            public void run(Object result) {
                                                try {
                                                    //  jsonArray = new JSONArray(result.toString());

                                                    jsonObject = new JSONObject(result.toString());
                                                    Log.d("jsonObject = " , jsonObject.toString());



                                                    //populate the fields
                                                    //table number
                                                    od = new OrderData(jsonObject);
                                                    tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
                                                    tableNumberTextField.setText(Long.toString(od.getTableNumber()));

                                                    //display the status
                                                    orderStatusTextView = (TextView) findViewById(R.id.orderStatusTextView);
                                                    orderStatusTextView.setText(od.getOrderStatusStrings());

                                                    // select default meals

                                                    breakfastSpinner.setSelection(od.getBreakfast().intValue());
                                                    lunchSpinner.setSelection(od.getLunch().intValue());
                                                    dinnerSpinner.setSelection(od.getDinner().intValue());
                                                    dessertSpinner.setSelection(od.getDessert().intValue());

                                                    breakfastQtyEditText.setText(od.getBreakfast_qty().toString());
                                                    lunchQtyEditText.setText(od.getLunch_qty().toString());
                                                    dinnerQtyEditText.setText(od.getDinner_qty().toString());
                                                    dessertQtyEditText.setText(od.getDessert_qty().toString());

                                                    // the notes
                                                    notesEditText = ((EditText) findViewById(R.id.notesEditText));
                                                    notesEditText.setText(od.getNotes());




                                                    msg = new Messages(WaiterPage.this);
                                                    msg.displayToast(WaiterPage.this, "Order Updated");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                        cm.execute(url);
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        // Create the AlertDialog object and return it
                         builder.create().show();




                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                case 5://cancel
                    try {

                        // 1. Instantiate an AlertDialog.Builder with its constructor


                        if (tableNumberTextField.getText().toString() == null || statusDetailEditText == null || (breakfast == 0 && lunch == 0 && dinner == 0 && dessert == 0)) {

                            msg = new Messages(WaiterPage.this);
                            msg.displayToast(WaiterPage.this, "Load order first!");
                        } else {


//get current info from screen
                        tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                        breakfastQty = Integer.parseInt(breakfastQtyEditText.getText().toString());
                        lunchQty = Integer.parseInt(lunchQtyEditText.getText().toString());
                        dinnerQty = Integer.parseInt(dinnerQtyEditText.getText().toString());
                        dessertQty = Integer.parseInt(dessertQtyEditText.getText().toString());
                        notes = ((EditText) findViewById(R.id.notesEditText)).getText().toString();

                        notes = notes.replaceAll("\\s+", "").trim();
                        userName = userNameTextView.getText().toString().replaceAll("\\s+", "").trim();

                        orderNumber = orderId;
                        orderNumber = Integer.parseInt(orderDetailsSpinner.getSelectedItem().toString());
                        //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
                        url = new URL("http://moortala.com/services/capstone/orders/waiter/edit?orderNumber=" + orderNumber + "&tableNumber=" + tableNumber + "&breakfast=" + breakfast + "&breakfastQty=" + breakfastQty + "&lunch=" + lunch + "&lunchQty=" + lunchQty + "&dinner=" + dinner + "&dinnerQty=" + dinnerQty + "&dessert=" + dessert + "&dessertQty=" + dessertQty + "&notes=CANCELED-" + notes + "&status=" + orderStatus + "&waiterName=" + userName + "&deviceId=" + deviceId + "&userId=" + userID);
                        //      Log.d("URL",url.toString());


                            // Use the Builder class for convenient dialog construction
                            AlertDialog.Builder builder = new AlertDialog.Builder(WaiterPage.this);
                            builder.setTitle("CONFIRM ACTION");
                            builder.setMessage("Cancel order?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // FIRE ZE MISSILES!  //connect

                                            cm = new ConnectionManager(WaiterPage.this, new Callback() {
                                                @Override
                                                public void run(Object result) {
                                                    try {
                                                        // jsonArray = new JSONArray(result.toString());
                                                        //  cm = new ConnectionManager(url);
                                                        jsonObject = new JSONObject(result.toString());
                                                        Log.d("jsonObject = ", jsonObject.toString());

                                                        msg = new Messages(WaiterPage.this);
                                                        msg.displayToast(WaiterPage.this, "Order Canceled");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                            cm.execute(url);
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                            // Create the AlertDialog object and return it
                            builder.create().show();



                    }

                      /*  cm = new ConnectionManager(url);
                        jsonObject = new JSONObject(cm.execute(url).get());
                        Log.d("jsonObject = " , jsonObject.toString());

                        msg = new Messages(WaiterPage.this);
                        msg.displayToast(WaiterPage.this, "Order Canceled");*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //display message that it has been canceled

                   // resetFields();
                    break;

                case 2://complete order
                    try {

                        //get current info from screen
                        tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                        breakfastQty =Integer.parseInt(breakfastQtyEditText.getText().toString());
                        lunchQty =Integer.parseInt(lunchQtyEditText.getText().toString());
                        dinnerQty =Integer.parseInt(dinnerQtyEditText.getText().toString());
                        dessertQty =Integer.parseInt(dessertQtyEditText.getText().toString());
                        notes = ((EditText) findViewById(R.id.notesEditText)).getText().toString();
                        notes = notes.replaceAll("\\s+", "").trim();
                        userName = userNameTextView.getText().toString().replaceAll("\\s+", "").trim();

                        orderNumber = orderId;
                        orderNumber = Integer.parseInt(orderDetailsSpinner.getSelectedItem().toString());
                        //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
                       // url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes=DONE-"+notes+"&status="+orderStatus+"&name="+userName+"&deviceId="+ deviceId);
                      //  Log.d("URL",url.toString());
                        //connect
                     //   cm = new ConnectionManager(url);
                    //    jsonObject = new JSONObject(cm.execute(url).get());
                   //     Log.d("jsonObject = " , jsonObject.toString());
//
                        msg = new Messages(WaiterPage.this);
                       msg.displayToast(WaiterPage.this, "You need to be chef/manager to complete this action");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //display message that it has been canceled

                  //  resetFields();
                    break;

                default:
                    ;
                    Log.d("???? " , "nothing valid selected");
                    break;
            }

    }

    private void resetFields() {
        //reset table number
        tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
        tableNumberTextField.setText("");
       //order status spinner
        orderActionsSpinner.setSelection(0);
       //meals
        breakfastSpinner.setSelection(0);
        lunchSpinner.setSelection(0);
        dinnerSpinner.setSelection(0);
        dessertSpinner.setSelection(0);

        breakfastQtyEditText.setText("");
        lunchQtyEditText.setText("");
        dinnerQtyEditText.setText("");
        dessertQtyEditText.setText("");

        //clear the notes
        notesEditText = ((EditText) findViewById(R.id.notesEditText));
        notesEditText.setText("");
        //clear the status details ?
        statusDetailEditText = ((EditText) findViewById(R.id.notesEditText));
        statusDetailEditText.setText("");
    }

    public void clearForm(View view) {
        resetFields();
    }

    public void handleResults(String data) {

    }


    private void scheduleNotification(Notification notification, int orderId, AlarmManager alarmManager) {
        Intent notificationIntent = new Intent(this, OrderNotifier.class);
        notificationIntent.putExtra(OrderNotifier.NOTIFICATION_ID, orderId);
        notificationIntent.putExtra(OrderNotifier.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, orderId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + orderId;
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        // alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        // alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        //         SystemClock.elapsedRealtime(),2*60*1000,pendingIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);

    }

    private Notification getNotification(String content) {
        k=0;
        Notification noti = new Notification.Builder(this)
                .setContentTitle("ORDER(S) UPDATES")
                .setContentText(content).setSmallIcon(R.mipmap.ic_launcher).build();

       /* Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);*/
        return noti;
    }

}
