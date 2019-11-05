package org.app.mbooster.shopping_mall;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.Dialog.DialogFragmentUniversal;
import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MyAddressEdit;
import org.app.mbooster.listAdapter_folder.ShippingListAdapter;
import org.app.mbooster.model_folder.AddressSB;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectSBaddress extends AppCompatActivity implements DialogFragmentUniversal.onSubmitListener, NetworkStateReceiver.NetworkStateReceiverListener {
    private ListView shippinglist;
    private ArrayList<AddressSB> addressArrayList = new ArrayList<>();
    private ShippingListAdapter shippingListAdapter;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;
    private AlertDialog alertDialog, dialog;

    private Toolbar toolbar;
    private SwipeRefreshLayout refresh;

    private TextView no_address, add_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sbaddress);
        context = activity = this;
        ToobarSetting();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        no_address = (TextView) findViewById(R.id.no_address);
        add_address = (TextView) findViewById(R.id.add_address);
        add_address.setText(Html.fromHtml("<font color='blue'><u>"+getString(R.string.add_address)+"</u></font>"));
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAddress();
            }
        });

        shippinglist = (ListView) findViewById(R.id.shippinglist);

        shippingListAdapter = new ShippingListAdapter(this, addressArrayList);

        shippinglist.setAdapter(shippingListAdapter);

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SelectSBaddress.this,MyAddressEdit.class),2);
            }
        });

        shippinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AddressSB item = addressArrayList.get(i);
                if (item.currently.equals("0")) {
//                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(SelectSBaddress.this, R.style.AlertDialogTheme);
//                LayoutInflater inflater = SelectSBaddress.this.getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.alert_address_option, null);
//                builderSingle.setView(dialogView);

                    String arg = getString(R.string.are_u_sure_select_this_shipping_address);
                    String arg2 = getString(R.string.select_address);
                    String arg3 = getString(R.string.yes);
                    String arg4 = getString(R.string.cancel);

                    final Dialog dialog = new Dialog(SelectSBaddress.this, R.style.AlertDialogTheme);
                    TypefaceUtil.overrideFont(SelectSBaddress.this, "SERIF", "fonts/gotham_book.otf");
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    dialog.setContentView(R.layout.fragment_dialog_universal);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    TextView content = (TextView) dialog.findViewById(R.id.content);
                    TextView title = (TextView) dialog.findViewById(R.id.title);
                    title.setText(arg2);
                    Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                    Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                    btnConfirm.setText(arg3);
                    btnCancel.setText(arg4);


                    content.setText(arg);
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            SelectSBaddr(item.id);
                        }
                    });


                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                    dialog.show();
                } else {
                    Toast.makeText(context, R.string.currently_seleted_this_address, Toast.LENGTH_SHORT).show();
                }
            }
        });


        getAddress();
        Helper.CheckMaintenance(this);
    }

    @Override
    public void setOnSubmitListener(String arg) {
        Log.d("arg", arg.toString());
        if (arg.equals(getString(R.string.take_photo))) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getAddress();
        }else if(requestCode == 2){
            startActivity(getIntent());
            this.finish();
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void SelectSBaddr(final String aid) {
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
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().selectSBAddress(SavePreferences.getUserID(context), aid,SavePreferences.getApplanguage(context));
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
                addressArrayList.clear();
                if (!refresh.isRefreshing()) {
                    refresh.setRefreshing(true);
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                refresh.setRefreshing(false);
                try {
                    if (jsonObject.getString("success").equals("1")) {


                        JSONArray addressarray = jsonObject.getJSONArray("address_array");

                        for (int i = 0; i < addressarray.length(); i++) {
                            JSONObject json = addressarray.getJSONObject(i);
                            addressArrayList.add(new AddressSB(json.getString("address_id"),
                                    json.getString("name"),
                                    json.getString("contact"),
                                    json.getString("email"),
                                    json.getString("ic_no"),
                                    json.getString("addr1"),
                                    json.getString("addr2"),
                                    json.getString("postcode"),
                                    json.getString("city"),
                                    json.getString("state"),
                                    json.getString("country"),
                                    json.getString("shipping"),
                                    json.getString("billing"),
                                    json.getString("status"),
                                    json.getString("current"),
                                    json.getString("default_billing")
                            ));
                        }
                        shippingListAdapter.notifyDataSetChanged();


//                        if(jsonObject.getString("default_shipping").equals("0") && jsonObject.getString("default_billing").equals("0")) {
//                            noshipbill.setVisibility(View.VISIBLE);
//                            tvnoshipbill.setText("No default Shipping and Billing address is set");
//                        }else if(jsonObject.getString("default_shipping").equals("0")){
//                            noshipbill.setVisibility(View.VISIBLE);
//                            tvnoshipbill.setText("No default Shipping address is set");
//                        }else if( jsonObject.getString("default_billing").equals("0")){
//                            noshipbill.setVisibility(View.VISIBLE);
//                            tvnoshipbill.setText("No default Billing address is set");
//                        }else{
//                            noshipbill.setVisibility(View.GONE);
//                        }
                    }
                    if (addressArrayList.size() == 0) {
                        no_address.setVisibility(View.VISIBLE);
                        add_address.setVisibility(View.VISIBLE);
                    } else {
                        no_address.setVisibility(View.GONE);
                        add_address.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getSBAddress(SavePreferences.getUserID(SelectSBaddress.this));
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
