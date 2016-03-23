package moortala.com.ordertracker;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaiterPage extends AppCompatActivity {
    private int orderStatus;
    private int tableNumber;
    private EditText tableNumberTextField;
    private String notes;
    private static URL url;
    private int orderNumber;
    private String userName;
    private String deviceId;
    private TextView userNameTextView;
    private JSONObject jsonObject;
    private ConnectionManager cm;
    private EditText notesEditText;
    private Spinner orderDetailsSpinner;
    private JSONArray jsonArray;
    private List<Integer> listIds;
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
    private String[] actions;
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
    private int inProgressSize;
    private int inProgressVal;
    private List<Integer> inProgressOrders, completedOrders;
    private ArrayList<Integer> updatedOrders;


    @Override
    protected void onResume() {
        super.onResume();

        try {
            //initiate variables
            orderStatusTextView = (TextView) findViewById(R.id.orderStatusTextView);
            listIds = new ArrayList<>();
            timeTakenTextView = (TextView) findViewById(R.id.timeTakenTextView);

            //get the user name from the login activity
            userNameTextView.setText(getIntent().getStringExtra("user"));
            userName = getIntent().getStringExtra("user");
            userID = getIntent().getLongExtra("user_id", 0);

            //arraylists for the notification service variables
            inProgressOrders = new ArrayList<>();
            completedOrders = new ArrayList<>();
            updatedOrders = new ArrayList<>();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        //start the notifier service on resume
        try {
            url = new URL("http://moortala.com/services/capstone/orders/findCurrentOrders");
            //connect to server and get json data
            ConnectionManager cm1 = new ConnectionManager(WaiterPage.this, new Callback() {
                @Override
                public void run(Object result) {
                    try {
                        jsonArray = new JSONArray(result.toString());
                        Log.d("jsonArray = ", jsonArray.toString());
                        //get the list of all the order ids
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = jsonArray.getJSONObject(i);
                            //get in progress orders
                            if (jsonobj.getInt("status") == 5) {
                                inProgressOrders.add(jsonobj.getInt("id"));
                            }
                            //get completed orders
                            if (jsonobj.getInt("status") == 4) {
                                completedOrders.add(jsonobj.getInt("id"));
                            }
                            //get edited orders
                            if (jsonobj.getInt("status") == 2) {
                                updatedOrders.add(jsonobj.getInt("id"));
                            }
                        }

                        inProgressSize = jsonArray.length();

                        //check previous and new values in order to display notification
                        if (inProgressSize > inProgressVal) {
                            inProgressVal = inProgressSize; //keep old value

                            AlarmManager alarmManager = null;
                            AlarmManager alarmManager2 = null;
                            AlarmManager alarmManager3 = null;
                            scheduleNotification(getNotification("Order(s) " + inProgressOrders.toString() + " started"), 1, alarmManager);
                            scheduleNotification(getNotification("Order(s) " + completedOrders.toString() + " completed"), 2, alarmManager2);
                            scheduleNotification(getNotification("Order(s) " + updatedOrders.toString() + " updated"), 3, alarmManager3);

                        } else {
                            AlarmManager alarmManager4 = null;
                            scheduleNotification(getNotification(""), 1, alarmManager4);
                            //update the data
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            cm1.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //populate order action spinner with the actions array
        ArrayAdapter<String> statusesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, actions);
        orderActionsSpinner.setAdapter(statusesAdapter);
        statusesAdapter.notifyDataSetChanged();

        //listen to the actions spinner for changes
        orderActionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderId = position;
                Log.d("action selected = ", (String) parent.getItemAtPosition(position));
                orderStatus = orderId;

                //disable the submit button when one is viewing: new, in progress and completed orders
                if (orderId == 1 || orderId == 2 || orderId == 3) {
                    submitButton.setEnabled(false);
                } else {
                    submitButton.setEnabled(true);
                }

                if (position == 0) { //if new order is selected
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
                } else if (position == 1) { //view new orders is selected
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
                        url = new URL("http://www.moortala.com/services/capstone/orders/findByStatus/0/" + userID);
                        Log.d("URL-Actions-View_new:", url.toString());
                        //connect
                        cm = new ConnectionManager(WaiterPage.this, new Callback() {
                            @Override
                            public void run(Object result) {
                                try {
                                    jsonArray = new JSONArray(result.toString());

                                    Log.d("jsonArray = ", jsonArray.toString());
                                    //get the list of all the order ids
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                                        //insert the list to the spinner
                                        listIds.add(jsonobj.getInt("id"));
                                    }
                                    Log.d("IDS:Viewable_orders= ", listIds.toString());
                                    ArrayAdapter<Integer> adp = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, listIds);
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (position == 2) { //view orders in progress is selected
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
                        url = new URL("http://www.moortala.com/services/capstone/orders/findByStatus/5/" + userID);
                        Log.d("URL-Actions-View_prgss:", url.toString());
                        //connect
                        cm = new ConnectionManager(WaiterPage.this, new Callback() {
                            @Override
                            public void run(Object result) {
                                try {
                                    jsonArray = new JSONArray(result.toString());

                                    Log.d("jsonArray = ", jsonArray.toString());
                                    //get the list of all the order ids
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                                        //insert the list to the spinner
                                        listIds.add(jsonobj.getInt("id"));
                                    }
                                    Log.d("IDS:In_Prgs_orders = ", listIds.toString());
                                    ArrayAdapter<Integer> adp = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, listIds);
                                    orderDetailsSpinner.setAdapter(adp);
                                    orderDetailsSpinner.setSelection(orderId);
                                    adp.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        cm.execute(url);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (position == 3) { //view completed orders
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
                        url = new URL("http://www.moortala.com/services/capstone/orders/findCompletedOrders/4/" + userID);
                        Log.d("URL-Actions-View_comp:", url.toString());
                        //connect
                        cm = new ConnectionManager(WaiterPage.this, new Callback() {
                            @Override
                            public void run(Object result) {
                                try {
                                    jsonArray = new JSONArray(result.toString());
                                    Log.d("jsonArray = ", jsonArray.toString());
                                    //get the list of all the order ids
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                                        //insert the list to the spinner
                                        listIds.add(jsonobj.getInt("id"));
                                    }
                                    Log.d("IDS:comp_orders= ", listIds.toString());
                                    ArrayAdapter<Integer> adp = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, listIds);
                                    orderDetailsSpinner.setAdapter(adp);
                                    orderDetailsSpinner.setSelection(orderId);
                                    adp.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        cm.execute(url);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (position == 4) { //edit order is selected
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

                } else if (position == 5) {//cancel order is selected
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //populate breakfast spinner with the breakfasts array
        ArrayAdapter<String> breakfastsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, breakfasts);
        breakfastSpinner.setAdapter(breakfastsAdapter);
        breakfastsAdapter.notifyDataSetChanged();
        //set a listener for the breakfast spinner
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

        //populate lunch spinner with the lunches array
        ArrayAdapter<String> lunchesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, lunches);
        lunchSpinner.setAdapter(lunchesAdapter);
        lunchesAdapter.notifyDataSetChanged();
        //set a listener for the lunch spinner
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

        //populate dinner spinner with the dinners array
        ArrayAdapter<String> dinnersAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, dinners);
        dinnerSpinner.setAdapter(dinnersAdapter);
        dinnersAdapter.notifyDataSetChanged();
        //set a listnener for the lunch spinner
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

        //populate dessert spinner with the dessert array
        ArrayAdapter<String> dessertsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, desserts);
        dessertSpinner.setAdapter(dessertsAdapter);
        dessertsAdapter.notifyDataSetChanged();
        //listenner for the dessert spinner
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

        //spinner to get the value of current orders
        orderDetailsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("selected detail= " , "" + parent.getItemAtPosition(position));
                orderId = (Integer) parent.getItemAtPosition(position);
                ///display selected order
                try {
                    msg = new Messages(WaiterPage.this);
                    orderNumber = orderId;
                    url = new URL("http://moortala.com/services/capstone/orders/getOrder/" + orderNumber);
                    //connect
                    cm = new ConnectionManager(WaiterPage.this, new Callback() {
                        @Override
                        public void run(Object result) {
                            try {
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
                                breakfastSpinner.setSelection(od.getBreakfast().intValue());
                                lunchSpinner.setSelection(od.getLunch().intValue());
                                dinnerSpinner.setSelection(od.getDinner().intValue());
                                dessertSpinner.setSelection(od.getDessert().intValue());
                                //select default meal quantities
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
                                    timeTakenTextView = (TextView) findViewById(R.id.timeTakenTextView);
                                    long minutes = (int) (((od.getEnd_time() - od.getStart_time())));
                                   // long minutes = (int) (((od.getEnd_time() - od.getStart_time()) / 1000));
                                    timeTakenTextView.setText("" + TimeUnit.MILLISECONDS.toMinutes(minutes));
                                }
                                //display status message
                                msg.displayToast(WaiterPage.this, "Order loaded");

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
    protected void onCreate(Bundle savedInstanceState) {//when activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //variables
        //name field
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        //table number
        tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
        //order detail spinner
        orderDetailsSpinner = (Spinner) findViewById(R.id.orderDetailsSpinner);
        //order status spinner
        orderActionsSpinner = (Spinner) findViewById(R.id.orderActionsSpinner);
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
        submitButton = (Button) findViewById(R.id.submitButton);
        //clear button
        clearButton = (Button) findViewById(R.id.clearButton);

        //set up spinner for order status
        actions = new String[6];
        actions[0] = "0 - NEW ORDER";
        actions[1] = "1 - VIEW NEW ORDERS";
        actions[2] = "2 - VIEW IN PROGRESS";
        actions[3] = "3 - VIEW COMPLETED";
        actions[4] = "4 - EDIT";
        actions[5] = "5 - CANCEL";

        //set up spinner for order breakfast
        breakfasts = new String[6];
        breakfasts[0] = "0 - Make a selection";
        breakfasts[1] = "1 - Omelette";
        breakfasts[2] = "2 - Cereals";
        breakfasts[3] = "3 - Pancakes";
        breakfasts[4] = "4 - Toasts";
        breakfasts[5] = "5 - Waffles";

        //set up spinner for order lunch
        lunches = new String[6];
        lunches[0] = "0 - Make a selection";
        lunches[1] = "1 - Salad";
        lunches[2] = "2 - Lasagna";
        lunches[3] = "3 - Fajitas";
        lunches[4] = "4 - Burger";
        lunches[5] = "5 - Pasta";

        //set up spinner for order dinner
        dinners = new String[6];
        dinners[0] = "0 - Make a selection";
        dinners[1] = "1 - Soup";
        dinners[2] = "2 - French Fries";
        dinners[3] = "3 - Chili";
        dinners[4] = "4 - Crab Cake";
        dinners[5] = "5 - Ciopino";

        //set up spinner for order dessert
        desserts = new String[6];
        desserts[0] = "0 - Make a selection";
        desserts[1] = "1 - Cheese Cake";
        desserts[2] = "2 - Sodas";
        desserts[3] = "3 - Chocolate Cake";
        desserts[4] = "4 - Ice Cream";
        desserts[5] = "5 - Wine";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //when option menus are created
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waiter_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//when option menus are selected
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //if settings option is selected
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        //if log out option is selected
        if (id == R.id.action_logout) {

            Intent myIntent = new Intent(WaiterPage.this, LoginActivity.class);
            // myIntent.putExtra("userName",email);
            WaiterPage.this.startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //submit the order button is clicked
    public void submitOrder(View view) {
        //get data from the inputs
        //get table number
        tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
        deviceId = OrderData.getDeviceMacAddress(WaiterPage.this);

        //chose an action to perform
        switch (orderStatus) {
            case 0://new order
                if (tableNumberTextField.getText().toString() == null || (breakfast == 0 && lunch == 0 && dinner == 0 && dessert == 0)) {
                    msg = new Messages(WaiterPage.this);
                    msg.displayToast(WaiterPage.this, "Please fill a Table#, meal and Qty fields");
                } else {
                    //get current info from screen
                    tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                    notesEditText = (EditText) findViewById(R.id.notesEditText);
                    notes = notesEditText.getText().toString();
                    //notes = notes.replaceAll("[^a-zA-Z]+", "").trim();
                    notes = notes.replaceAll("\\s+", "").trim();
                    Log.d("notes", notes);
                    userName = userNameTextView.getText().toString().replaceAll("\\s+", "").trim();

                    String bf = breakfastQtyEditText.getText().toString();
                    String l = lunchQtyEditText.getText().toString();
                    String d = dinnerQtyEditText.getText().toString();
                    String dd = dessertQtyEditText.getText().toString();

                    //empty,null check
                    if (bf.isEmpty()) {
                        breakfastQty = 0;
                    } else {
                        breakfastQty = Integer.parseInt(breakfastQtyEditText.getText().toString());
                    }
                    if (l.isEmpty()) {
                        lunchQty = 0;
                    } else {
                        lunchQty = Integer.parseInt(lunchQtyEditText.getText().toString());
                    }
                    if (d.isEmpty()) {
                        dinnerQty = 0;
                    } else {
                        dinnerQty = Integer.parseInt(dinnerQtyEditText.getText().toString());
                    }
                    if (dd.isEmpty()) {
                        dessertQty = 0;
                    } else {
                        dessertQty = Integer.parseInt(dessertQtyEditText.getText().toString());
                    }

                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(WaiterPage.this);
                    builder.setTitle("CONFIRM ACTION");
                    builder.setMessage("Create order?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            try {
                                msg = new Messages(WaiterPage.this);

                                url = new URL("http://moortala.com/services/capstone/orders/waiter/submit?tableNumber=" + tableNumber + "&breakfast=" + breakfast + "&breakfastQty=" + breakfastQty + "&lunch=" + lunch + "&lunchQty=" + lunchQty + "&dinner=" + dinner + "&dinnerQty=" + dinnerQty + "&dessert=" + dessert + "&dessertQty=" + dessertQty + "&notes=" + notes + "&status=" + orderStatus + "&waiterName=" + userName + "&deviceId=" + deviceId + "&userId=" + userID);
                                //connect
                                cm = new ConnectionManager(WaiterPage.this, new Callback() {
                                    @Override
                                    public void run(Object result) {
                                        try {
                                            //jsonArray = new JSONArray(result.toString());
                                            jsonObject = new JSONObject(result.toString());
                                            Log.d("jsonObject = ", jsonObject.toString());
                                            OrderData od = new OrderData(jsonObject);
                                            msg.displayToast(WaiterPage.this, "Order #" + od.getId() + " created");
                                            //reset fields for next order
                                            resetFields();

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
            case 1:
                break;

            case 2: //complete an order
                break;

            case 3:
                break;

            case 4://edit order
                try {

                    //get current info from screen
                    tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                    breakfastQty = Integer.parseInt(breakfastQtyEditText.getText().toString());
                    lunchQty = Integer.parseInt(lunchQtyEditText.getText().toString());
                    dinnerQty = Integer.parseInt(dinnerQtyEditText.getText().toString());
                    dessertQty = Integer.parseInt(dessertQtyEditText.getText().toString());
                    notes = notesEditText.getText().toString();
                    //notes = notes.replaceAll("[^a-zA-Z]+", "").trim();
                    notes = notes.replaceAll("\\s+", "").trim();
                    userName = userNameTextView.getText().toString().replaceAll("\\s+", "").trim();
                    orderNumber = orderId;
                    orderNumber = Integer.parseInt(orderDetailsSpinner.getSelectedItem().toString());
                    url = new URL("http://moortala.com/services/capstone/orders/waiter/edit?orderNumber=" + orderNumber + "&tableNumber=" + tableNumber + "&breakfast=" + breakfast + "&breakfastQty=" + breakfastQty + "&lunch=" + lunch + "&lunchQty=" + lunchQty + "&dinner=" + dinner + "&dinnerQty=" + dinnerQty + "&dessert=" + dessert + "&dessertQty=" + dessertQty + "&notes=" + notes + "&status=2&waiterName=" + userName + "&deviceId=" + deviceId + "&userId=" + userID);
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(WaiterPage.this);
                    builder.setTitle("CONFIRM ACTION");
                    builder.setMessage("Edit order?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    cm = new ConnectionManager(WaiterPage.this, new Callback() {
                                        @Override
                                        public void run(Object result) {
                                            try {
                                                //  jsonArray = new JSONArray(result.toString());
                                                jsonObject = new JSONObject(result.toString());
                                                // Log.d("jsonObject = ", jsonObject.toString());
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

                    if (tableNumberTextField.getText().toString() == null|| (breakfast == 0 && lunch == 0 && dinner == 0 && dessert == 0)) {
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
                        url = new URL("http://moortala.com/services/capstone/orders/waiter/edit?orderNumber=" + orderNumber + "&tableNumber=" + tableNumber + "&breakfast=" + breakfast + "&breakfastQty=" + breakfastQty + "&lunch=" + lunch + "&lunchQty=" + lunchQty + "&dinner=" + dinner + "&dinnerQty=" + dinnerQty + "&dessert=" + dessert + "&dessertQty=" + dessertQty + "&notes=CANCELED-" + notes + "&status=" + orderStatus + "&waiterName=" + userName + "&deviceId=" + deviceId + "&userId=" + userID);

                        // Use the Builder class for convenient dialog construction
                        AlertDialog.Builder builder = new AlertDialog.Builder(WaiterPage.this);
                        builder.setTitle("CONFIRM ACTION");
                        builder.setMessage("Cancel order?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //display message that it has been canceled
                break;



            default:
                Log.d("???? ", "nothing valid selected");
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
        //quantities
        breakfastQtyEditText.setText("");
        lunchQtyEditText.setText("");
        dinnerQtyEditText.setText("");
        dessertQtyEditText.setText("");
        //clear the notes
        notesEditText = ((EditText) findViewById(R.id.notesEditText));
        notesEditText.setText("");
        //clear the status details ?
    }

    public void clearForm(View view) {
        resetFields();
    }

    //schedule a notification
    private void scheduleNotification(Notification notification, int orderId, AlarmManager alarmManager) {
        Intent notificationIntent = new Intent(this, OrderNotifier.class);
        notificationIntent.putExtra(OrderNotifier.NOTIFICATION_ID, orderId);
        notificationIntent.putExtra(OrderNotifier.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, orderId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
    }

    //get a notification
    private Notification getNotification(String content) {
        Notification noti = new Notification.Builder(this)
                .setContentTitle("ORDER(S) STATUS")
                .setContentText(content).setSmallIcon(R.mipmap.ic_launcher).build();
        return noti;
    }
}
