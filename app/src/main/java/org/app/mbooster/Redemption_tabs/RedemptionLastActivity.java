package org.app.mbooster.Redemption_tabs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Dialog.DialogFragmentUniversal;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.Holder.ConstantHolder;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.activity_folder.MyAddressEdit;
import org.app.mbooster.model_folder.AddressSB;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.YoutubeSliderView;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.DescriptionAnimation2;
import org.app.mbooster.shopping_mall.PaymentFailed;
import org.app.mbooster.shopping_mall.RedemptionActivity;
import org.app.mbooster.shopping_mall.Shopping_bag;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class RedemptionLastActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener, DialogFragmentUniversal.onSubmitListener {

    private Activity activity;
    private Context context;
    private ACProgressFlower flowerDialog;

    private int network = 0;
    private Toolbar toolbar;
    TextView toolbar_title;
    TextView shipaddress,ship_edit,bill_edit,textView_prodcutTitle;
    ImageView imageView_product;
    private RadioGroup radio_group;
    private String currently_selected_saddress = "";
    private String currently_default_baddress = "";
    private ArrayList<AddressSB> addressArrayList = new ArrayList<>();

    String color,addressId ="",userId,productid;
    Button button_redeem;
    EditText editText_redemption;

    private DialogFragmentUniversal a;
    RelativeLayout noshipbill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemption_last);

        context = activity = this;
        setuptoolbar();

        flowerDialog = new ACProgressFlower.Builder(RedemptionLastActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(getResources().getColor(R.color.colorbutton))
                .fadeColor(Color.GRAY).build();

        color = getIntent().getStringExtra("color");
        productid = getIntent().getStringExtra("productId");

        ship_edit = findViewById(R.id.ship_edit);
        bill_edit = findViewById(R.id.bill_edit);
        shipaddress = findViewById(R.id.shipaddress);
        imageView_product = findViewById(R.id.imageView_product);
        textView_prodcutTitle = findViewById(R.id.textView_prodcutTitle);
        button_redeem = findViewById(R.id.button_redeem);
        editText_redemption = findViewById(R.id.editText_redemption);
        noshipbill = findViewById(R.id.noshipbill);

        userId = SavePreferences.getUserID(this);
        ship_edit.setText(Html.fromHtml("<u>" + getString(R.string.edit) + "</u>"));
        bill_edit.setText(Html.fromHtml("<u>" + getString(R.string.edit) + "</u>"));


        ship_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_select);

                TextView text = (TextView) dialog.findViewById(R.id.title);
                text.setText(getString(R.string.shopping_select_shipping_address));
                radio_group = (RadioGroup) dialog.findViewById(R.id.radio_group);
                getAddress(0);

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                dialogButton.setText(getString(R.string.confirm));
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        addressId = addressArrayList.get(radio_group.getCheckedRadioButtonId()).id;
                        shipaddress.setText(addressArrayList.get(radio_group.getCheckedRadioButtonId()).addr1+" "+
                                addressArrayList.get(radio_group.getCheckedRadioButtonId()).addr2+" "+
                                addressArrayList.get(radio_group.getCheckedRadioButtonId()).city+" "+
                                addressArrayList.get(radio_group.getCheckedRadioButtonId()).state+" "+
                                addressArrayList.get(radio_group.getCheckedRadioButtonId()).country);
                    }
                });

                ImageView close_btn = (ImageView) dialog.findViewById(R.id.close_btn);
                close_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setAttributes(lp);
            }
        });
        bill_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_select);

                TextView text = (TextView) dialog.findViewById(R.id.title);
                text.setText(getString(R.string.shopping_select_billing_address));
                radio_group = (RadioGroup) dialog.findViewById(R.id.radio_group);
                getAddress(1);

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                dialogButton.setText(getString(R.string.confirm));
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        dialog.dismiss();
                        if (!currently_default_baddress.equals("")) {
                            if (currently_default_baddress.equals(addressArrayList.get(radio_group.getCheckedRadioButtonId()).id)) {
                                Log.i("selectedID", addressArrayList.get(radio_group.getCheckedRadioButtonId()).id);
                                Toast.makeText(RedemptionLastActivity.this, "Currently selected this address", Toast.LENGTH_SHORT).show();
                            } else {
                                //update default billing address
                                SetShipBill(addressArrayList.get(radio_group.getCheckedRadioButtonId()).id, "1");
                                dialog.dismiss();
                            }
                        } else {

                        }
                        Log.i("selected", radio_group.getCheckedRadioButtonId() + "");
                        Log.i("selectedID", addressArrayList.get(radio_group.getCheckedRadioButtonId()).id);


                    }
                });

                ImageView close_btn = (ImageView) dialog.findViewById(R.id.close_btn);
                close_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setAttributes(lp);
            }
        });

        button_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_redemption.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter redemption code",Toast.LENGTH_SHORT).show();
                }else {
                    String arg = getString(R.string.are_you_sure_to_redeem);
                    String arg2 = getString(R.string.redemptionss);
                    String arg3 = getString(R.string.confirm);
                    String arg4 = getString(R.string.cancel);

                    a = DialogFragmentUniversal.newInstance(arg, arg2, arg3, arg4);
                    a.mListener = RedemptionLastActivity.this;
                    a.show(getFragmentManager(), "");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAddressFirst();
        getProductDetails();
    }

    private void getAddressFirst() { //0 = s, 1 = b
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
                        Log.d("address",jsonObject.toString());
                        LayoutInflater layoutInflater = LayoutInflater.from(RedemptionLastActivity.this);

                        JSONArray addressarray = jsonObject.getJSONArray("address_array");

                            for (int i = 0; i < addressarray.length(); i++) {
                                JSONObject json = addressarray.getJSONObject(i);
                                if(json.getString("shipping").equals("1")){
                                    addressId = json.getString("address_id");
                                    shipaddress.setText(json.getString("addr1")+ " "+json.getString("addr2")+
                                            " "+json.getString("postcode")+" "+
                                            json.getString("city")+" "+
                                            json.getString("state")+" "+
                                            json.getString("country"));
                                }
                            }


                    }else {
                        noshipbill.setVisibility(View.VISIBLE);
                        noshipbill.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(RedemptionLastActivity.this, MyAddressEdit.class), 2);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getSBAddress(SavePreferences.getUserID(RedemptionLastActivity.this));
            }
        }
        new getinfo().execute();
    }

    private void getAddress(final int sb) { //0 = s, 1 = b
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                addressArrayList.clear();
                radio_group.removeAllViews();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        Log.d("address",jsonObject.toString());
                        LayoutInflater layoutInflater = LayoutInflater.from(RedemptionLastActivity.this);

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

                            String addrstr;
//                            if (sb == 1) {
//                                addrstr = SavePreferences.getMMSPOTNAME(Shopping_bag.this) + "\n" + SavePreferences.getMMSPOTCONTACT(Shopping_bag.this) + "\n" + SavePreferences.getMMSPOTEMAIL(Shopping_bag.this) + "\n\n" + json.getString("addr1") + ",\n" +
//                                        json.getString("city") + ", " + json.getString("postcode") + ", " + json.getString("state") + ", " + json.getString("country");
//                            }else{
                            addrstr = json.getString("name") + "\n" + json.getString("contact") + "\n" + json.getString("email") + "\n" + json.getString("ic_no") + "\n\n" + json.getString("addr1") + ",\n" +
                                    json.getString("city") + ", " + json.getString("postcode") + ", " + json.getString("state") + ", " + json.getString("country");
//                            }

                            AppCompatRadioButton rb = new AppCompatRadioButton(RedemptionLastActivity.this);
                            rb.setText(addrstr);
                            rb.setId(i);
                            LinearLayout.LayoutParams rb_params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                            Drawable drawable = getResources().getDrawable(R.drawable.radio_btn_custom);
                            drawable.setBounds(0, 0, 75, 72);
                            rb.setCompoundDrawables(null, null, drawable, null);

                            rb.setGravity(Gravity.CENTER_VERTICAL);
                            rb.setChecked(false);
                            if (sb == 0) {
                                if (json.getString("current").equals("1")) {
                                    rb.setChecked(true);
                                    currently_selected_saddress = json.getString("address_id");
                                }
                            } else {
                                if (!json.getString("default_billing").equals("0")) {
                                    rb.setChecked(true);
                                    currently_default_baddress = json.getString("default_billing");
                                }
                            }

                            rb.setButtonDrawable(android.R.color.transparent);
                            rb.setBackgroundDrawable(null);
                            rb.setLayoutParams(rb_params);

                            radio_group.addView(rb);

                            View view = new View(RedemptionLastActivity.this);
                            view.setBackgroundColor(Color.parseColor("#999999"));
                            LinearLayout.LayoutParams mainlistLayoutParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 5);
                            mainlistLayoutParams.topMargin = 15;
                            mainlistLayoutParams.bottomMargin = 15;
                            view.setLayoutParams(mainlistLayoutParams);
                            radio_group.addView(view);
                        }
//                        shippingListAdapter.notifyDataSetChanged();
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
//                    if (addressArrayList.size() == 0) {
//                        no_address.setVisibility(View.VISIBLE);
//                        add_address.setVisibility(View.VISIBLE);
//                    } else {
//                        no_address.setVisibility(View.GONE);
//                        add_address.setVisibility(View.GONE);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getSBAddress(SavePreferences.getUserID(RedemptionLastActivity.this));
            }
        }
        new getinfo().execute();
    }

    private void SetShipBill(final String address_id, final String shipbill) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(RedemptionLastActivity.this)
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
                        startActivity(getIntent());
                        finish();
                    }
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().setShipBillDefault(SavePreferences.getUserID(RedemptionLastActivity.this), address_id, shipbill, SavePreferences.getApplanguage(RedemptionLastActivity.this));
            }
        }

        new getinfo().execute();
    }


    private void SelectSBaddr(final String aid) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            private ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(RedemptionLastActivity.this)
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
                    try {
                        flowerDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        startActivity(getIntent());
                        finish();
                    }
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().selectSBAddress(SavePreferences.getUserID(context), aid, SavePreferences.getApplanguage(context));
            }
        }
        new getinfo().execute();
    }

    private void setuptoolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void networkAvailable() {
        network = 1;
    }

    @Override
    public void networkUnavailable() {
        network = 0;
        if (network == 0) {
            new AlertDialog.Builder(RedemptionLastActivity.this, R.style.AlertDialogTheme)
                    .setTitle(getString(R.string.no_network_notification))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isOnline() == false) {
                                networkUnavailable();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
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

    public void getProductDetails() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                if (!flowerDialog.isShowing()) {
                    try {
                        flowerDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                super.onPostExecute(jsonObject);
                try {
                    String response = jsonObject.getString("success");

                    Log.i("product666",jsonObject.toString());

                    if (response.equals("1")) {
                        JSONArray imgarray = jsonObject.getJSONArray("image");

                        textView_prodcutTitle.setText(JSonHelper.getObjString(jsonObject ,"product_name"));

                        for (int i = 0; i < imgarray.length(); i++) {
                            JSONObject jj = imgarray.getJSONObject(i);
                            if(i ==0){
                                ImageLoader.getInstance()
                                        .displayImage(jj.getString("product_img"), new ImageViewAware(imageView_product, false));
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.getProductDetails(userId, productid, SavePreferences.getApplanguage(RedemptionLastActivity.this));
                //Log.i("product",json.toString());

                int maxLogSize = 1000;
                for (int i = 0; i <= json.toString().length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > json.toString().length() ? json.toString().length() : end;
                }
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    @Override
    public void setOnSubmitListener(String arg) {
        if (arg.equals(getString(R.string.cancel))) {
            a.dismiss();
            Log.d("CHECKOUT","NOT OKAY");
        } else{
            if(addressId.equals("")){
                Toast.makeText(getApplicationContext(),"Please set address first",Toast.LENGTH_SHORT).show();
            }else{
                checkOut();
            }
        }
    }

    public void checkOut() {
        String code = editText_redemption.getText().toString();
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                if (!flowerDialog.isShowing()) {
                    try {
                        flowerDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                super.onPostExecute(jsonObject);
                try {
                    Log.i("checkout",jsonObject.toString());

                    if(jsonObject.getString("status").equals("0")){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("rtnmsg"),Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SavePreferences.setMainActivitySelectTab(RedemptionLastActivity.this,"2");
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();

                JSONObject json = url.checkoutRedemption(userId,addressId, productid, color, RedemptionActivity.productId,code,"SAVEREDEMPTION");
                //Log.i("product",json.toString());

                int maxLogSize = 1000;
                for (int i = 0; i <= json.toString().length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > json.toString().length() ? json.toString().length() : end;
                }
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }
}
