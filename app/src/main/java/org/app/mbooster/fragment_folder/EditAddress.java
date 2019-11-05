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
import android.os.Handler;
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

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.RegisterActivity;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditAddress extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{
    private EditText shipname, shipcontact, shipemail,shipicno;
//    private EditText spstate;
    private EditText saddr1, scity, scountry;
    private TextView toolbar_title;
    SearchableSpinner spstate,spostcode;

    private RelativeLayout submit;

    private Toolbar toolbar;

    private String stateload, postcodeload;

    private TextView tvsubmit;

    private ArrayList<State> state_list = new ArrayList<>();
    private ArrayList<String> state_list_str = new ArrayList<>();

    private ArrayList<City> city_list = new ArrayList<>();
    private ArrayList<String> city_list_str = new ArrayList<>();

    private ArrayList<Postcode> postcode_list = new ArrayList<>();
    private ArrayList<String> postcode_list_str = new ArrayList<>();

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;
    private AlertDialog alertDialog;

    private String address_id = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        context = activity = this;


        LogHelper.debug("[EditAddress]");

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        ToobarSetting();
//        getAddressFromCart();

        LogHelper.debug("[pageOpen] EditAddress");

        Intent intent = getIntent();
        intent.getExtras();
        address_id = intent.getStringExtra("address_id");
        Log.i("address_id", address_id);

        stateload = "";

        toolbar_title  =(TextView) findViewById(R.id.toolbar_title);

        tvsubmit = (TextView) findViewById(R.id.tvsubmit);
        shipname = (EditText) findViewById(R.id.shipname);
        shipcontact = (EditText) findViewById(R.id.shipcontact);
        spstate = findViewById(R.id.spstate);
        spostcode = findViewById(R.id.spostcode);

        spstate.setTitle(getString(R.string.state));
        spostcode.setTitle(getString(R.string.postcode));

        saddr1 = (EditText) findViewById(R.id.saddr1);
//        saddr2 = (EditText) findViewById(R.id.saddr2);
        scountry = (EditText) findViewById(R.id.scountry);
        shipemail = (EditText) findViewById(R.id.shipemail);
        shipicno = (EditText) findViewById(R.id.icno);
        scity = (EditText) findViewById(R.id.scity);

        submit = (RelativeLayout) findViewById(R.id.submit);
        Helper.buttonEffect(submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shipname.getText().toString().trim().length() == 0 ) {
                    String error_msg = getString( R.string.please_fill_in)+ getString(R.string.name);
                    Toast.makeText(EditAddress.this, error_msg, Toast.LENGTH_SHORT).show();
                    shipname.requestFocus();
                } else if(shipcontact.getText().toString().trim().length() == 0){
                    String error_msg = getString( R.string.please_fill_in)+ getString(R.string.contact);
                    Toast.makeText(EditAddress.this, error_msg, Toast.LENGTH_SHORT).show();
                    shipcontact.requestFocus();
                }else if(shipemail.getText().toString().trim().length() == 0){
                    String error_msg = getString( R.string.please_fill_in)+ getString(R.string.email);
                    Toast.makeText(EditAddress.this, error_msg, Toast.LENGTH_SHORT).show();
                    shipemail.requestFocus();
                }else if(!Helper.isValidEmail(shipemail.getText().toString())){
                    Toast.makeText(EditAddress.this, R.string.please_enter_a_valid_email, Toast.LENGTH_SHORT).show();
                    shipemail.requestFocus();
                }else if(shipicno.getText().toString().trim().length() == 0){
                    String error_msg = getString( R.string.please_fill_in)+ getString(R.string.ic_no);
                    Toast.makeText(EditAddress.this, error_msg, Toast.LENGTH_SHORT).show();
                    shipicno.requestFocus();
                }else if(saddr1.getText().toString().trim().length() == 0){
                    String error_msg = getString( R.string.please_fill_in)+ getString(R.string.address_1);
                    Toast.makeText(EditAddress.this, error_msg, Toast.LENGTH_SHORT).show();
                    saddr1.requestFocus();
                }else if(scity.getText().toString().trim().length() == 0){
                    String error_msg = getString( R.string.please_fill_in)+ getString(R.string.city);
                    Toast.makeText(EditAddress.this, error_msg, Toast.LENGTH_SHORT).show();
                    scity.requestFocus();
                }
//                else if(spostcode.getText().toString().trim().length() == 0){
//                    String error_msg = getString( R.string.please_fill_in)+ getString(R.string.postcode);
//                    Toast.makeText(EditAddress.this, error_msg, Toast.LENGTH_SHORT).show();
//                    spostcode.requestFocus();
//                }
                else if(scountry.getText().toString().trim().length() == 0){
                    String error_msg = getString( R.string.please_fill_in)+ getString(R.string.country);
                    Toast.makeText(EditAddress.this, error_msg, Toast.LENGTH_SHORT).show();
                    scountry.requestFocus();
                } else{
                    updateAddr(shipname.getText().toString(), shipcontact.getText().toString(), shipemail.getText().toString(), shipicno.getText().toString(), scity.getText().toString()
                            ,state_list_str.get(spstate.getSelectedItemPosition()),//state_list_str.get(spstate.getSelectedItemPosition()),
                            postcode_list_str.get(spostcode.getSelectedItemPosition()), saddr1.getText().toString(), scountry.getText().toString(), address_id);
                }
            }
        });


        if (!address_id.equals("0")){
            getAddress(address_id);
            toolbar_title.setText(R.string.edit_address);
        }else{
            getState();
            getPostcodeList();
            toolbar_title.setText(R.string.add_address);
        }
        Helper.CheckMaintenance(this);

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

    public void updateAddr(final String shipname, final String shipcontact, final String shipemail,final String shipicno, final String shipcity,final String shipstate, final String shippostcode, final String shipaddr1, final String shipcountry, final String address_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(EditAddress.this)
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
                        setResult(RESULT_OK);
                        finish();
                    }
                    Toast.makeText(EditAddress.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().updateAddAddress(SavePreferences.getUserID(EditAddress.this),address_id, shipname, shipcontact, shipemail, shipicno, shipcity,shipstate,
                        shippostcode, shipaddr1, shipcountry,SavePreferences.getApplanguage(EditAddress.this));

            }
        }
        new getinfo().execute();
    }

    private void getState() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                state_list.clear();
                state_list_str.clear();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    JSONArray array = jsonObject.getJSONArray("state");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        state_list.add(new State(json.getString("ssid"), json.getString("state")));
                        state_list_str.add(json.getString("state"));
                    }


                    ArrayAdapter<String> stateList = new ArrayAdapter<>(EditAddress.this, android.R.layout.simple_spinner_dropdown_item, state_list_str);
//                    stateList.setDropDownViewResource(R.layout.spinner_item_divider2);
//                    stateList.setDropDownViewTheme();
                    spstate.setAdapter(stateList);
//                    spstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            city_list.clear();
//                            city_list_str.clear();
//                            String id = state_list.get(i).getId();
//                        }
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//                        }
//                    });

                    if (!stateload.equals("")) {
                        spstate.setSelection(state_list_str.indexOf(stateload));
                        stateload = "";
                    }
                    getPostcodeList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().shipping_state();
            }
        }
        new getinfo().execute();
    }

    private void getPostcodeList() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    JSONArray array = jsonObject.getJSONArray("postcode");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        postcode_list_str.add(json.getString("postcode"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditAddress.this, android.R.layout.simple_spinner_dropdown_item, postcode_list_str);
//                    spostcode.setThreshold(1);
                    spostcode.setAdapter(adapter);
//                    spostcode.setText(postcodeload);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getPostcodeList();
            }
        }
        new getinfo().execute();
    }

    private void getAddress(final String address_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        shipname.setText(jsonObject.getString("name"));
                        shipcontact.setText(jsonObject.getString("contact"));
                        shipemail.setText(jsonObject.getString("email"));
                        shipicno.setText(jsonObject.getString("ic_no"));
                        saddr1.setText(jsonObject.getString("addr1"));
//                        saddr2.setText(jsonObject.getString("addr2"));
//                        cityload = jsonObject.getString("user_shipcity");
                        scity.setText(jsonObject.getString("city"));
//                        stateload = jsonObject.getString("state");
//                        spstate.setText(jsonObject.getString("state"));
                        postcodeload = jsonObject.getString("postcode");
//                        spostcode.setText(jsonObject.getString("postcode"));
                        scountry.setText(jsonObject.getString("country"));
                        getState();


                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    spstate.setSelection(getIndex(spstate, jsonObject.getString("state")));
                                    spostcode.setSelection(getIndex(spostcode, jsonObject.getString("postcode")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1000);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getAddressNew(SavePreferences.getUserID(EditAddress.this), address_id);
            }
        }
        new getinfo().execute();
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().toUpperCase().equalsIgnoreCase(myString.toUpperCase())){
                return i;
            }
        }
        return 0;
    }

    class City {
        String id, city;

        public City(String id, String city) {
            this.id = id;
            this.city = city;
        }

        public String getId() {
            return id;
        }

        public String getCity() {
            return city;
        }
    }

    private class State {
        String id;
        String name;

        public State(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

    }

    class Postcode {
        String id, postcode;

        public Postcode(String id, String postcode) {
            this.id = id;
            this.postcode = postcode;
        }

        public String getId() {
            return id;
        }

        public String getPostcode() {
            return postcode;
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
