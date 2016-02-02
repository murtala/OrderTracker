package moortala.com.ordertracker;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by moortala on 2/2/2016.
 */
public class ConnectionManager {

    private static URLConnection connection;
    private static URL url;
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
            url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
