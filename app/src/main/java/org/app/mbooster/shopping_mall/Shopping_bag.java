package org.app.mbooster.shopping_mall;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Dialog.DialogFragmentSingleBtn;
import org.app.mbooster.Dialog.DialogFragmentUniversal;
import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.VoucherSelectionActivity;
import org.app.mbooster.activity_folder.signInActivity;
import org.app.mbooster.activity_folder.MyAddressEdit;
import org.app.mbooster.model_folder.AddressSB;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.ShoppingBagItem;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import me.himanshusoni.chatmessageview.ChatMessageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;



public class Shopping_bag extends AppCompatActivity implements DialogFragmentUniversal.onSubmitListener, DialogFragmentSingleBtn.onSubmitListener, NetworkStateReceiver.NetworkStateReceiverListener {
    private ListView listview;
    private Toolbar toolbar;
    private String userId, usercredit;
    private TextView granttotal, trancs, grandtotalma, grandtotalmyr, totalcashback;
    private String granttotalstr;
    private ArrayList<ShoppingBagItem> items = new ArrayList<>();
    private RelativeLayout noitem, checkout;
    private DialogFragmentUniversal a;
    private DialogFragmentSingleBtn b;

    private ShoppingListAdapter adapter;

    double shipping_cost;

    private NetworkStateReceiver networkStateReceiver;
    private Activity activity;
    private Context context;
    private int network = 0;

    private TextView tvtrans, tvgrandtotal, tvempty, tvcheckout, toolbar_title, tvgrandtotalma, tvgrandtotalmyr, tvtotalcashback;

    private RelativeLayout add_voucher, voucher_entitlement;
    private TextView voucher_count;
    private RelativeLayout voucher_count_ly;
    private RelativeLayout darker_bg;
    private ChatMessageView voucher_tips;

    private ArrayList<AddressSB> addressArrayList = new ArrayList<>();

    private RelativeLayout voucher_ly;
    private LinearLayout footer_ly;
    private TextView voucher_total_amount;

    private final int VOUCHER = 10;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    //user info
    private String namestr = "", emailstr = "", contactstr = "", address1str = "", postcodestr = "", countrystr = "", statestr = "", citystr = "", address2str = "";

    //private EditText name, email, contact, icno, address2, country, postcode;
    private TextView totalpoint, address1, shippingfee, title, tvtotalpoint, tvsubmit;
    private TextView ship_edit, bill_edit;
    private TextView tvusevoucher;

//    private Button edit;

    private View footerView;

    private static int UPDATE_ADDRESS = 99;
    private RelativeLayout noshipbill;
    private TextView tvnoshipbill;

    private RadioGroup radio_group;
    private String currently_selected_saddress = "";
    private String currently_default_baddress = "";

    private SwipeRefreshLayout swipe;

    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    private String isSelfCollect = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_bag);
        LogHelper.info("This is Shopping_bag.java");

        context = activity = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // firebase analytic
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        pref = getSharedPreferences("mboosterMY", 0); // 0 - for private mode
        editor = pref.edit();
        Helper.setupImageCache(Shopping_bag.this);
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
                handleOnBackPress();
            }
        });
        userId = SavePreferences.getUserID(this);

        darker_bg = (RelativeLayout) findViewById(R.id.darker_bg);
        voucher_tips = (ChatMessageView) findViewById(R.id.voucher_tips);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        darker_bg.setVisibility(View.GONE);
        voucher_tips.setVisibility(View.GONE);

        voucher_count = (TextView) findViewById(R.id.voucher_count);
        voucher_count_ly = (RelativeLayout) findViewById(R.id.voucher_count_ly);
        voucher_count_ly.setVisibility(View.GONE);

        voucher_entitlement = (RelativeLayout) findViewById(R.id.voucher_entitlement);
        voucher_entitlement.setVisibility(View.GONE);

        add_voucher = (RelativeLayout) findViewById(R.id.add_voucher);
        add_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("fab", "clicked");
                //Intent intent = new Intent(Shopping_bag.this, Voucher.class);
                //startActivityForResult(intent, 4);
                //startActivityForResult(new Intent(Shopping_bag.this, Voucher.class), VOUCHER);
            }
        });

        add_voucher.setVisibility(View.GONE);

//        voucher_ly = (LinearLayout) findViewById(R.id.voucher_ly);
//        voucher_ly.setVisibility(View.GONE);
//        voucher_total_amount = (TextView) findViewById(R.id.voucher_total_amount);

        listview = (ListView) findViewById(R.id.listview);

        adapter = new ShoppingListAdapter(Shopping_bag.this, items);
        listview.setAdapter(adapter);
;
        noitem = (RelativeLayout) findViewById(R.id.noitem);
        checkout = (RelativeLayout) findViewById(R.id.checkout);

//        granttotal = findViewById(R.id.granttotal);
//        totalcashback = (TextView) findViewById(R.id.totalcashback);

        //In the bottom button bar
        grandtotalma = (TextView) findViewById(R.id.grandtotalma);
        grandtotalmyr = (TextView) findViewById(R.id.grandtotalmyr);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tvempty = (TextView) findViewById(R.id.tvempty);
        tvcheckout = (TextView) findViewById(R.id.tvcheckout);
//        tvgrandtotal = findViewById(R.id.tvgrandtotal);

//        tvgrandtotal.setVisibility(View.GONE);
//        granttotal.setVisibility(View.GONE);

        noshipbill = (RelativeLayout) findViewById(R.id.noshipbill);
        tvnoshipbill = (TextView) findViewById(R.id.tvnoshipbill);

        noshipbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Shopping_bag.this, MyAddressEdit.class), 2);
            }
        });

        Helper.buttonEffect(checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.size() == 0) {
                    Toast.makeText(Shopping_bag.this, getString(R.string.oops_there_is_nothing_in_your_shopping_bag), Toast.LENGTH_SHORT).show();

                } else {
                    String arg = getString(R.string.are_you_sure_to_checkout);
                    String arg2 = getString(R.string.check_out);
                    String arg3 = getString(R.string.confirm);
                    String arg4 = getString(R.string.cancel);

                    a = DialogFragmentUniversal.newInstance(arg, arg2, arg3, arg4);
                    a.mListener = Shopping_bag.this;
                    a.show(getFragmentManager(), "");
                }
            }
        });

        //Header
        LayoutInflater layoutInflater = LayoutInflater.from(Shopping_bag.this);
        View headerview = layoutInflater.inflate(R.layout.shopping_list_header, null);
        headerview.setEnabled(false);
        headerview.setFocusable(false);
        headerview.setClickable(false);

        //Add FooterHere If have data
        initiateFooter();

        if (SavePreferences.getUserID(Shopping_bag.this).equals("0")) {
            swipe.setVisibility(View.GONE);
            //Helper.LoginDialog(Shopping_bag.this);
            startActivity(new Intent(Shopping_bag.this, signInActivity.class));
            handleOnBackPress();
        } else {
            shoppingbaglist();
        }

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(Shopping_bag.this, ViewProductFromCart.class);
//                i.putExtra("productid", items.get(position).getProduct_id());
//                startActivity(i);
//            }
//        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shoppingbaglist();
            }
        });



        address1 = (TextView) headerview.findViewById(R.id.shipaddress);
        ship_edit = (TextView) headerview.findViewById(R.id.ship_edit);
        bill_edit = (TextView) headerview.findViewById(R.id.bill_edit);

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
//                        dialog.dismiss();
                        if (!currently_selected_saddress.equals("")) {
                            if (currently_selected_saddress.equals(addressArrayList.get(radio_group.getCheckedRadioButtonId()).id)) {
                                Log.i("selectedID", addressArrayList.get(radio_group.getCheckedRadioButtonId()).id);
                                Toast.makeText(Shopping_bag.this, "Currently selected this address", Toast.LENGTH_SHORT).show();
                            } else {
                                //update shipping address
                                SelectSBaddr(addressArrayList.get(radio_group.getCheckedRadioButtonId()).id);
                                dialog.dismiss();
                            }
                        } else {
                            //update shipping address
                            SelectSBaddr(addressArrayList.get(radio_group.getCheckedRadioButtonId()).id);
                            dialog.dismiss();
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
                                Toast.makeText(Shopping_bag.this, "Currently selected this address", Toast.LENGTH_SHORT).show();
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


//        Helper.buttonEffect(edit);
        listview.addHeaderView(headerview, "Header", false);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void setOnSubmitListener(String arg) {
        if (arg.equals(getString(R.string.cancel))) {
            a.dismiss();
        } else if(arg.equals("ok")){
            b.dismiss();
        }else {
            ValidateCheckOut2();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Intent i = new Intent();
                setResult(RESULT_OK, i);
            } else {
                startActivity(getIntent());
                this.finish();
            }
            this.finish();
        } else if (requestCode == 2) {
            startActivity(getIntent());
            this.finish();
        } else if (requestCode == VOUCHER) {
//            String count = data.getStringExtra("voucher_count");
//            if(!count.equals("0")){
//                voucher_count_ly.setVisibility(View.VISIBLE);
//                voucher_count.setText(count);
//
            startActivity(getIntent());
            this.finish();
        } else if (requestCode == 50 && resultCode == RESULT_OK) {
            startActivity(getIntent());
            this.finish();
        } else if (requestCode == UPDATE_ADDRESS) {
            startActivity(getIntent());
            this.finish();
        } else if (requestCode == 4) {
            Log.i("requestcode4", "inside requestcode4");
            if (resultCode == RESULT_OK) {
                shoppingbaglist();
                Log.i("requestcode4", "inside if");
            }
        }
    }

    private void initiateFooter(){
        //Footer
        LayoutInflater layoutInflater = LayoutInflater.from(Shopping_bag.this);
        footerView = layoutInflater.inflate(R.layout.shopping_list_footer, null);
        footerView.setEnabled(false);
        footerView.setFocusable(false);
        footerView.setClickable(false);
        listview.addFooterView(footerView, "Footer", false);
//        tvtrans = findViewById(R.id.tvtrans);
        footer_ly = footerView.findViewById(R.id.shopping_footer);
        trancs =  footerView.findViewById(R.id.trancs);
        voucher_ly = footerView.findViewById(R.id.voucher_ly);
        voucher_ly.setVisibility(View.GONE);
        totalcashback = footerView.findViewById(R.id.totalcashback);
        voucher_total_amount = footerView.findViewById(R.id.voucher_total_amount);
        grandtotalmyr = footerView.findViewById(R.id.grandtotalmyr);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            handleOnBackPress();
        }
        return false;
    }

    public void handleOnBackPress() {

//        onclick back arrow back to previous page
        Intent i = new Intent();
        //setResult(RESULT_OK, i);
        this.finish();
    }

    private void SelectSBaddr(final String aid) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            private ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Shopping_bag.this)
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

    private void SetShipBill(final String address_id, final String shipbill) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Shopping_bag.this)
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
                return new urlLink().setShipBillDefault(SavePreferences.getUserID(Shopping_bag.this), address_id, shipbill, SavePreferences.getApplanguage(Shopping_bag.this));
            }
        }

        new getinfo().execute();
    }

    public void dismissitem(final String cart_id, final int position) {
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

                        shoppingbaglist();
                        Toast.makeText(Shopping_bag.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //adapter.notifyDataSetChanged();
                    startActivity(getIntent());
                    finish();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink uu = new urlLink();
                JSONObject json = uu.dismissitem(SavePreferences.getUserID(Shopping_bag.this), cart_id, SavePreferences.getApplanguage(Shopping_bag.this));
                return json;
            }

        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void shoppingbaglist() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            private ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                }
                items.clear();
                footer_ly.setVisibility(View.GONE);
                flowerDialog = new ACProgressFlower.Builder(Shopping_bag.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }

            }

            @Override
            protected void onPostExecute(final JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                Log.i("711ccc", jsonObject.toString());

                if (flowerDialog.isShowing()) {
                    try {
                        flowerDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        JSONObject userinfo = jsonObject.getJSONObject("userinfo");
                        namestr = userinfo.getString("username");
                        emailstr = userinfo.getString("email");
                        contactstr = userinfo.getString("contact");
                        address1str = userinfo.getString("address");
                        address2str = userinfo.getString("address2");
                        citystr = userinfo.getString("city");
                        statestr = userinfo.getString("state");
                        countrystr = userinfo.getString("country");
                        postcodestr = userinfo.getString("postcode");

                        address1.setText(address1str + ",\n" + citystr + ", " + postcodestr + ", " + statestr + "," + countrystr);

                        final JSONArray jsonArray = jsonObject.getJSONArray("array");

                        items.addAll(JSonHelper.parseShoppingBagItem(jsonArray, isSelfCollect));
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject json = jsonArray.getJSONObject(i);
//                            Log.i("748 all", json.toString());
//
//                            items.add(new ShoppingBagItem(json.getString("cart_id"), json.getString("product_id"), json.getString("product_name")
//                                    , json.getString("product_qty"), json.getString("product_img"), json.getString("amount_point"), json.getString("total_product_point"),
//                                    json.getString("supplier_name"), json.getString("shipping_cost"), json.getString("voucher_status"), json.getString("isSelfCollect"), json.getString("is_allowed_self_collect_item"), json.getString("totSplitAmt"), json.getString("totCashAmt"), json.getString("totVcrAmt"), json.getString("redeem_type")));
//                        }

                        trancs.setText(": " + getResources().getString(R.string.currency) + jsonObject.getString("shippingcost"));
                        Log.i("757 all", jsonObject.toString());

                        grandtotalma.setText(": " + getResources().getString(R.string.currency) + jsonObject.getString("grdtotMA"));
                        grandtotalmyr.setText(": " + getResources().getString(R.string.currency) + jsonObject.getString("grdtotCash"));
                        totalcashback.setText(": "+ jsonObject.getString("grdtotCashback"));

                        footer_ly.setVisibility(View.VISIBLE);

                        granttotalstr = jsonObject.getString("total");
                        noitem.setVisibility(View.GONE);
                        tvempty.setVisibility(View.GONE);
                        ship_edit.setVisibility(View.VISIBLE);
                        bill_edit.setVisibility(View.VISIBLE);
                        final String voucher_qualify = jsonObject.getString("voucher_qualify");
                        final String voucherST = jsonObject.getString("voucher_status");
                        if (voucher_qualify.equals("1")) {
                            voucher_entitlement.setVisibility(View.VISIBLE);
                        } else {
                            voucher_entitlement.setVisibility(View.GONE);
                        }


                        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView absListView, int i) {
                                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                                    Log.i("a", "scrolling stopped...");

                                    if (voucherST.equals("1")) {
                                        //add_voucher.setVisibility(View.VISIBLE);
                                        add_voucher.setVisibility(View.GONE);
                                    }
                                    if (voucher_qualify.equals("1")) {
                                        voucher_entitlement.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    add_voucher.setVisibility(View.GONE);

                                    voucher_entitlement.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                            }
                        });

                        if (jsonObject.getString("voucher_status").equals("1")) {

                            //add_voucher.setVisibility(View.VISIBLE);
                            add_voucher.setVisibility(View.GONE);
                            voucher_ly.setVisibility(View.VISIBLE);
                            voucher_total_amount.setText(": " + getString(R.string.currency) + jsonObject.getString("eVoucherAmt"));

                            String voucher_countstr = jsonObject.getString("voucher_count");
                            if (!voucher_countstr.equals("0")) {
                                voucher_count_ly.setVisibility(View.VISIBLE);
                                voucher_count.setText(voucher_countstr);
                            }
                            if (pref.getString("vouchertips", "0").equals("0")) {
                                add_voucher.setEnabled(false);
                                darker_bg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        darker_bg.setVisibility(View.GONE);
                                        voucher_tips.setVisibility(View.GONE);
                                        add_voucher.setEnabled(true);
                                    }
                                });
                                voucher_tips.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        darker_bg.setVisibility(View.GONE);
                                        voucher_tips.setVisibility(View.GONE);
                                        add_voucher.setEnabled(true);
                                    }
                                });


                                editor.putString("vouchertips", "1").commit();
                            }

                        } else {
                            add_voucher.setVisibility(View.GONE);
                            voucher_ly.setVisibility(View.GONE);
                        }

//                        edit.setVisibility(View.VISIBLE);

                    } else if (jsonObject.getString("success").equals("0")) {
                        trancs.setText(": " + getResources().getString(R.string.currency) + "0");
                        //granttotal.setText(getResources().getString(R.string.currency) + "0");
                        grandtotalma.setText(": " + getResources().getString(R.string.currency) + "0");
                        grandtotalmyr.setText(": " + getResources().getString(R.string.currency) + "0");
                        totalcashback.setText(": " + getResources().getString(R.string.currency) + "0");
                        ship_edit.setVisibility(View.INVISIBLE);
                        bill_edit.setVisibility(View.INVISIBLE);
                    }

                    if (jsonObject.getString("default_shipping").equals("0") && jsonObject.getString("default_billing").equals("0")) {
                        noshipbill.setVisibility(View.VISIBLE);
                        tvnoshipbill.setText(R.string.no_ship_bill_default);
                        checkout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(activity, R.string.please_set_default_shipping_and_billing, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (jsonObject.getString("default_shipping").equals("0")) {
                        noshipbill.setVisibility(View.VISIBLE);
                        tvnoshipbill.setText(R.string.no_ship_default);
                        checkout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(activity, R.string.please_set_default_shipping, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (jsonObject.getString("default_billing").equals("0")) {
                        noshipbill.setVisibility(View.VISIBLE);
                        tvnoshipbill.setText(R.string.no_bill_default);
                        checkout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(activity, R.string.please_set_default_billing, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        noshipbill.setVisibility(View.GONE);
                    }
                    if (jsonObject.getString("default_shipping").equals("0") || jsonObject.getString("default_billing").equals("0")) {
                        //New Dialog

                        final Dialog dialog = new Dialog(activity);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.dialog_msg);

                        TextView text = (TextView) dialog.findViewById(R.id.msg);
                        text.setText("You haven't added your Default Shipping or Billing Address.");

                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Shopping_bag.this, MyAddressEdit.class));
                                finish();
                                dialog.dismiss();
                            }
                        });

                        ImageView close_btn = (ImageView) dialog.findViewById(R.id.close_btn);
                        close_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }

                    if (jsonObject.getString("APP_ORDER_DISABLE_STATUS").equals("1") && !SavePreferences.getUserID(Shopping_bag.this).equals("113878")) {
                        Log.i("AP_ORDER_DISABLE_STATUS", "show dialog");
                        LayoutInflater factory = LayoutInflater.from(Shopping_bag.this);
                        final View indexDialogView = factory.inflate(R.layout.dialog_image_btn, null);
                        final AlertDialog index_dialog = new AlertDialog.Builder(Shopping_bag.this).create();
                        index_dialog.setView(indexDialogView);
                        index_dialog.setCancelable(false);
                        index_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        index_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        indexDialogView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //your business logic
                                index_dialog.dismiss();
                                finish();
                            }
                        });
//                            ImageLoader.getInstance().displayImage(jsonObject.getString(""), new ImageViewAware());
                        ImageLoader.getInstance().displayImage(jsonObject.getString("APP_ORDER_DISABLE_IMG"), new ImageViewAware((ImageView) indexDialogView.findViewById(R.id.dialog_image), false));
                        index_dialog.show();

                        checkout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                index_dialog.show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogHelper.error(e.getLocalizedMessage());
                } finally {
                    adapter.notifyDataSetChanged();
//                    checkoutLoad(SavePreferences.getUserID(Shopping_bag.this));
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink uu = new urlLink();
                JSONObject json = uu.shopping_bag_list(Shopping_bag.this);//userId);
                return json;
            }

        }
        getinfo ge = new getinfo();
        ge.execute();
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
                        LayoutInflater layoutInflater = LayoutInflater.from(Shopping_bag.this);

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

                            AppCompatRadioButton rb = new AppCompatRadioButton(Shopping_bag.this);
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

                            View view = new View(Shopping_bag.this);
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
                return new urlLink().getSBAddress(SavePreferences.getUserID(Shopping_bag.this));
            }
        }
        new getinfo().execute();
    }

    private void ValidateCheckOut2() {
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
                        Shopping_bag.this.finish();
                        Intent i = new Intent(Shopping_bag.this, CheckOut.class);
                        startActivityForResult(i, 1);
                    } else if (jsonObject.getString("success").equals("0")) {
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else{
                        String arg = jsonObject.getString("message");
                        String arg2 = "OK";

                        b = DialogFragmentSingleBtn.newInstance(arg, arg2);
                        b.mListener = Shopping_bag.this;
                        b.show(getFragmentManager(), "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().validateCheckOut(SavePreferences.getUserID(activity), SavePreferences.getApplanguage(activity));
            }
        }
        new getinfo().execute();
    }


    public void product_add_qty(final String card_id) {

        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Shopping_bag.this)
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
                        shoppingbaglist();

                    } else if (jsonObject.getString("success").equals("2")) {
//                        Toast.makeText(Shopping_bag.this, R.string.you_have_reach_stock_limit, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Shopping_bag.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Shopping_bag.this, R.string.not_updated, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink uu = new urlLink();
                JSONObject json = uu.product_add_qty(SavePreferences.getUserID(Shopping_bag.this), card_id, SavePreferences.getApplanguage(Shopping_bag.this));
                return json;
            }


        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void product_deduct_qty(final String card_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Shopping_bag.this)
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
                        shoppingbaglist();

                    } else {
                        Toast.makeText(Shopping_bag.this, R.string.not_updated, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink uu = new urlLink();
                JSONObject json = uu.product_deduct_qty(SavePreferences.getUserID(Shopping_bag.this), card_id, SavePreferences.getApplanguage(Shopping_bag.this));
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void product_choose_delivery(final String card_id) {

        isSelfCollect = "0";

        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Shopping_bag.this)
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
                        shoppingbaglist();
                    } else {
                        Toast.makeText(Shopping_bag.this, R.string.not_updated, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink uu = new urlLink();
                JSONObject json = uu.update_shipping_method(SavePreferences.getUserID(Shopping_bag.this), card_id, isSelfCollect, SavePreferences.getApplanguage(Shopping_bag.this));
                return json;
            }


        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void product_choose_selfcollect(final String card_id) {

        isSelfCollect = "1";

        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Shopping_bag.this)
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
                        shoppingbaglist();

                    } else {
                        Toast.makeText(Shopping_bag.this, R.string.not_updated, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink uu = new urlLink();
                JSONObject json = uu.update_shipping_method(SavePreferences.getUserID(Shopping_bag.this), card_id, isSelfCollect, SavePreferences.getApplanguage(Shopping_bag.this));
                return json;
            }


        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    class ShoppingListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ShoppingBagItem> items;

        public ShoppingListAdapter(Context context, ArrayList<ShoppingBagItem> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(context, R.layout.shoppinglistitemv2, null);
            final ShoppingBagItem item = items.get(position);
            final ViewHolder holder = new ViewHolder();
            ImageView img = (ImageView) convertView.findViewById(R.id.img);
            ImageView label_voucher = (ImageView) convertView.findViewById(R.id.image_label_voucher);
            ImageView up = (ImageView) convertView.findViewById(R.id.imageView4);
            ImageView down = (ImageView) convertView.findViewById(R.id.down);
            ImageView deleteitem = (ImageView) convertView.findViewById(R.id.deleteitem);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView pts = (TextView) convertView.findViewById(R.id.pts);
            TextView totalpts = (TextView) convertView.findViewById(R.id.totalpts);
            TextView productby = (TextView) convertView.findViewById(R.id.productby);
            holder.qty = (EditText) convertView.findViewById(R.id.qty);

            TextView tvunitprice = (TextView) convertView.findViewById(R.id.tvunitprice);
            TextView tvtotal = (TextView) convertView.findViewById(R.id.tvtotal);
            TextView tvshippingfee = (TextView) convertView.findViewById(R.id.tvshippingfee);
            TextView shippingfee = (TextView) convertView.findViewById(R.id.shippingfee);
            TextView tvtotalma = (TextView) convertView.findViewById(R.id.tvtotalma);
            TextView tvtotalmyr = (TextView) convertView.findViewById(R.id.tvtotalmyr);
            TextView totalma = (TextView) convertView.findViewById(R.id.totalma);
            TextView totalmyr = (TextView) convertView.findViewById(R.id.totalmyr);
            View wholeview = (View) convertView.findViewById(R.id.wholeview);
            TextView tvusevoucher = (TextView) convertView.findViewById(R.id.tvusevoucher);
            TextView tvtotalvouchervalue = (TextView) convertView.findViewById(R.id.tvtotalvouchervalue);
            TextView totalvouchervalue = (TextView) convertView.findViewById(R.id.totalvouchervalue);

            tvtotal.setVisibility(View.INVISIBLE);
            totalpts.setVisibility(View.INVISIBLE);

            //Log.i("1461get_redeemType", item.get_redeemType());

            if (item.getRedeem_type().equals("1")) {
                tvusevoucher.setText(Html.fromHtml("<u>" + getString(R.string.use_voucher) + "</u>"));
                tvusevoucher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("1445", item.getProduct_id());
                        Log.i("1445 cart", item.getCart_id());
                        /** shopping Bag use voucher**/
//                        Intent intent = new Intent(Shopping_bag.this, Voucher.class);
                        Intent intent = new Intent(Shopping_bag.this, VoucherSelectionActivity.class);
                        intent.putExtra("product_id", item.getProduct_id());
                        intent.putExtra("cart_id", item.getCart_id());
                        startActivityForResult(intent, 4);
                        //startActivityForResult(new Intent(Shopping_bag.this, Voucher.class), VOUCHER);
                    }
                });
            } else {
                tvusevoucher.setVisibility(View.GONE);
            }

            RadioButton delivery_RdbBtn = (RadioButton) convertView.findViewById(R.id.deliveryRdbBtn);
            delivery_RdbBtn.setText(getResources().getString(R.string.delivery));
            RadioButton selfcollect_RdbBtn = (RadioButton) convertView.findViewById(R.id.selfcollectRdbBtn);
            selfcollect_RdbBtn.setText(getResources().getString(R.string.selfcollect));
            //TextView selfcollectinfo = (TextView) convertView.findViewById(R.id.selfcollectInfo);
            //selfcollectinfo.setText(getResources().getString(R.string.selfcollectinfo));

            delivery_RdbBtn.setVisibility(View.GONE);
            selfcollect_RdbBtn.setVisibility(View.GONE);
            //selfcollectinfo.setVisibility(View.GONE);

            if (item.getIs_allowed_self_collect_item().equals("1")) {
                delivery_RdbBtn.setVisibility(View.VISIBLE);
                selfcollect_RdbBtn.setVisibility(View.VISIBLE);
                //selfcollectinfo.setVisibility(View.VISIBLE);
                wholeview.getLayoutParams().height = 800;
                wholeview.requestLayout();
            }

            if (isSelfCollect.equals("1")) {
                selfcollect_RdbBtn.setChecked(true);
                delivery_RdbBtn.setChecked(false);
            }else {
                selfcollect_RdbBtn.setChecked(false);
                delivery_RdbBtn.setChecked(true);
            }

            Log.d("myTag666", isSelfCollect);
            Log.d("myTag6662", item.getIs_allowed_self_collect_item());

            ImageLoader.getInstance().displayImage(item.getProduct_img(), new ImageViewAware(img, false));
            name.setText(Html.fromHtml(item.getProduct_name()));

            holder.qty.setText(item.getProduct_qty());
            pts.setText(getString(R.string.currency) + item.getAmount_point());

            productby.setText("Product By : " + item.getSupplier_name());
            productby.setText("");
            totalpts.setText(getString(R.string.currency) + item.getTotal_amount_point());

            totalma.setText(getString(R.string.currency) + item.getTotalma());
            totalmyr.setText(getString(R.string.currency) + item.getTotalmyr());
            totalvouchervalue.setText(getString(R.string.currency) + item.getTotalvouchervalue());

            tvshippingfee.setText(item.getSupplier_name());
            holder.qty.setEnabled(false);
            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int q = Integer.parseInt(holder.qty.getText().toString());
                    if (q != 1) {
                        product_deduct_qty(item.getCart_id());
                    }
                }
            });

            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product_add_qty(item.getCart_id());
                }
            });

            delivery_RdbBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delivery_RdbBtn.setChecked(true);
                    selfcollect_RdbBtn.setChecked(false);
                    Log.d("myTag1", "This is my delivery rdb");
                    product_choose_delivery(item.getCart_id());
                }
            });

            selfcollect_RdbBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delivery_RdbBtn.setChecked(false);
                    selfcollect_RdbBtn.setChecked(true);
                    Log.d("myTag2", "This is my selfcollect rdb");
                    product_choose_selfcollect(item.getCart_id());
                }
            });

            deleteitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                            .setMessage(getString(R.string.are_you_sure_to_delete_this_item))
                            .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dismissitem(items.get(position).getCart_id(), position);
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            });


            Log.d("voucher", item.getVoucher_status());
//            if (item.getVoucher_status().equals("1")) {
//                label_voucher.setVisibility(View.VISIBLE);
//            } else {
                label_voucher.setVisibility(View.GONE);
//            }
//            }

            Locale current_locale = getResources().getConfiguration().locale;
            if (current_locale.toString().toLowerCase().contains("en")) {
                label_voucher.setImageResource(R.drawable.label_mbooster_voucher);
            } else if (current_locale.toString().toLowerCase().contains("zh")) {
                label_voucher.setImageResource(R.drawable.label_mbooster_voucher_cn);
            }

            return convertView;
        }

        class ViewHolder {
            EditText qty;
        }
    }


//    class Shoppingbagitems {
//        String cart_id, product_id, product_name, product_qty, product_img, amount_point, total_amount_point, supplier_name, shippingcost, voucher_status, is_self_collect, is_allowed_self_collect_item, totalma, totalmyr, totalvouchervalue, redeem_type;
//
//        public Shoppingbagitems(String cart_id, String product_id, String product_name, String product_qty, String product_img, String amount_point, String total_amount_point, String supplier_name, String shippingcost, String voucher_status, String is_self_collect, String is_allowed_self_collect_item, String totalma, String totalmyr, String totalvouchervalue, String redeem_type) {
//            this.cart_id = cart_id;
//            this.product_id = product_id;
//            this.product_name = product_name;
//            this.product_qty = product_qty;
//            this.product_img = product_img;
//            this.amount_point = amount_point;
//            this.total_amount_point = total_amount_point;
//            this.supplier_name = supplier_name;
//            this.shippingcost = shippingcost;
//            this.voucher_status = voucher_status;
//            this.is_self_collect = is_self_collect;
//            this.is_allowed_self_collect_item = is_allowed_self_collect_item;
//            this.totalma = totalma;
//            this.totalmyr = totalmyr;
//            this.totalvouchervalue = totalvouchervalue;
//            this.redeem_type = redeem_type;
//        }
//
//        public String getSupplier_name() {
//            return supplier_name;
//        }
//
//        public String getProduct_name() {
//            return product_name;
//        }
//
//        public String getProduct_id() {
//            return product_id;
//        }
//
//        public String getTotal_amount_point() {
//            return total_amount_point;
//        }
//
//        public String getAmount_point() {
//            return amount_point;
//        }
//
//        public String getCart_id() {
//            return cart_id;
//        }
//
//        public String getProduct_img() {
//            return product_img;
//        }
//
//        public String getProduct_qty() {
//            return product_qty;
//        }
//
//        public void setProduct_qty(String product_qty) {
//            this.product_qty = product_qty;
//        }
//
//        public String getShippingcost() {
//            return shippingcost;
//        }
//
//        public String getVoucher_status() {
//            return voucher_status;
//        }
//
//        public String getProduct_shipping_method() {
//            return isSelfCollect;
//        }
//
//        public String getProduct_is_allowed_to_self_collect_item() {
//            return is_allowed_self_collect_item;
//        }
//
//        public String get_totalma() {
//            return totalma;
//        }
//
//        public String get_totalmyr() {
//            return totalmyr;
//        }
//
//        public String get_totalvouchervalue() {
//            return totalvouchervalue;
//        }
//
//        public String get_redeemType() {
//            return redeem_type;
//        }
//    }

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

        String pagename = "(Android) Shopping bag";
        mTracker.setScreenName(pagename);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mFirebaseAnalytics.setCurrentScreen(this, pagename, null /* class override */);
    }

    @Override
    public void networkAvailable() {
        network = 1;
    }

    @Override
    public void networkUnavailable() {

        network = 0;

        if (network == 0) {
            new AlertDialog.Builder(context, R.style.AlertDialogTheme)
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
                            ((Activity) context).finish();
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
}