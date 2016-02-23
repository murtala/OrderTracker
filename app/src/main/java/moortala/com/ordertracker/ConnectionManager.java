package moortala.com.ordertracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
    //static Context context;
    private static URLConnection connection;
    private static URL url;
    private static InputStream inputStream;
    private Context context;
    private ProgressDialog dialog;
    public BufferedReader JSONData;
    private BufferedReader responseString;
    public static JSONObject jsonObject;
    Messages msg = new Messages();
    Object Activity;
    Context loginActivity;
    private static ProgressDialog pd;
    static Activity activity;


    public ConnectionManager(URL url) {
        ConnectionManager.setUrl(url);
    }

    public ConnectionManager(Context aContext, URL url) {
        context = aContext;
     //   Log.d("context",  context.getApplicationContext().toString());
        ConnectionManager.setUrl(url);
    }



  /*  public ConnectionManager(Activity anActivity, URL url) {
        ConnectionManager.setUrl(url);
       // activity=anActivity;

      //  msg = new Messages(activity);
        msg.displayProgressDialog(anActivity,"ooooooooooo");
    }*/


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
            Log.d("URL",  getUrl().toString());
            inputStream = getUrl().openConnection().getInputStream();
            Log.d("inputStream",  inputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public BufferedReader getResponseString() {
        //getInputStream();

        BufferedReader responseString = new BufferedReader(new InputStreamReader(getInputStream()));
        Log.d("responseString",  responseString.toString());
        return responseString;
    }

    public void setResponseString(BufferedReader JSONData) {
        this.responseString = JSONData;
    }

    @Override
    protected void onPreExecute() {
        System.out.println("888888888888");


       // dialog.setMessage("Doing something, please wait.");
      //  dialog.show();
      //  msg.displayProgressDialog(context,"loading");
        super.onPreExecute();

       // msg = new Messages(context);
     //   msg.displayProgressDialog("loading");
    }

    @Override
    protected String doInBackground(URL... params) {
        StringBuilder builder = new StringBuilder();
        try {
            Log.d("params[0]", params[0].toString());
            setUrl(params[0]);
            getResponseString();
            String line;
            builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Log.d("builder[0]", builder.toString());
        //    String dd = builder.toString();
           // jsonObject = new JSONObject(builder.toString());
            System.out.println("9999999");
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

        Log.d("Connection completed", "++++++");
        super.onPostExecute(string);

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


}
