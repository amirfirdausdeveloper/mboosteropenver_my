package org.app.mbooster.fragment_folder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class VerifyAddress extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    RelativeLayout submit;
    EditText verifycode;
    TextView name, address;
    private String address_id = "0";
    private Toolbar toolbar;
    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_address);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = activity = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        ToobarSetting();

        submit = (RelativeLayout) findViewById(R.id.submit);
        verifycode = (EditText) findViewById(R.id.verifycode);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        Intent intent = getIntent();
        intent.getExtras();
        address_id = intent.getStringExtra("address_id");
        Log.i("address_id", address_id);


        getAddress(address_id);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifycode.getText().toString().trim().isEmpty()) {
                    Toast.makeText(VerifyAddress.this, R.string.pleaseenterverificationcode, Toast.LENGTH_SHORT).show();
                } else {
                    VerifyCodeSubmit(address_id, verifycode.getText().toString());
                }
            }
        });
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

    private void getAddress(final String address_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(VerifyAddress.this)
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
                        name.setText(jsonObject.getString("name") + "\n" + (jsonObject.getString("contact")) + "\n" + jsonObject.getString("email") + "\n" + jsonObject.getString("ic_no"));
                        address.setText(jsonObject.getString("addr1") + "\n" + jsonObject.getString("addr2") + "\n" + jsonObject.getString("city") + ", " + jsonObject.getString("postcode")+ ", "  + jsonObject.getString("state") + "\n" + jsonObject.getString("country"));
                        if(jsonObject.getString("addr2").equals("")){
                            address.setText(jsonObject.getString("addr1") + "\n" + jsonObject.getString("city") + ", " + jsonObject.getString("postcode")+ ", "  + jsonObject.getString("state") + "\n" + jsonObject.getString("country"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getAddressNew(SavePreferences.getUserID(VerifyAddress.this), address_id);
            }
        }
        new getinfo().execute();
    }

    private void VerifyCodeSubmit(final String address_id, final String verify_code) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(VerifyAddress.this)
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
                try{
                    if(jsonObject.getString("success").equals("1")){
                        setResult(RESULT_OK);
                        finish();
                    }
                    Toast.makeText(VerifyAddress.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().verifyaddress(SavePreferences.getUserID(VerifyAddress.this), address_id, verify_code,SavePreferences.getApplanguage(VerifyAddress.this));
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
                            //getAddressFromCart();
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
