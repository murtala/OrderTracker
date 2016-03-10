package moortala.com.ordertracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by moortala on 2/2/2016.
 */
public class ConnectionManager extends AsyncTask<URL, Void, String> {

    Callback callback;
    //static Context context;
    private static URLConnection connection;
    private static URL url;
    private static InputStream inputStream;
    private Context context;
    private ProgressDialog dialog;
    public BufferedReader JSONData;
    private BufferedReader responseString;
    public static JSONObject jsonObject;
    Messages msg;
    Object Activity;
    Context loginActivity;
    private static ProgressDialog pd;
    static Activity activity;

    boolean running;
    ProgressDialog progressDialog;
    private JSONArray jsonArray;


    public ConnectionManager(URL url) {
        ConnectionManager.setUrl(url);
    }

    public ConnectionManager(Context aContext, URL url) {
        context = aContext;
     //   Log.d("context",  context.getApplicationContext().toString());
        ConnectionManager.setUrl(url);
    }



    public ConnectionManager(WaiterPage waiterPage, URL url) {
        msg = new Messages(waiterPage);

       // this.callback = callbck;
        ConnectionManager.setUrl(url);

    }


  /* public ConnectionManager(Activity anActivity, URL url) {
        ConnectionManager.setUrl(url);

       dialog = new ProgressDialog(anActivity);
       // activity=anActivity;

      //  msg = new Messages(activity);
     //   msg.displayProgressDialog(anActivity,"ooooooooooo");
    }

    public ConnectionManager(Callback callback) {

    }*/

    public ConnectionManager(Context contxt, Callback callbck) {

       msg = new Messages(contxt);

        this.callback = callbck;
      /*  dialog = new ProgressDialog(contxt);
        // progressDialog.setTitle("Processing...");
        //  progressDialog.setMessage("Please wait.");
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);
        dialog =  dialog.show(contxt, "Processing...", "<><><>");*/
    }

    public ConnectionManager(Callback callbck) {

  //      msg = new Messages(contxt);

        this.callback = callbck;
      /*  dialog = new ProgressDialog(contxt);
        // progressDialog.setTitle("Processing...");
        //  progressDialog.setMessage("Please wait.");
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);
        dialog =  dialog.show(contxt, "Processing...", "<><><>");*/
    }



    //get the connection
    public static URLConnection getConnection() {
        return connection;
    }

    //sets the connection
    public static void setConnection(URLConnection connection) {
        ConnectionManager.connection = connection;
    }

    //returns the url
    public static URL getUrl() {
        return url;
    }

    //sets the url
    public static void setUrl(URL url) {
        ConnectionManager.url = url;
    }

    //open a connnection
    public static void openConnection() {
        try {
            ConnectionManager.connection= url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getInputStream() {
        try {
            Log.d("input stream ",  getUrl().toString());
            inputStream = getUrl().openConnection().getInputStream();
        //    Log.d("inputStream",  inputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public BufferedReader getResponseString() {
        //getInputStream();

        BufferedReader responseString = new BufferedReader(new InputStreamReader(getInputStream()));
      //  Log.d("responseString",  responseString.toString());
        return responseString;
    }

    public void setResponseString(BufferedReader JSONData) {
        this.responseString = JSONData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("on pre excecute");
        try {
            msg.displayProgressDialog("", "loading...");
        } catch (Exception e) {
          //  e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(URL... params) {
        StringBuilder builder = new StringBuilder();
        try {
            Log.d("in back ground- url: ", params[0].toString());
            setUrl(params[0]);
          //  getResponseString();
            String line;
            builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Log.d("in bg - string data" , builder.toString());
          //  setJsonArray( new JSONArray(builder.toString()));
        //    String dd = builder.toString();
           // jsonObject = new JSONObject(builder.toString());
         //   System.out.println("in background");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  builder.toString();
    }

    @Override
    protected void onPostExecute(String string) {




       // OrderData od = new OrderData(this);
        //  OrderData.getOrder.getOrderStatus();
      //  od.getOrderStatus();
   //     pd.dismiss();
       /* try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
     //   msg.dismissProgressDialog();
    /*    if (dialog.isShowing()) {
            dialog.dismiss();
        }*/

       // Log.d("Connection completed", "++++++");
        super.onPostExecute(string);

        System.out.println("in onPostExecute");

        callback.run(string);

    /*    if (dialog.isShowing()) {
           System.out.println("dismissing the dialog");
          dialog.dismiss();
        }*/

        try {
            msg.dismissProgressDialog();
        } catch (Exception e) {
          //  e.printStackTrace();
        }


     /*   try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        if(msg.getProgressDialog() != null && msg.getProgressDialog().isShowing())
        {
            msg.getProgressDialog().dismiss();
        }*/


     //   msg.dismissProgressDialog();
    }

    public static JSONObject getJsonObject() {
        return jsonObject;
    }

    public static void setJsonObject(JSONObject jsonObject) {
        ConnectionManager.jsonObject = jsonObject;
    }


    public ConnectionManager(Activity activity) {
        dialog = new ProgressDialog(activity);
    }


    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
