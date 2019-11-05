package org.app.mbooster.shopping_mall;

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
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.RegisterActivity;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddVoucher extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{
    private EditText shipname, shipcontact, shipemail,shipicno;
    private AppCompatSpinner spevoucher;
    private EditText saddr1, scity, scountry;
    private AutoCompleteTextView spostcode;
    private TextView toolbar_title;

    private RelativeLayout submit;

    private Toolbar toolbar;

    private String eVoucherload;

    private TextView tvsubmit;

    private ArrayList<eVoucher> eVoucher_list = new ArrayList<>();
    private ArrayList<String> eVoucher_list_str = new ArrayList<>();
    private ArrayList<String> eVoucher_list_str2 = new ArrayList<>();


    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;
    private AlertDialog alertDialog;

    private String cart_id = "0";
    private String product_id = "0";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = activity = this;


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        ToobarSetting();

        Intent i = getIntent();
        i.getExtras();
        cart_id = i.getStringExtra("cart_id");
        product_id = i.getStringExtra("product_id");

        eVoucherload = "";

        toolbar_title  =(TextView) findViewById(R.id.toolbar_title);

        tvsubmit = (TextView) findViewById(R.id.tvsubmit);
        spevoucher = (AppCompatSpinner) findViewById(R.id.spevoucher);

        submit = (RelativeLayout) findViewById(R.id.submit);
        Helper.buttonEffect(submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("109ccc", eVoucher_list_str.get(spevoucher.getSelectedItemPosition()));
                updateEVoucher(eVoucher_list_str.get(spevoucher.getSelectedItemPosition()));

            }
        });

        getEVoucher();

        toolbar_title.setText(R.string.voucher);
        Helper.CheckMaintenance(this);
    }

    public void updateEVoucher(final String voucher_code) {
        Log.i("147ccc", "updateEVoucher inside");
        Log.i("147voucher_code", voucher_code);
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(AddVoucher.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();

                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                Log.i("166ccc", "updateEVoucher onPostExecute inside");
                super.onPostExecute(jsonObject);
                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        setResult(RESULT_OK);
                        finish();
                        Log.i("jsonObject is 1 ok", jsonObject.getString("success"));
                    }else{
                        Log.i("jsonObject is else ok", "else no ok");
                    }
                    Toast.makeText(AddVoucher.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().updateEvoucher(SavePreferences.getUserID(AddVoucher.this), voucher_code, cart_id, product_id);

            }
        }
        new getinfo().execute();
    }

    private void getEVoucher() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                eVoucher_list.clear();
                eVoucher_list_str.clear();
                eVoucher_list_str2.clear();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    JSONArray array = jsonObject.getJSONArray("eVoucher");

                    Log.i("varray", jsonObject.toString());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        eVoucher_list.add(new eVoucher(json.getString("voucher_id"), json.getString("voucher_code_display")));
                        eVoucher_list_str.add(json.getString("voucher_code"));

                        eVoucher_list_str2.add(json.getString("voucher_code_display"));
                    }

                    ArrayAdapter<String> eVoucherList = new ArrayAdapter<>(AddVoucher.this, android.R.layout.simple_spinner_dropdown_item, eVoucher_list_str2);
                    eVoucherList.setDropDownViewResource(R.layout.spinner_item_divider2);
//                    stateList.setDropDownViewTheme();
                    spevoucher.setAdapter(eVoucherList);
                    spevoucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String id = eVoucher_list.get(i).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    if (!eVoucherload.equals("")) {
                        spevoucher.setSelection(eVoucher_list_str.indexOf(eVoucherload));
                        eVoucherload = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getAllEvoucher(SavePreferences.getUserID(AddVoucher.this), cart_id);
            }
        }
        new getinfo().execute();
    }


    private class eVoucher {
        String id;
        String vcode;

        public eVoucher(String id, String vcode) {
            this.id = id;
            this.vcode = vcode;
        }

        public String getVcode() {
            return vcode;
        }

        public String getId() {
            return id;
        }

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
