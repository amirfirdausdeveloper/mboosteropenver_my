package org.app.mbooster.shopping_mall;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditSBShipping extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private EditText shipname, shipcontact, shipemail;
    private Spinner spstate;
    private EditText saddr1, saddr2, scity, scountry;
    private AutoCompleteTextView spostcode;

    private RelativeLayout submit;

    private Toolbar toolbar;

    private String stateload, postcodeload;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sbshipping);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = activity = this;


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        ToobarSetting();
//        getAddressFromCart();


        shipname = (EditText) findViewById(R.id.shipname);
        shipcontact = (EditText) findViewById(R.id.shipcontact);
        spstate = (Spinner) findViewById(R.id.spstate);
        spostcode = (AutoCompleteTextView) findViewById(R.id.spostcode);
        saddr1 = (EditText) findViewById(R.id.saddr1);
        saddr2 = (EditText) findViewById(R.id.saddr2);
        scountry = (EditText) findViewById(R.id.scountry);
        shipemail = (EditText) findViewById(R.id.shipemail);
        scity = (EditText) findViewById(R.id.scity);

        Intent intent = getIntent();
        shipname.setText(intent.getStringExtra("username"));
        shipcontact.setText(intent.getStringExtra("contact"));
        saddr1.setText(intent.getStringExtra("addr1"));
        saddr2.setText(intent.getStringExtra("addr2"));
        scountry.setText(intent.getStringExtra("country"));
        shipemail.setText(intent.getStringExtra("email"));
        scity.setText(intent.getStringExtra("city"));

        stateload = intent.getStringExtra("state");
        postcodeload = intent.getStringExtra("postcode");


        submit = (RelativeLayout) findViewById(R.id.submit);
        Helper.buttonEffect(submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shipname.getText().toString().trim().length() == 0 || shipcontact.getText().toString().trim().length() == 0 || shipemail.getText().toString().trim().length() == 0
                        || saddr1.getText().toString().trim().length() == 0) {
                    Toast.makeText(EditSBShipping.this, R.string.please_fill_in_all_blanks, Toast.LENGTH_SHORT).show();
                } else {
                    updateSBShippingAddr(shipname.getText().toString(), shipcontact.getText().toString(), shipemail.getText().toString(), scity.getText().toString(),state_list_str.get(spstate.getSelectedItemPosition()),
                            spostcode.getText().toString(),saddr1.getText().toString(),saddr2.getText().toString(), scountry.getText().toString());
                }
            }
        });

        //getAddress();
        getState();
        getPostcodeList();
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

    public void updateSBShippingAddr(final String shipname, final String shipcontact, final String shipemail, final String shipcity,final String shipstate, final String shippostcode, final String shipaddr1,final String shipaddr2, final String shipcountry) {
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
                        setResult(RESULT_OK);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().updateSBShipping(SavePreferences.getUserID(EditSBShipping.this), shipname, shipcontact, shipemail, shipcity,shipstate, shippostcode, shipaddr1,shipaddr2, shipcountry);
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

                    ArrayAdapter<String> stateList = new ArrayAdapter<>(EditSBShipping.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            state_list_str);
                    spstate.setAdapter(stateList);
                    spstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            sppostcode.setAdapter(null);
//                            spcity.setAdapter(null);
//                            postcode_list_str.clear();
//                            postcode_list.clear();
                            city_list.clear();
                            city_list_str.clear();
                            String id = state_list.get(i).getId();

//                            getCity(id);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    if (!stateload.equals("")) {
                        spstate.setSelection(state_list_str.indexOf(stateload));
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
                        postcode_list_str.add(json.getString("postcode"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditSBShipping.this, android.R.layout.simple_spinner_dropdown_item, postcode_list_str);
                    spostcode.setThreshold(1);
                    spostcode.setAdapter(adapter);
                    spostcode.setText(postcodeload);


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
