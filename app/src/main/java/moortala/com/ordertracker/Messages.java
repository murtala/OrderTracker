package moortala.com.ordertracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by moortala on 2/9/2016.
 */
public class Messages {
    private Activity activity;
    private Context context;
    private ProgressDialog progressDialog;

    public Messages(Activity activ) {
        this.activity = activ;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public boolean dialogConfirmed;

    public Messages() {

    }

    public Messages(Context contxt) {
        context = contxt;
    }

    public void displayToast(Context context, CharSequence message) {
        context = context.getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void displayProgressDialog(String message) {


        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog = progressDialog.show(context, "Processing...", message);
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void displayDialog(String message, String title) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog = progressDialog.show(context, title, message);
    }

    public boolean isDialogConfirmed() {
        return dialogConfirmed;
    }

    public void setDialogConfirmed(boolean dialogConfirmed) {
        this.dialogConfirmed = dialogConfirmed;
    }

    public void displayProgressDialog(String title, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog = progressDialog.show(context, title, message);
    }


    public void displayProgressDialog(Context waiterPage, String message) {
    }

    public void dismissProgressDialog(Context aContext) {
        Log.d("dialog", progressDialog.getOwnerActivity().getCallingActivity().getClassName().toString());
    }
}
