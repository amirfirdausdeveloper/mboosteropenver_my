package org.app.mbooster.activity_folder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.Helper.JSONParser;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONException;
import org.json.JSONObject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyProfileEdit extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private Toolbar toolbar;

    private EditText name, email, phnumber;
    private RelativeLayout submit;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phnumber = (EditText) findViewById(R.id.phnumber);
        submit = (RelativeLayout) findViewById(R.id.submit);

        ToobarSetting();

        context = activity = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Helper.buttonEffect(submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().length() == 0 || email.getText().toString().trim().length() == 0 || phnumber.getText().toString().trim().length() == 0) {
                    if (SavePreferences.getApplanguage(MyProfileEdit.this).equals("SCN")) {
                        Toast.makeText(MyProfileEdit.this, "请填入所有空格", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyProfileEdit.this, "Please fill in all blanks", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (Helper.isValidEmail(email.getText().toString())) {
                        update_profile(name.getText().toString(), email.getText().toString(), phnumber.getText().toString());
                    } else {
                        if (SavePreferences.getApplanguage(MyProfileEdit.this).equals("SCN")) {
                            Toast.makeText(MyProfileEdit.this, "电邮无效", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyProfileEdit.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        if (SavePreferences.getUserType(this).equals("0")) {
            name.setEnabled(false);
            phnumber.setEnabled(false);
            email.setEnabled(false);
            name.setText(SavePreferences.getMMSPOTNAME(this));
            phnumber.setText(SavePreferences.getMMSPOTCONTACT(this));
            email.setText(SavePreferences.getMMSPOTEMAIL(this));
        }

        Helper.CheckMaintenance(this);

        profileImage(SavePreferences.getUserID(MyProfileEdit.this));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void ToobarSetting() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);

        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //********************************************************************************************************************************************************//
    //load ProfileFragment function part
    //********************************************************************************************************************************************************//

    public void profileImage(final String id) {

        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {

            String data = new urlLink().submitgetUserData(context);
            String response;
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(MyProfileEdit.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jsonObject = new JSONParser().getJSONFromUrl(new urlLink().URLgetUser, data);

                Log.i("json", jsonObject.toString());
                return jsonObject;

            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                Log.i("Running onpost", jsonObject.toString());
                try {

                    String companyname = jsonObject.getString("user_username");
                    String companyemail = jsonObject.getString("user_email");
                    String companyphone = jsonObject.getString("user_contact_tel");

                    name.setText(companyname);
                    email.setText(companyemail);
                    phnumber.setText(companyphone);


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }


        }

        new AsyncTaskRunner().execute();
    }

    public void update_profile(final String name, final String email, final String phnumber) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(MyProfileEdit.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        finish();
                    }
                    Toast.makeText(MyProfileEdit.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().updateProfile(SavePreferences.getUserID(MyProfileEdit.this), name, email, phnumber, SavePreferences.getApplanguage(MyProfileEdit.this));
            }
        }
        new getinfo().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            this.unregisterReceiver(networkStateReceiver);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    public void networkAvailable() {
        network = 1;


    }

    @Override
    public void networkUnavailable() {

        network = 0;
        alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.no_network_notification))
                .setCancelable(false)

                .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline() == false) {
                            networkUnavailable();
                        } else {
                            dialog.dismiss();
                            profileImage(SavePreferences.getUserID(MyProfileEdit.this));
                        }

                    }
                })
                .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).finish();
                    }
                })
                .create();

        if (network == 0) {

            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }


    public boolean isOnline() {


        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) {

            return false;
        }

    }
}
