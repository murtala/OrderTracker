package moortala.com.ordertracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;

import java.net.MalformedURLException;
import java.net.URL;

public class WaiterPage extends AppCompatActivity {

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

        String[] values=new String[3];
        values[0]="1 - Salad";
        values[1]="2 - Meat";
        values[2]="3 - Fruits";

        NumberPicker np=
                (NumberPicker) findViewById(R.id.mealNumberPicker);
        np.setMaxValue(values.length - 1);
        np.setMinValue(0);
        np.setDisplayedValues(values);
       // np.setMaxValue(9);
       // np.setMinValue(0);
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
        OrderData.getOrderStatus();
        OrderData.getTableNumber();
        OrderData.getNotes();

        //send the request
        String ip = null;
        URL url = null; //&key=ABQIAAAADxhJjHRvoeM2WF3nxP5rCBRcGWwHZ9XQzXD3SWg04vbBlJ3EWxR0b0NVPhZ4xmhQVm3uUBvvRF-VAA&userip=192.168.0.172");
        try {
            url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" + "v=1.0&q=" + "searchQueries.get(i)" + "&rsz=8&imgsz="+"imageSize"+"&hl=" + "currentLocale" + "&userip=" +ip + "&safe=active");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ConnectionManager.setUrl(url);
        ConnectionManager.openConnection();
        // use json handler to get the data
    }
}
