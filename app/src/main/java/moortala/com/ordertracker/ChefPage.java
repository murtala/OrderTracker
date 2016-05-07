package moortala.com.ordertracker;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChefPage extends AppCompatActivity {

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
    private  Spinner orderDetailsSpinner;
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
    private Button startOrderButton;
    private Button completeOrderButton;
    private ArrayList<Integer> updatedOrders;
    private ArrayList<Integer> newOrders;
    private ArrayList<Integer> canceledOrders;
    private ArrayList<Integer> inProgressOrders;
    private int inProgressSize;
    private int inProgressVal;
    private PendingIntent pendingIntent;
    private TextView ActionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        orderActionsSpinner = (Spinner) findViewById(R.id.orderActionsSpinner);
        //resources
        //name field
        userNameTextView = (TextView)findViewById(R.id.userNameTextView);
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
        submitButton =(Button)findViewById(R.id.submitButton);
        //clear button
        clearButton =(Button)findViewById(R.id.clearButton);

        statuses=new String[3];
        statuses[0]="VIEW";
        statuses[1]="EDIT";
        statuses[2]="----------";

        //set up spinner for order berakfast spinner
        breakfasts=new String[6];
        breakfasts[0]="----------";
        breakfasts[1]="1 - Omelette";
        breakfasts[2]="2 - Cereals";
        breakfasts[3]="3 - Pancakes";
        breakfasts[4]="4 - Toasts";
        breakfasts[5]="5 - Waffles";

        //set up spinner for order lunch
        lunches=new String[6];
        lunches[0]="----------";
        lunches[1]="1 - Salad";
        lunches[2]="2 - Lasagna";
        lunches[3]="3 - Fajitas";
        lunches[4]="4 - Burger";
        lunches[5]="5 - Pasta";

        //set up spinner for order dinner
        dinners=new String[6];
        dinners[0]="----------";
        dinners[1]="1 - Soup";
        dinners[2]="2 - French Fries";
        dinners[3]="3 - Chili";
        dinners[4]="4 - Crab Cake";
        dinners[5]="5 - Ciopino";

        //set up spinner for order dessert
        desserts=new String[6];
        desserts[0]="----------";
        desserts[1]="1 - Cheese Cake";
        desserts[2]="2 - Sodas";
        desserts[3]="3 - Chocolate Cake";
        desserts[4]="4 - Ice Cream";
        desserts[5]="5 - Wine";
    }

    @Override
    protected void onResume() {
        super.onResume();

        //initialize variables
        try {
            //get the user name from the login
            userNameTextView.setText(getIntent().getStringExtra("user"));
            userName = getIntent().getStringExtra("user");
            userID = getIntent().getLongExtra("user_id", 0);
            //initiate id, order status
            listIds = new ArrayList<Integer>();
            orderStatusTextView = (TextView) findViewById(R.id.orderStatusTextView);
            timeTakenTextView =(TextView) findViewById(R.id.timeTakenTextView);
            //start the notification service
            updatedOrders =new ArrayList<Integer>();;
            newOrders =new ArrayList<Integer>();;
            canceledOrders = new ArrayList<Integer>();
            inProgressOrders = new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //start the notifier service for the chef.
        try {
            url = new URL("http://moortala.com/services/capstone/orders/findNewAndUpdatedOrders");
            Log.d("URL", url.toString());
            //connect
            ConnectionManager   cm1 = new ConnectionManager(ChefPage.this, new Callback() {
                @Override
                public void run(Object result) {
                    Log.d("Orders in progress", "done" + result.toString());
                    //only interested in new and updated/canceled orders
                    try {
                        jsonArray = new JSONArray(result.toString());
                        Log.d("jsonArray = ", jsonArray.toString());
                        //get the list of all the order ids
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = jsonArray.getJSONObject(i);
                            //get edited orders
                            if (jsonobj.getInt("status") ==2){
                                updatedOrders.add(jsonobj.getInt("id"));
                            }
                            //get new orders
                            if (jsonobj.getInt("status") ==0){
                                newOrders.add(jsonobj.getInt("id"));
                            }
                            //get canceled orders
                            if (jsonobj.getInt("status") ==3){
                                canceledOrders.add(jsonobj.getInt("id"));
                            }
                            //get in progress orders
                            if (jsonobj.getInt("status") == 5) {
                                inProgressOrders.add(jsonobj.getInt("id"));
                            }
                        }

                        Log.d("updatedOrders", "" + updatedOrders.toString());
                        Log.d("newOrders", "" + newOrders.toString());
                        Log.d("canceled", "" + canceledOrders.toString());
                        inProgressSize = jsonArray.length();

                        if (inProgressSize > inProgressVal){
                            inProgressVal = inProgressSize;
                            Log.d("ProgressSize", ""+ inProgressSize);
                            Log.d("ProgressVal", ""+ inProgressVal);
                            AlarmManager alarmManager =null;
                            AlarmManager alarmManager2 =null;

                            //if any order list is empty do nothing, otherwise, start a notification for it
                            if (updatedOrders.isEmpty()){

                            }else{
                                scheduleNotification(getNotification("Order(s) " + updatedOrders.toString() + " updated"), 3, alarmManager);
                            }

                            if (newOrders.isEmpty()){
                            }else{
                                scheduleNotification(getNotification("New order(s) " + newOrders.toString()), 1, alarmManager2);
                            }

                            if (canceledOrders.isEmpty()){
                            }else{
                                scheduleNotification(getNotification("Canceled " + canceledOrders.toString()), 4, alarmManager2);
                            }

                        }else{
                            AlarmManager alarmManager3 =null;
                            scheduleNotification(getNotification(""), 2, alarmManager3);
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

        startOrderButton = (Button) findViewById(R.id.startOrderButton);
        completeOrderButton = (Button) findViewById(R.id.completeOrderButton);

        //populate order status spinner
        ArrayAdapter<String> statusesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, statuses);
        orderActionsSpinner.setAdapter(statusesAdapter);
        statusesAdapter.notifyDataSetChanged();
        orderActionsSpinner.setSelection(2);

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


        //listen to the status spinner for changes
        orderActionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println(" status selected = " + parent.getItemAtPosition(position));
               // orderId = position;
                listIds.clear();
                 if (position == 0) { //view order radio button
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
                    //since we are viewing the order, no need of the submit button
                    findViewById(R.id.submitButton).setEnabled(false);
                    startOrderButton.setEnabled(true);
                    completeOrderButton.setEnabled(true);

                    //load data from server
                     loadCurrentOrders();


                } else if (position == 1) { //edit order radio button

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
                    notesEditText.setEnabled(true);
                    //since we are editing the order, we  need of the submit button
                    findViewById(R.id.submitButton).setEnabled(true);
                    startOrderButton.setEnabled(false);
                    completeOrderButton.setEnabled(false);
                } else if (position == 2) { //complete order

                    //set order status
                    orderStatus = 4;
                    //disable spinner
                    orderDetailsSpinner.setEnabled(false);
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

        orderDetailsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(" detail item selected = " + parent.getItemAtPosition(position));
                orderId = (Integer) parent.getItemAtPosition(position);

                //clear the status display message
                orderStatusTextView.setText("");
                //clear time
                timeTakenTextView.setText("");
                //load the item selected
                try {
                    msg = new Messages(ChefPage.this);
                    orderNumber = orderId;
                    url = new URL("http://moortala.com/services/capstone/orders/getOrder/"+orderId);
                    Log.d("URL", url.toString());
                    //connect

                    cm = new ConnectionManager(ChefPage.this, new Callback() {
                        @Override
                        public void run(Object result) {
                            try {
                                jsonObject = new JSONObject(result.toString());
                                Log.d("jsonObject = ", jsonObject.toString());
                                //populate the fields
                                //table number
                                od = new OrderData(jsonObject);
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
                                //quantitites
                                breakfastQtyEditText.setText(od.getBreakfast_qty().toString());
                                lunchQtyEditText.setText(od.getLunch_qty().toString());
                                dinnerQtyEditText.setText(od.getDinner_qty().toString());
                                dessertQtyEditText.setText(od.getDessert_qty().toString());
                                //clear the notes
                                notesEditText = (EditText) findViewById(R.id.notesEditText);
                                notesEditText.setText(od.getNotes());
                                //display a message
                                msg.displayToast(ChefPage.this, "Order loaded");

                                //TODO: now
                                //change the state and text on the buttons depending on state of order
                               /* 0  - new
                                1 - view
                                2 - edit
                                3 - cancel
                                4 - complete
                                5 - in progress*/

                              /*  switch ((int) od.getStatus()){
                                    case 0:
                                        startOrderButton.setText("START ORDER");
                                        startOrderButton.setEnabled(true);
                                        completeOrderButton.setText("COMPLETE ORDER");
                                        completeOrderButton.setEnabled(true);
                                        break;
                                    case 1:
                                        startOrderButton.setEnabled(false);
                                        completeOrderButton.setEnabled(false);
                                        break;
                                    case 2:
                                        break;
                                    case 3:
                                        break;
                                    case 4:
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        break;
                                }
                             */   if (od.getStatus() == 5) {
                                    startOrderButton.setText("ORDER STARTED");
                                    startOrderButton.setEnabled(false);
                                } else if (od.getStatus() == 4){
                                    completeOrderButton.setText("ORDER COMPLETED");
                                    completeOrderButton.setEnabled(false);
                                    startOrderButton.setText("ORDER STARTED");
                                    startOrderButton.setEnabled(false);
                                }else if(od.getStatus()== 0){
                                    startOrderButton.setText("START ORDER");
                                    startOrderButton.setEnabled(true);
                                    completeOrderButton.setText("COMPLETE ORDER");
                                    completeOrderButton.setEnabled(true);
                                }
                                else {
                                    startOrderButton.setText("START ORDER");
                                    startOrderButton.setEnabled(true);
                                    completeOrderButton.setText("COMPLETE ORDER");
                                    completeOrderButton.setEnabled(true);
                                }

                                if (orderActionsSpinner.getSelectedItemPosition() == 1){
                                    findViewById(R.id.submitButton).setEnabled(true);
                                    startOrderButton.setEnabled(false);
                                    completeOrderButton.setEnabled(false);
                                }
                                //display the time taken to complete order
                                if (od.getEnd_time() == null || od.getEnd_time() == 0) {
                                } else {
                                    long minutes = (int) (((od.getEnd_time() - od.getStart_time())));
                                    timeTakenTextView = (TextView) findViewById(R.id.timeTakenTextView);
                                    timeTakenTextView.setText("" + TimeUnit.MILLISECONDS.toMinutes(minutes));
                                }



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

    private void loadCurrentOrders() {
        try {
            url = new URL("http://www.moortala.com/services/capstone/orders/findCurrentOrders");
            Log.d("URL", url.toString());
            //connect
            cm = new ConnectionManager(ChefPage.this, new Callback() {
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
                        Log.d("listIds = ", listIds.toString());
                        ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, listIds);
                        //   dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                        orderDetailsSpinner.setAdapter(adp);
                        //   orderDetailsSpinner.setSelection(orderId);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chef_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //if settings option selected
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        if (id == R.id.action_logout) {

            Intent myIntent = new Intent(ChefPage.this,LoginActivity.class);
            // myIntent.putExtra("userName",email);
            ChefPage.this.startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateOrder(View view) {

        //get data from the inputs
        //get table number
        tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
        deviceId = OrderData.getDeviceMacAddress(this);

        //chose an action to perform
        switch (orderStatus){
            case 0://new order
                //disable the edit radio button

                if (tableNumberTextField.getText().toString()==null  || (breakfast==0 && lunch==0 && dinner==0 && dessert ==0)){
                    Log.d("==>","please fill required fields");
                }else {

                    //get current info from screen
                    tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                    breakfastQty =Integer.parseInt(breakfastQtyEditText.getText().toString());
                    lunchQty =Integer.parseInt(lunchQtyEditText.getText().toString());
                    dinnerQty =Integer.parseInt(dinnerQtyEditText.getText().toString());
                    dessertQty =Integer.parseInt(dessertQtyEditText.getText().toString());
                    notes = ((EditText) findViewById(R.id.notesEditText)).getText().toString();
                    notes = notes.replaceAll("\\s+", "").trim();
                    userName = userNameTextView.getText().toString().replaceAll("\\s+", "").trim();

                    try {
                        url = new URL("http://moortala.com/services/capstone/orders/waiter/submit?tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes="+notes+"&status="+orderStatus+"&chefName="+userName+"&deviceId="+ deviceId);
                        Log.d("URL", url.toString());
                        //connect
                        cm = new ConnectionManager(ChefPage.this, new Callback() {
                            @Override
                            public void run(Object result) {
                                try {
                                    //jsonArray = new JSONArray(result.toString());
                                    jsonObject = new JSONObject(result.toString());
                                    Log.d("jsonObject = ", jsonObject.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                orderActionsSpinner.setSelection(2);
                                startOrderButton.setEnabled(false);
                                completeOrderButton.setEnabled(false);


                            }
                        });
                        cm.execute(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:  //view an order
                try {
                    msg = new Messages(ChefPage.this);
                   // orderNumber = orderId;
                    url = new URL("http://moortala.com/services/capstone/orders/getOrder/"+orderId);
                    Log.d("URL", url.toString());
                    //connect

                    cm = new ConnectionManager(ChefPage.this, new Callback() {
                        @Override
                        public void run(Object result) {
                            try {
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
                                Log.d("spinners" , ""+od.getBreakfast()+"-" + od.getLunch()+"-" + od.getDinner()+"-" + od.getDessert());
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


                                msg.displayToast(ChefPage.this, "Order loaded");
                                orderActionsSpinner.setSelection(2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    cm.execute(url);

                    //if order is found, then activate the edit button
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case 2://edit order

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
                   // orderNumber = orderId;


                    Log.d("orderId ", "" + orderId);
                    url = new URL("http://moortala.com/services/capstone/orders/chef/edit?notes="+notes+"&status="+orderStatus+"&chefName="+userName+"&orderNumber="+orderId+ "&userId="+userID);
                    Log.d("URL", url.toString());
                      // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChefPage.this);
                    builder.setTitle("CONFIRM ACTION");
                    builder.setMessage("Update order?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //connect
                                    cm = new ConnectionManager(ChefPage.this, new Callback() {
                                        @Override
                                        public void run(Object result) {
                                            try {
                                                //  jsonArray = new JSONArray(result.toString());
                                                //   cm = new ConnectionManager(url);
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
                                                //display the time taken to complete order
                                                if (od.getEnd_time() ==null || od.getEnd_time()==0){
                                                }else {
                                                    long minutes = (int) (((od.getEnd_time() - od.getStart_time()) / 1000));
                                                    timeTakenTextView = (TextView) findViewById(R.id.timeTakenTextView);
                                                    timeTakenTextView.setText("" + TimeUnit.MILLISECONDS.toMinutes(minutes));
                                                }
                                                msg = new Messages(ChefPage.this);
                                                msg.displayToast(ChefPage.this, "Order Updated");

                                                orderActionsSpinner.setSelection(2);
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

            case 3://cancel
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
                    url = new URL("http://moortala.com/services/capstone/orders/chef/update?orderNumber="+orderId+"&tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes=CANCELED-"+notes+"&status="+orderStatus+"&chefName="+userName+"&deviceId="+ deviceId);
                    Log.d("URL",url.toString());
                    //connect
                    cm = new ConnectionManager(ChefPage.this, new Callback() {
                        @Override
                        public void run(Object result) {
                            try {
                               // jsonArray = new JSONArray(result.toString());
                              //  cm = new ConnectionManager(url);
                                jsonObject = new JSONObject(result.toString());
                                Log.d("jsonObject = " , jsonObject.toString());

                                msg = new Messages(ChefPage.this);
                                msg.displayToast(ChefPage.this, "Order Canceled");

                                orderActionsSpinner.setSelection(2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    cm.execute(url);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //display message that it has been canceled
                break;

            case 4://complete order
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
                   // orderNumber = orderId;
                    orderNumber = Integer.parseInt(orderDetailsSpinner.getSelectedItem().toString());
                    url = new URL("http://moortala.com/services/capstone/orders/chef/complete?notes="+notes+"&status=4&chefName="+userName+"&orderNumber="+orderId+"&userId="+userID);
                    // url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&meal="+dishNumber+"&notes="+notes+ "&status="+orderStatus+"&name="+"&deviceId=");
                    Log.d("URL",url.toString());
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChefPage.this);
                    builder.setTitle("CONFIRM ACTION");
                    builder.setMessage("Complete order?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                     //connect
                                    cm = new ConnectionManager(ChefPage.this, new Callback() {
                                        @Override
                                        public void run(Object result) {
                                            try {
                                                //  jsonArray = new JSONArray(result.toString());
                                                //  cm = new ConnectionManager(url);
                                                jsonObject = new JSONObject(result.toString());
                                                Log.d("jsonObject = " , jsonObject.toString());

                                                msg = new Messages(ChefPage.this);
                                                msg.displayToast(ChefPage.this, "Order Completed");

                                                orderActionsSpinner.setSelection(2);
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
                //display message that it has been canceled
                break;

            default:
                ;
                Log.d("???? " , "nothing valid selected");
                break;
        }
    }

    public void completeOrder(View view) {

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
            url = new URL("http://moortala.com/services/capstone/orders/chef/complete?notes="+notes+"&status=4&chefName="+userName+"&orderNumber="+orderId+"&userId="+userID);
            // url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&meal="+dishNumber+"&notes="+notes+ "&status="+orderStatus+"&name="+"&deviceId=");
            Log.d("URL",url.toString());
            //location of the order in the list
            final int itemRemoved = listIds.indexOf(orderId);
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(ChefPage.this);
            builder.setTitle("CONFIRM ACTION");
            builder.setMessage("Complete order?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //connect
                            cm = new ConnectionManager(ChefPage.this, new Callback() {
                                @Override
                                public void run(Object result) {
                                    try {
                                        //  jsonArray = new JSONArray(result.toString());
                                        //  cm = new ConnectionManager(url);
                                        jsonObject = new JSONObject(result.toString());
                                        Log.d("jsonObject = " , jsonObject.toString());

                                        od = new OrderData(jsonObject);

                                        if (od.getStatus() == 5) {
                                            startOrderButton.setText("ORDER STARTED");
                                            startOrderButton.setEnabled(false);
                                        } else if (od.getStatus() == 4){
                                            completeOrderButton.setText("ORDER COMPLETED");
                                            completeOrderButton.setEnabled(false);
                                            startOrderButton.setText("ORDER STARTED");
                                            startOrderButton.setEnabled(false);
                                        }
                                        else {
                                            startOrderButton.setText("START ORDER");
                                            startOrderButton.setEnabled(true);
                                            completeOrderButton.setText("COMPLETE ORDER");
                                            completeOrderButton.setEnabled(true);
                                        }

                                        if (orderActionsSpinner.getSelectedItemPosition() == 1){
                                            findViewById(R.id.submitButton).setEnabled(true);
                                            startOrderButton.setEnabled(false);
                                            completeOrderButton.setEnabled(false);
                                        }


                                        //display the time taken to complete order
                                        if (od.getEnd_time() == null || od.getEnd_time() == 0) {
                                        } else {
                                            long minutes = (int) (((od.getEnd_time() - od.getStart_time()) / 1000));
                                            timeTakenTextView = (TextView) findViewById(R.id.timeTakenTextView);
                                            timeTakenTextView.setText("" + TimeUnit.MILLISECONDS.toMinutes(minutes));
                                        }
                                        msg = new Messages(ChefPage.this);
                                        msg.displayToast(ChefPage.this, "Order Completed");

                                        orderActionsSpinner.setSelection(2);
                                        startOrderButton.setEnabled(false);
                                        completeOrderButton.setEnabled(false);

                                        //reset the list of orders
                                        listIds.remove(itemRemoved);
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
    }

    public void startOrder(View view) {

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
            orderNumber = Integer.parseInt(orderDetailsSpinner.getSelectedItem().toString());
            url = new URL("http://moortala.com/services/capstone/orders/chef/edit?notes="+notes+"&status=5"+"&chefName="+userName+"&orderNumber="+orderId+ "&userId="+userID);
            Log.d("URL", url.toString());

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(ChefPage.this);
            builder.setTitle("CONFIRM ACTION");
            builder.setMessage("Start order?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //connect
                            cm = new ConnectionManager(ChefPage.this, new Callback() {
                                @Override
                                public void run(Object result) {
                                    try {
                                        //  jsonArray = new JSONArray(result.toString());
                                        //   cm = new ConnectionManager(url);
                                        jsonObject = new JSONObject(result.toString());
                                        Log.d("jsonObject = ", jsonObject.toString());
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
                                        //display the time taken to complete order
                                        if (od.getEnd_time() == null || od.getEnd_time() == 0) {
                                        } else {
                                            long minutes = (int) (((od.getEnd_time() - od.getStart_time()) / 1000));
                                            timeTakenTextView = (TextView) findViewById(R.id.timeTakenTextView);
                                            timeTakenTextView.setText("" + TimeUnit.MILLISECONDS.toMinutes(minutes));
                                        }

                                        if (od.getStatus() == 5) {
                                            startOrderButton.setText("ORDER STARTED");
                                            startOrderButton.setEnabled(false);
                                        } else if (od.getStatus() == 4){
                                            completeOrderButton.setText("ORDER COMPLETED");
                                            completeOrderButton.setEnabled(false);
                                            startOrderButton.setText("ORDER STARTED");
                                            startOrderButton.setEnabled(false);
                                        }
                                        else {
                                            startOrderButton.setText("START ORDER");
                                            startOrderButton.setEnabled(true);
                                            completeOrderButton.setText("COMPLETE ORDER");
                                            completeOrderButton.setEnabled(true);
                                        }
                                        if (orderActionsSpinner.getSelectedItemPosition() == 1){
                                            findViewById(R.id.submitButton).setEnabled(true);
                                            startOrderButton.setEnabled(false);
                                            completeOrderButton.setEnabled(false);
                                        }

                                        msg = new Messages(ChefPage.this);
                                        msg.displayToast(ChefPage.this, "Order Started");

                                        orderActionsSpinner.setSelection(2);
                                        startOrderButton.setEnabled(false);
                                        completeOrderButton.setEnabled(false);
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
    }

    private void scheduleNotification(final Notification notification, final int orderId, AlarmManager alarmManager) {
        Log.d("scheduleNotification1 ", "scheduleNotification1");
        final Intent notificationIntent = new Intent(this, OrderNotifier.class);
        notificationIntent.putExtra(OrderNotifier.NOTIFICATION_ID, orderId);
        notificationIntent.putExtra(OrderNotifier.NOTIFICATION, notification);

        pendingIntent = PendingIntent.getBroadcast(this, orderId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent.getActivity(ChefPage.this, orderId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + orderId;
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        // alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        // alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        //         SystemClock.elapsedRealtime(),2*60*1000,pendingIntent);
     //   alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);



        final Handler h = new Handler();
        final int delay = 5000; //milliseconds

        h.postDelayed(new Runnable(){
            public void run(){
                //do something
                sendBroadcast(notificationIntent);
                h.postDelayed(this, 5000);
                Log.d("delay", "run");
                PendingIntent.getBroadcast(ChefPage.this, orderId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
             /*   PendingIntent.getActivity(ChefPage.this, orderId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/



            }
        }, delay);

        Log.d("scheduleNotification2 ", "scheduleNotification2");
    }

    public  Notification getNotification(String content) {
        Log.d("getNotification ", "getNotification");
        Notification noti = new Notification.Builder(ChefPage.this).setAutoCancel(true)
                .setContentTitle("ORDER(S) STATUS")
                .setContentText(content).setSmallIcon(R.mipmap.ic_launcher).build();

        Log.d("getNotification2 ", "getNotification2");
        return noti;
    }
}