package moortala.com.ordertracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    private Spinner orderStatusSpinner;
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

    @Override
    protected void onResume() {

        //get the user name from the login
        try {
            userNameTextView.setText(getIntent().getStringExtra("userName"));
        } catch (Exception e) {
         //   userNameTextView.setText("anonymous waiter");
           // e.printStackTrace();
        }
        listIds = new ArrayList<Integer>();


        //populate order status spinner

        ArrayAdapter<String> statusesAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, statuses);

        orderStatusSpinner.setAdapter(statusesAdapter);
        statusesAdapter.notifyDataSetChanged();

        orderStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("status selected = ", (String) parent.getItemAtPosition(position));
                orderStatus = (Integer) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //populate breakfast spinner
        ArrayAdapter<String> breakfastsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, breakfasts);
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
        ArrayAdapter<String> lunchesAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, lunches);
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
        ArrayAdapter<String> dinnersAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dinners);
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
        ArrayAdapter<String> dessertsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, desserts);
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
        orderStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println(" status selected = " + parent.getItemAtPosition(position));
                orderId = position;
                if (position == 0) { //new order radio button
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
                } else if (position == 1) { //view order radio button
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
                        url = new URL("http://www.moortala.com/services/capstone/orders");
                        Log.d("URL", url.toString());
                        //connect
                        cm = new ConnectionManager(WaiterPage.this,url);

                        jsonArray = new JSONArray(cm.execute(url).get());
                        Log.d("jsonArray = ", jsonArray.toString());
                        //get the list of all the order ids

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = jsonArray.getJSONObject(i);
                            //insert the list to the spinner
                            listIds.add(jsonobj.getInt("id"));
                        }
                        Log.d("listIds = ", listIds.toString());


                        ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listIds);
                        //   dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        orderDetailsSpinner.setAdapter(adp);
                        orderDetailsSpinner.setSelection(orderId);
                        adp.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (position == 2) { //edit order radio button

                    //set order status
                    orderStatus = 2;
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


                } else if (position == 3) {//cancel order radio button

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
                        cm = new ConnectionManager(url);
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


                        ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listIds);
                        //   dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        orderDetailsSpinner.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                else if (position == 4) { //edit order radio button

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

        //spinner to get the value of current orders



        orderDetailsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(" item selected = " + parent.getItemAtPosition(position));
                orderId = (Integer) parent.getItemAtPosition(position);

               // Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //resources
        //name field
        userNameTextView = (TextView)findViewById(R.id.userNameTextView);
        //table number
        tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
        //order detail spinner
        orderDetailsSpinner = (Spinner) findViewById(R.id.orderDetailsSpinner);
        //order status spinner
        orderStatusSpinner = (Spinner) findViewById(R.id.orderStatusSpinner);
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
        statuses=new String[4];
        statuses[0]="0 - NEW";
        statuses[1]="1 - VIEW";
        statuses[2]="2 - EDIT";
        statuses[3]="3 - CANCEL";



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
                        userName = userNameTextView.getText().toString();


                        try {
//http://localhost:8080/capstone/orders/waiter/submit?tableNumber=2&breakfast=77&breakfastQty=2&lunch=777&lunchQty=22&dinner=3&dinnerQty=36&dessert=98&dessertQty=32&notes=%27coke%27&status=1&name=sampleer&deviceId=
                            url = new URL("http://moortala.com/services/capstone/orders/waiter/submit?tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes="+notes+"&status="+orderStatus+"&name="+userName+"&deviceId="+ deviceId);
                            Log.d("URL", url.toString());
                            //connect
                            cm = new ConnectionManager(url);
                            jsonObject = new JSONObject(cm.execute(url).get());
                            Log.d("jsonObject = ", jsonObject.toString());
                            //reset fields for next order
                            resetFields();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:  //view an order
                    try {
                        msg = new Messages(WaiterPage.this);
                        orderNumber = orderId;
                        url = new URL("http://moortala.com/services/capstone/orders/getOrder/"+orderNumber);
                        Log.d("URL",url.toString());
                        //connect
                        cm = new ConnectionManager(WaiterPage.this,url);

                       // msg.displayProgressDialog(WaiterPage.this,"loading");

                        jsonObject = new JSONObject(cm.execute(url).get());
                        Log.d("jsonObject = " , jsonObject.toString());
                        msg.displayToast(WaiterPage.this, "Order loaded");

                        //if order is found, then activate the edit button
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //http://moortala.com/services/capstone/orders/getOrder/12

                    //populate the fields
                    //table number
                    OrderData od = new OrderData(jsonObject);
                    tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
                    tableNumberTextField.setText(Long.toString(od.getTableNumber()));

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
                        userName = userNameTextView.getText().toString();


                        orderNumber = orderId;
                        //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
                        url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes="+notes+"&status="+orderStatus+"&name="+userName+"&deviceId="+ deviceId);
                       // url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&meal="+dishNumber+"&notes="+notes+ "&status="+orderStatus+"&name="+"&deviceId=");
                        Log.d("URL",url.toString());
                        //connect
                        cm = new ConnectionManager(url);
                        jsonObject = new JSONObject(cm.execute(url).get());
                        Log.d("jsonObject = " , jsonObject.toString());

                        msg = new Messages(WaiterPage.this);
                        msg.displayToast(WaiterPage.this, "Order Updated");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //populate the fields
                    //table number
                    od = new OrderData(jsonObject);
                    tableNumberTextField = (EditText) findViewById(R.id.tableNumberTextField);
                    tableNumberTextField.setText(Long.toString(od.getTableNumber()));

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
                        userName = userNameTextView.getText().toString();


                        orderNumber = orderId;
                        //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
                        url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes=CANCELED-"+notes+"&status="+orderStatus+"&name="+userName+"&deviceId="+ deviceId);
                        Log.d("URL",url.toString());
                        //connect
                        cm = new ConnectionManager(url);
                        jsonObject = new JSONObject(cm.execute(url).get());
                        Log.d("jsonObject = " , jsonObject.toString());

                        msg = new Messages(WaiterPage.this);
                        msg.displayToast(WaiterPage.this, "Order Canceled");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //display message that it has been canceled

                   // resetFields();
                    break;

                /*case 4://done
                    try {

                        //get current info from screen
                        tableNumber = Integer.parseInt(tableNumberTextField.getText().toString());
                        breakfastQty =Integer.parseInt(breakfastQtyEditText.getText().toString());
                        lunchQty =Integer.parseInt(lunchQtyEditText.getText().toString());
                        dinnerQty =Integer.parseInt(dinnerQtyEditText.getText().toString());
                        dessertQty =Integer.parseInt(dessertQtyEditText.getText().toString());
                        notes = ((EditText) findViewById(R.id.notesEditText)).getText().toString();
                        userName = userNameTextView.getText().toString();


                        orderNumber = orderId;
                        //http://localhost:8080/capstone/orders/submit?tableNumber=2&meal=12&notes=%27coke%27&status=1&name=bob&deviceId=2w3e4r
                        url = new URL("http://moortala.com/services/capstone/orders/update?orderNumber="+orderNumber+"&tableNumber="+tableNumber+"&breakfast="+breakfast+"&breakfastQty="+breakfastQty+"&lunch="+lunch+"&lunchQty="+lunchQty+"&dinner="+dinner+"&dinnerQty="+dinnerQty+"&dessert="+dessert+"&dessertQty="+dessertQty+"&notes=DONE-"+notes+"&status="+orderStatus+"&name="+userName+"&deviceId="+ deviceId);
                        Log.d("URL",url.toString());
                        //connect
                        cm = new ConnectionManager(url);
                        jsonObject = new JSONObject(cm.execute(url).get());
                        Log.d("jsonObject = " , jsonObject.toString());

                        msg = new Messages(WaiterPage.this);
                        msg.displayToast(WaiterPage.this, "Order Completed");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //display message that it has been canceled

                  //  resetFields();
                    break;*/

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
        orderStatusSpinner.setSelection(0);
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
}
