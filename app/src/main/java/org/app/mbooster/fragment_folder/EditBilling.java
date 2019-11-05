package org.app.mbooster.fragment_folder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditBilling extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private TextView tvbaddr1, tvbaddr2, tvbstate, tvbcity, tvbpostcode, tvbcountry;
    private EditText baddr1, baddr2, bcountry, bcity;
    private AutoCompleteTextView bpostcode;
    private Spinner spbstate;
//    , spbcity, spbpostcode;

    private ArrayList<State> bstate_list = new ArrayList<>();
    private ArrayList<String> bstate_list_str = new ArrayList<>();
    private ArrayList<City> bcity_list = new ArrayList<>();
    private ArrayList<String> bcity_list_str = new ArrayList<>();
    private ArrayList<Postcode> bpostcode_list = new ArrayList<>();
    private ArrayList<String> bpostcode_list_str = new ArrayList<>();

    Toolbar toolbar;
    private RelativeLayout submit;
    private TextView tvsubmit, toolbar_title;

    private String cityload, stateload, postcodeload;
    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_billing);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        LogHelper.debug("[EditBilling]");

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        tvbaddr1 = (TextView) findViewById(R.id.tvbaddr1);
        tvbaddr2 = (TextView) findViewById(R.id.tvbaddr2);
        tvbstate = (TextView) findViewById(R.id.tvbstate);
        tvbcity = (TextView) findViewById(R.id.tvbcity);
        tvbpostcode = (TextView) findViewById(R.id.tvbpostcode);
        tvbcountry = (TextView) findViewById(R.id.tvbcountry);
        tvsubmit = (TextView) findViewById(R.id.tvsubmit);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        submit = (RelativeLayout) findViewById(R.id.submit);

        baddr1 = (EditText) findViewById(R.id.baddr1);
        baddr2 = (EditText) findViewById(R.id.baddr2);
        bcountry = (EditText) findViewById(R.id.bcountry);
        spbstate = (Spinner) findViewById(R.id.spbstate);
        bcity = (EditText) findViewById(R.id.bcity);
        bpostcode = (AutoCompleteTextView) findViewById(R.id.bpostcode);
//        spbcity = (Spinner) findViewById(R.id.spbcity);
//        spbpostcode = (Spinner) findViewById(R.id.spbpostcode);

        ToobarSetting();
        LanguageSetting();

        getAddress();
        getPostcodeList();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (baddr1.getText().toString().trim().length() == 0 || bpostcode.getText().toString().trim().length() == 0 || bcity.getText().toString().trim().length() == 0) {
                    if (SavePreferences.getApplanguage(EditBilling.this).equals("SCN")) {
                        Toast.makeText(EditBilling.this, "请填入所有空格", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditBilling.this, "Please fill in all blanks", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    updateBillingInfo(baddr1.getText().toString(), baddr2.getText().toString(), bpostcode.getText().toString(),
                            bcity.getText().toString(), bstate_list.get(spbstate.getSelectedItemPosition()).getName(), bcountry.getText().toString());
                }
            }
        });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void updateBillingInfo(final String addr1, final String addr2, final String postcode, final String city, final String state, final String country) {
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
                        Toast.makeText(EditBilling.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().updateBillingInfo(SavePreferences.getUserID(EditBilling.this), addr1, addr2, postcode, city, state, country);
                return null;
            }
        }
        new getinfo().execute();
    }


    //get_address.php?user_id=
    private void getAddress() {
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


                        baddr1.setText(jsonObject.getString("user_billaddr1"));
                        baddr2.setText(jsonObject.getString("user_billaddr2"));
                        stateload = jsonObject.getString("user_billstate");
//                        cityload = jsonObject.getString("user_billcity");
                        bcity.setText(jsonObject.getString("user_billcity"));
//                        postcodeload = jsonObject.getString("user_billzip");
                        bpostcode.setText(jsonObject.getString("user_billzip"));
                        bcountry.setText(jsonObject.getString("user_billcountry"));

                        getbState();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getAddress(SavePreferences.getUserID(EditBilling.this));
            }
        }
        new getinfo().execute();
    }


    private void getbState() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    JSONArray array = jsonObject.getJSONArray("state");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        bstate_list.add(new State(json.getString("ssid"), json.getString("state")));
                        bstate_list_str.add(json.getString("state"));
                    }

                    ArrayAdapter<String> stateList = new ArrayAdapter<>(EditBilling.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            bstate_list_str);
                    spbstate.setAdapter(stateList);
                    spbstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            spbpostcode.setAdapter(null);
//                            spbcity.setAdapter(null);
//                            bpostcode_list_str.clear();
//                            bpostcode_list.clear();
                            bcity_list.clear();
                            bcity_list_str.clear();
                            String id = bstate_list.get(i).getId();
//                            getbCity(id);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    if (!stateload.equals("")) {
                        spbstate.setSelection(bstate_list_str.indexOf(stateload));
                        stateload = "";
                    }
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
                        bpostcode_list_str.add(json.getString("postcode"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditBilling.this, android.R.layout.simple_spinner_dropdown_item, bpostcode_list_str);
                    bpostcode.setThreshold(1);
                    bpostcode.setAdapter(adapter);

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

//    private void getbCity(final String state_id) {
//        class getinfo extends AsyncTask<String, String, JSONObject> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                super.onPostExecute(jsonObject);
//
//                try {
//                    JSONArray array = jsonObject.getJSONArray("city");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject json = array.getJSONObject(i);
//                        bcity_list.add(new City(json.getString("sciid"), json.getString("city")));
//                        bcity_list_str.add(json.getString("city"));
//                    }
//                    ArrayAdapter<String> cityList = new ArrayAdapter<>(EditBilling.this,
//                            android.R.layout.simple_spinner_item,
//                            bcity_list_str);
//                    spbcity.setAdapter(cityList);
//                    spbcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            String id = bcity_list.get(i).getId();
//                            bpostcode_list.clear();
//                            bpostcode_list_str.clear();
//                            getbPostcode(id);
//
//
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//
//                        }
//                    });
////                    if (!cityload.equals("")) {
////                        spbcity.setSelection(bcity_list_str.indexOf(cityload));
////                        cityload = "";
////                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().shipping_city(state_id);
//            }
//        }
//        new getinfo().execute();
//    }

//    private void getbPostcode(final String city_id) {
//        class getinfo extends AsyncTask<String, String, JSONObject> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                super.onPostExecute(jsonObject);
//
//                try {
//                    JSONArray array = jsonObject.getJSONArray("postcode");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject json = array.getJSONObject(i);
//                        bpostcode_list.add(new Postcode(json.getString("spid"), json.getString("postcode")));
//                        bpostcode_list_str.add(json.getString("postcode"));
//                    }
//                    ArrayAdapter<String> postcodeList = new ArrayAdapter<>(EditBilling.this,
//                            android.R.layout.simple_spinner_item,
//                            bpostcode_list_str);
//                    spbpostcode.setAdapter(postcodeList);
//
//                    if (!postcodeload.equals("")) {
//                        spbpostcode.setSelection(bpostcode_list_str.indexOf(postcodeload));
//                        postcodeload = "";
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().shipping_postcode(city_id);
//            }
//        }
//        new getinfo().execute();
//    }


    private void LanguageSetting() {
        if (SavePreferences.getApplanguage(EditBilling.this).equals("SCN")) {
            tvbaddr1.setText("地址1");
            tvbaddr2.setText("地址2");
            tvbstate.setText("州属");
            tvbcity.setText("城市");
            tvbpostcode.setText("邮区编号");
            tvbcountry.setText("国家");
            tvsubmit.setText("提交");
            toolbar_title.setText("账单地址");
        }
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

    class Postcode {
        String id;
        String postcodestr;

        public Postcode(String id, String postcodestr) {
            this.id = id;
            this.postcodestr = postcodestr;
        }

        public String getId() {
            return id;
        }

        public String getPostcodestr() {
            return postcodestr;
        }
    }

    class City {
        String id;
        String name;

        public City(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    class State {
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
        alertDialog = new AlertDialog.Builder(context,R.style.AlertDialogTheme)
                .setTitle(getString(R.string.no_network_notification))
                .setCancelable(false)

                .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline() == false) {
                            networkUnavailable();
                        } else {
                            dialog.dismiss();
                            getAddress();
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
