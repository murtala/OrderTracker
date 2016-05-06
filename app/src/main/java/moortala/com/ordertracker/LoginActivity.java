package moortala.com.ordertracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "waiter:password", "chef:password"
    };
    private JSONObject jsonObject;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private URL url;
    private int userType;
    private OrderData od;
    private String email;
    private View focusView;
    private boolean cancel;
    private Context context;
    private String password;
    private ConnectionManager cm;
    private String user;

    //  Messages msg = new Messages(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }

    private void attemptRegister() {
     /*   if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        cancel = false;
        focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
            //msg.dismissProgressDialog();
        } else {
            // Show a progress spinner, and kick off a background task to perform the user login attempt.

            //determine if the user is waiter of chef
            if (userType == 0) { //waiter
                try {
                    url = new URL("http://moortala.com/services/capstone/users/findUser/" + email);
                    Log.d("get user details  URL", url.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (userType == 1) { //chef
                try {
                    url = new URL("http://moortala.com/services/capstone/users/findUser/" + email);
                    Log.d("get user details  URL", url.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // set up url
            //get user details from server
            //String user =null;
            cm = new ConnectionManager(LoginActivity.this, new Callback() {
                @Override
                public void run(Object result) {
                    try {
                        try {
                            jsonObject = new JSONObject(result.toString());
                            od = new OrderData(jsonObject);
                            if (userType == 0) { //waiter
                                user = od.getUser();
                            } else if (userType == 1) { //chef
                                user = od.getUser();
                            }

                        } catch (JSONException e) {
                            user = null;
                            e.printStackTrace();
                        }
                        // Log.d("jsonObject = " , jsonObject.toString());

                        //check if name matches naesm from database
                        if (user != null && user.equalsIgnoreCase(email)) {
                            //  showProgress(false);
                            mEmailView.setError("User already exists");
                            focusView = mEmailView;
                            cancel = true;
                        } else { //if name is null, then user does not exist, we can submit registration now
                            //by submitting blank order
                            // set up url
                            context = LoginActivity.this;

                            context = getApplicationContext();
                            CharSequence text = "User created. Loggin in...";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            try {
                                url = new URL("http://moortala.com/services/capstone/users/create?userName=" + email + "&password=" + password + "&userType=" + userType);
                                Log.d("register URL", url.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //get user details from server
                            cm = new ConnectionManager(LoginActivity.this, new Callback() {
                                @Override
                                public void run(Object result) {
                                    try {
                                        jsonObject = new JSONObject(result.toString());
                                        context = getApplicationContext();
                                        CharSequence text = "User created. Please login";
                                        int duration = Toast.LENGTH_LONG;

                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            cm.execute(url);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            cm.execute(url);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
      /*  if (mAuthTask != null) {
            return;
        }*/
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        cancel = false;
        focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to perform the user login attempt.

            try {
                url = new URL("http://moortala.com/services/capstone/users/findUser/" + email);
                Log.d("get user details  URL", url.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ConnectionManager cm = new ConnectionManager(LoginActivity.this, new Callback() {
                @Override
                public void run(Object result) {
                    try {
                        jsonObject = new JSONObject(result.toString());
                        Log.d("jsonObject = ", jsonObject.toString());

                        od = new OrderData(jsonObject);
                        String user = null;

                        if (userType == 0) { //waiter
                            user = od.getUser();
                        } else if (userType == 1) { //chef
                            user = od.getUser();
                        }


                        if (user != null && user.equalsIgnoreCase(email)) {
                            //  showProgress(false);
                            if (od.getPassword().equalsIgnoreCase(password)) {

                                if (userType == 0 && od.getUser_type() == 0) {
                                    Intent myIntent = new Intent(LoginActivity.this, WaiterPage.class);
                                    myIntent.putExtra("user_id", od.getId());
                                    myIntent.putExtra("user", od.getUser());
                                    LoginActivity.this.startActivity(myIntent);
                                } else if (userType == 1 && od.getUser_type() == 1) {
                                    Intent myIntent = new Intent(LoginActivity.this, ChefPage.class);
                                    myIntent.putExtra("user_id", od.getId());
                                    myIntent.putExtra("user", od.getUser());
                                    LoginActivity.this.startActivity(myIntent);
                                } else {

                                    Context context = getApplicationContext();
                                    CharSequence text = "Invalid type";
                                    int duration = Toast.LENGTH_LONG;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }

                            } else {
                                //  showProgress(false);
                                mPasswordView.setError("Invalid password");
                                focusView = mPasswordView;
                                cancel = true;
                                focusView.requestFocus();
                            }

                        } else {
                            // showProgress(false);
                            mEmailView.setError("Please register this user");
                            focusView = mEmailView;
                            cancel = true;
                            focusView.requestFocus();
                        }


                    } catch (Exception e) {

                        mEmailView.setError("user not found");
                        focusView = mEmailView;
                        cancel = true;
                        focusView.requestFocus();

                        e.printStackTrace();
                    }

                }
            });
            cm.execute(url);
        }
    }

    private boolean isEmailValid(String email) {
        //
        return email.length() > 3;
        //return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //
        return password.length() > 3;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //   emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public void userType(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.waiterRadioButton:
                if (checked)
                    userType = 0;
                break;
            case R.id.chefRadioButton:
                if (checked)
                    userType = 1;
                break;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // attempt authentication against a network service.

            // get the credentials by accessing network
            //if name is null then it does not exist. other wise, prompt to choose another name

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // register the new account here.
            //true will alwys go and log in
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            // showProgress(false);

            if (success) {
                finish();

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

