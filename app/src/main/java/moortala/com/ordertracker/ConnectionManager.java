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
        ConnectionManager.setUrl(url);
    }

    public ConnectionManager(WaiterPage waiterPage, URL url) {
        msg = new Messages(waiterPage);
        ConnectionManager.setUrl(url);
    }

    public ConnectionManager(Context contxt, Callback callbck) {
       msg = new Messages(contxt);
        this.callback = callbck;
    }

    public ConnectionManager(Callback callbck) {
        this.callback = callbck;
    }

    //get the connection
    public static URLConnection getConnection() {
        return connection;
    }

    public ConnectionManager(Activity activity) {
        dialog = new ProgressDialog(activity);
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
            Log.d("in bg - string data", builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  builder.toString();
    }

    @Override
    protected void onPostExecute(String string) {

        super.onPostExecute(string);

        System.out.println("in onPostExecute");

        callback.run(string);

        try {
            msg.dismissProgressDialog();
        } catch (Exception e) {
          //  e.printStackTrace();
        }
    }

    public static JSONObject getJsonObject() {
        return jsonObject;
    }

    public static void setJsonObject(JSONObject jsonObject) {
        ConnectionManager.jsonObject = jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
