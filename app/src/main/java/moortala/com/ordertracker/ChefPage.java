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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.zip.CheckedInputStream;

public class ChefPage extends AppCompatActivity {

   /* private Spinner orderActionsSpinner;
    private String[] statuses;
    private String[] breakfasts;
    private String[] lunches;
    private String[] dinners;
    private String[] desserts;*/

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
    private List<Integer> listIds;
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
    private Button startOrderButton;
    private Button completeOrderButton;
    private ArrayList<Integer> updatedOrders;
    private ArrayList<Integer> newOrders;
    private int inProgressSize;
    private int inProgressVal;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


        statuses=new String[2];
        statuses[0]="0 - VIEW";
        statuses[1]="1 - EDIT";
       // statuses[2]="2 - COMPLETE";

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



    }

    @Override
    protected void onResume() {
        super.onResume();
        listIds = new ArrayList<Integer>();
        orderStatusTextView = (TextView) findViewById(R.id.orderStatusTextView);
        timeTakenTextView =(TextView) findViewById(R.id.timeTakenTextView);


        //start the notification service

        updatedOrders =new ArrayList<Integer>();;
        newOrders =new ArrayList<Integer>();;

        //start the notifier service
        //load data from server for orders in progress
        try {
            //  url = new URL("http://www.moortala.com/services/capstone/orders");
            url = new URL("http://moortala.com/services/capstone/orders/findNewAndUpdatedOrders");

            Log.d("URL", url.toString());
            //connect
            //  cm = new ConnectionManager(url);
            //  jsonArray = new JSONArray(cm.execute(url).get());
            ConnectionManager   cm1 = new ConnectionManager(ChefPage.this, new Callback() {
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
                                Log.d("updatedOrders ", "updatedOrders");
                                scheduleNotification(getNotification("Order(s) " + updatedOrders.toString() + " updated"), 3, alarmManager);
                                Log.d("updatedOrders2 ", "updatedOrders2");
                                //update the data

                            }
                            if (newOrders.isEmpty()){

                            }else{
                                Log.d("newOrders ", "newOrders");
                                scheduleNotification(getNotification("New order(s) " + newOrders.toString()), 4, alarmManager2);
                                Log.d("newOrders2 ", "newOrders2");
                                //update the data
                            }

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


        startOrderButton = (Button) findViewById(R.id.startOrderButton);
        completeOrderButton = (Button) findViewById(R.id.completeOrderButton);

        //get the user name from the login
        try {
            userNameTextView.setText(getIntent().getStringExtra("user"));
            userName = getIntent().getStringExtra("user");
            userID = getIntent().getLongExtra("user_id", 0);
        } catch (Exception e) {
            //   userNameTextView.setText("anonymous waiter");
            // e.printStackTrace();
        }

        //populate order status spinner

        ArrayAdapter<String> statusesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, statuses);

        orderActionsSpinner.setAdapter(statusesAdapter);
        statusesAdapter.notifyDataSetChanged();


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

                System.out.println(" status selected = " + parent.getItemAtPosition(position));
               // orderId = position;
                listIds.clear();
                if (position == 9) { //new order radio button
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
                    // resetFields();
                } else if (position == 0) { //view order radio button
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


                      //  cm = new ConnectionManager(ChefPage.this, url);

                      //  jsonArray = new JSONArray(cm.execute(url).get());
                      /*  Log.d("jsonArray = ", jsonArray.toString());
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


                } else if (position == 9) {//cancel order radio button

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
                    notesEditText.setEnabled(true);


                    //load data from server
                    try {
                        url = new URL("http://www.moortala.com/services/capstone/orders");
                        Log.d("URL", url.toString());
                        //connect

                        cm = new ConnectionManager(ChefPage.this, new Callback() {
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





                     /*   cm = new ConnectionManager(url);
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
                        adp.notifyDataSetChanged();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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


//TODO
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
                                //  jsonArray = new JSONArray(result.toString());
                                //  cm = new ConnectionManager(ChefPage.this,url);

                                // msg.displayProgressDialog(WaiterPage.this,"loading");

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

                            //    Log.d("qty", "" + od.getBreakfast_qty() + "-" + od.getLunch_qty() + "-" + od.getDinner_qty() + "-" + od.getDessert_qty());

                                breakfastQtyEditText.setText(od.getBreakfast_qty().toString());
                                lunchQtyEditText.setText(od.getLunch_qty().toString());
                                dinnerQtyEditText.setText(od.getDinner_qty().toString());
                                dessertQtyEditText.setText(od.getDessert_qty().toString());

                                //clear the notes
                                notesEditText = (EditText) findViewById(R.id.notesEditText);
                                notesEditText.setText(od.getNotes());


                                msg.displayToast(ChefPage.this, "Order loaded");

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

                   /* cm = new ConnectionManager(ChefPage.this,url);

                    // msg.displayProgressDialog(WaiterPage.this,"loading");

                    jsonObject = new JSONObject(cm.execute(url).get());
                    Log.d("jsonObject = " , jsonObject.toString());
                    msg.displayToast(ChefPage.this, "Order loaded");*/

                    //if order is found, then activate the edit button
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {

            Intent myIntent = new Intent(ChefPage.this,LoginActivity.class);
            // myIntent.putExtra("userName",email);
            ChefPage.this.startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
                //disable the edit radio button

                if (tableNumberTextField.getText().toString()==null || statusDetailEditText ==null || (breakfast==0 && lunch==0 && dinner==0 && dessert ==0)){
                    Log.d("==>","please fill required fields");
                }else {

                    //get current info from screen
                    tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                    breakfastQty =Integer.parseInt(breakfastQtyEditText.getText().toString());
                    lunchQty =Integer.parseInt(lunchQtyEditText.getText().toString());
                    dinnerQty =Integer.parseInt(dinnerQtyEditText.getText().toString());
                    dessertQty =Integer.parseInt(dessertQtyEditText.getText().toString());
                    mealNumber = MealsData.getMeal(dishNumber);
                    notes = ((EditText) findViewById(R.id.notesEditText)).getText().toString();
                    notes = notes.replaceAll("\\s+", "").trim();
                    userName = userNameTextView.getText().toString().replaceAll("\\s+", "").trim();


                    try {
//http://localhost:8080/capstone/orders/waiter/submit?tableNumber=2&breakfast=77&breakfastQty=2&lunch=777&lunchQty=22&dinner=3&dinnerQty=36&dessert=98&dessertQty=32&notes=%27coke%27&status=1&name=sampleer&deviceId=
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

                            }
                        });
                        cm.execute(url);



                       /* cm = new ConnectionManager(url);
                        jsonObject = new JSONObject(cm.execute(url).get());
                        Log.d("jsonObject = ", jsonObject.toString());*/
                        //reset fields for next order
                      //  resetFields();
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
                              //  jsonArray = new JSONArray(result.toString());
                              //  cm = new ConnectionManager(ChefPage.this,url);

                                // msg.displayProgressDialog(WaiterPage.this,"loading");

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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    cm.execute(url);

                   /* cm = new ConnectionManager(ChefPage.this,url);

                    // msg.displayProgressDialog(WaiterPage.this,"loading");

                    jsonObject = new JSONObject(cm.execute(url).get());
                    Log.d("jsonObject = " , jsonObject.toString());
                    msg.displayToast(ChefPage.this, "Order loaded");*/

                    //if order is found, then activate the edit button
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //http://moortala.com/services/capstone/orders/getOrder/12



                break;
            case 2://edit order


                try {
                    //Log.d("order id","- " + orderId);
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
                 //   Log.d("orderDetailsSpinner.getPrompt() ", ""+orderDetailsSpinner.getPrompt());



                   // orderNumber = Integer.parseInt(orderDetailsSpinner.getSelectedItem().toString());
                    //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
                    //http://localhost:8080/capstone/orders/chef/submit?&notes=DONE&status=4&name=thecook&orderNumber=3
                    url = new URL("http://moortala.com/services/capstone/orders/chef/edit?notes="+notes+"&status="+orderStatus+"&chefName="+userName+"&orderNumber="+orderId+ "&userId="+userID);
                    // url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&meal="+dishNumber+"&notes="+notes+ "&status="+orderStatus+"&name="+"&deviceId=");
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






                   /* cm = new ConnectionManager(url);
                    jsonObject = new JSONObject(cm.execute(url).get());
                    Log.d("jsonObject = " , jsonObject.toString());

                    msg = new Messages(ChefPage.this);
                    msg.displayToast(ChefPage.this, "Order Updated");*/
                } catch (Exception e) {
                    e.printStackTrace();
                }




                    break;
            case 3://cancel
                try {

                    // 1. Instantiate an AlertDialog.Builder with its constructor


//get current info from screen
                    tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                    breakfastQty =Integer.parseInt(breakfastQtyEditText.getText().toString());
                    lunchQty =Integer.parseInt(lunchQtyEditText.getText().toString());
                    dinnerQty =Integer.parseInt(dinnerQtyEditText.getText().toString());
                    dessertQty =Integer.parseInt(dessertQtyEditText.getText().toString());
                    notes = ((EditText) findViewById(R.id.notesEditText)).getText().toString();
                    notes = notes.replaceAll("\\s+", "").trim();
                    userName = userNameTextView.getText().toString().replaceAll("\\s+", "").trim();


                    //orderNumber = orderId;
                    //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    cm.execute(url);

                  /*  cm = new ConnectionManager(url);
                    jsonObject = new JSONObject(cm.execute(url).get());
                    Log.d("jsonObject = " , jsonObject.toString());

                    msg = new Messages(ChefPage.this);
                    msg.displayToast(ChefPage.this, "Order Canceled");*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //display message that it has been canceled

                // resetFields();
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






                   /* cm = new ConnectionManager(url);
                    jsonObject = new JSONObject(cm.execute(url).get());
                    Log.d("jsonObject = " , jsonObject.toString());

                    msg = new Messages(ChefPage.this);
                    msg.displayToast(ChefPage.this, "Order Completed");*/

                    //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
                    // url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes=DONE-"+notes+"&status="+orderStatus+"&name="+userName+"&deviceId="+ deviceId);
                    //  Log.d("URL",url.toString());
                    //connect
                    //   cm = new ConnectionManager(url);
                    //    jsonObject = new JSONObject(cm.execute(url).get());
                    //     Log.d("jsonObject = " , jsonObject.toString());
//
                   // msg = new Messages(ChefPage.this);
                  //  msg.displayToast(ChefPage.this, "You need to be chef/manager to complete this action");
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






                   /* cm = new ConnectionManager(url);
                    jsonObject = new JSONObject(cm.execute(url).get());
                    Log.d("jsonObject = " , jsonObject.toString());

                    msg = new Messages(ChefPage.this);
                    msg.displayToast(ChefPage.this, "Order Completed");*/

            //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
            // url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes=DONE-"+notes+"&status="+orderStatus+"&name="+userName+"&deviceId="+ deviceId);
            //  Log.d("URL",url.toString());
            //connect
            //   cm = new ConnectionManager(url);
            //    jsonObject = new JSONObject(cm.execute(url).get());
            //     Log.d("jsonObject = " , jsonObject.toString());
//
            // msg = new Messages(ChefPage.this);
            //  msg.displayToast(ChefPage.this, "You need to be chef/manager to complete this action");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startOrder(View view) {


        try {
            //Log.d("order id","- " + orderId);
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
            //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
            //http://localhost:8080/capstone/orders/chef/submit?&notes=DONE&status=4&name=thecook&orderNumber=3
            url = new URL("http://moortala.com/services/capstone/orders/chef/edit?notes="+notes+"&status=5"+"&chefName="+userName+"&orderNumber="+orderId+ "&userId="+userID);
            // url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&meal="+dishNumber+"&notes="+notes+ "&status="+orderStatus+"&name="+"&deviceId=");
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






                   /* cm = new ConnectionManager(url);
                    jsonObject = new JSONObject(cm.execute(url).get());
                    Log.d("jsonObject = " , jsonObject.toString());

                    msg = new Messages(ChefPage.this);
                    msg.displayToast(ChefPage.this, "Order Updated");*/
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    private void scheduleNotification(Notification notification, int orderId, AlarmManager alarmManager) {
        Log.d("scheduleNotification1 ", "scheduleNotification1");
        Intent notificationIntent = new Intent(this, OrderNotifier.class);
        notificationIntent.putExtra(OrderNotifier.NOTIFICATION_ID, orderId);
        notificationIntent.putExtra(OrderNotifier.NOTIFICATION, notification);

        pendingIntent = PendingIntent.getBroadcast(this, orderId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + orderId;
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        // alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        // alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        //         SystemClock.elapsedRealtime(),2*60*1000,pendingIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
        Log.d("scheduleNotification2 ", "scheduleNotification2");

    }

    public  Notification getNotification(String content) {
        Log.d("getNotification ", "getNotification");
        Notification noti = new Notification.Builder(ChefPage.this)
                .setContentTitle("ORDER(S) UPDATES")
                .setContentText(content).setSmallIcon(R.mipmap.ic_launcher).build();

       /* Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);*/
        Log.d("getNotification2 ", "getNotification2");
        return noti;
    }
}
