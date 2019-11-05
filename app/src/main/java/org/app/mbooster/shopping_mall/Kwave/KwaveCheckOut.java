package org.app.mbooster.shopping_mall.Kwave;

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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Dialog.DialogFragmentUniversalPW;
import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.Paymentmethod;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.CheckOut;
import org.app.mbooster.shopping_mall.Payment.PaymentWebView;
import org.app.mbooster.shopping_mall.PaymentFailed;
import org.app.mbooster.shopping_mall.PaymentSuccessful;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import mcalls.mmspot.sdk.MMspotPaymentButton;
import mcalls.mmspot.sdk.RedemptionCallBack;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class KwaveCheckOut extends AppCompatActivity implements DialogFragmentUniversalPW.onSubmitListener, NetworkStateReceiver.NetworkStateReceiverListener {
    private EditText name, email, contact,icno, address2, country, postcode;
    private String namestr, emailstr, contactstr, address1str, address2str, citystr, statestr, countrystr, postcodestr, totalpointstr, usercredit, totalpointairtimestr;
    private String userId;
    private ListView listView;
    private Toolbar toolbar;
    private ArrayList<Shoppingbagitems> items = new ArrayList<>();
    private CheckOutItemAdapter adapter;
    private Button edit;
    private ArrayList<Paymentmethod> payment_items = new ArrayList<>();
    private RelativeLayout submit;
    private DialogFragmentUniversalPW a;
    private TextView totalpoint, address1, shippingfee, title, tvtotalpoint, toolbar_title, tvsubmit,bill_edit,ship_edit;

    private GridViewWithHeaderAndFooter gridview;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;
    private AlertDialog alertDialog;

    private TextView voucher_total_amount;
    private LinearLayout voucher_ly;
    private ArrayList<VoucherItem> voucher_items = new ArrayList<>();
    private String voucher_status = "0";
    private String voucher_qualify_status;
    private RelativeLayout voucher_entitlement;

    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AlertDialog dd;

    private String temp_tid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kwave_check_out);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

        Helper.setupImageCache(KwaveCheckOut.this);
        Helper.CheckMaintenance(this);

        context = activity = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
//        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userId = SavePreferences.getUserID(this);

        voucher_entitlement = (RelativeLayout) findViewById(R.id.voucher_entitlement);
        voucher_entitlement.setVisibility(View.GONE);
        voucher_total_amount = (TextView) findViewById(R.id.voucher_total_amount);
        voucher_ly = (LinearLayout) findViewById(R.id.voucher_ly);

        totalpoint = (TextView) findViewById(R.id.totalpoint);
        shippingfee = (TextView) findViewById(R.id.shippingfee);
        title = (TextView) findViewById(R.id.title);
        tvtotalpoint = (TextView) findViewById(R.id.tvtotalpoint);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tvsubmit = (TextView) findViewById(R.id.tvsubmit);
        gridview = (GridViewWithHeaderAndFooter) findViewById(R.id.gridview);

        submit = (RelativeLayout) findViewById(R.id.submit);
        Helper.buttonEffect(submit);

        LayoutInflater layoutInflater = LayoutInflater.from(KwaveCheckOut.this);
        View headerview = layoutInflater.inflate(R.layout.shopping_list_header, null);

        address1 = (TextView) headerview.findViewById(R.id.shipaddress);
        name = (EditText) headerview.findViewById(R.id.name);
        email = (EditText) headerview.findViewById(R.id.email);
        contact = (EditText) headerview.findViewById(R.id.contact);
        icno = (EditText) headerview.findViewById(R.id.icno);
        edit = (Button) headerview.findViewById(R.id.edit);
        ship_edit = (TextView)headerview.findViewById(R.id.ship_edit);
        bill_edit = (TextView)headerview.findViewById(R.id.bill_edit);
        edit.setVisibility(View.GONE);
        ship_edit.setVisibility(View.GONE);
        bill_edit.setVisibility(View.GONE);
        Helper.buttonEffect(edit);

        gridview.addHeaderView(headerview);
        adapter = new CheckOutItemAdapter(KwaveCheckOut.this, items, voucher_items);
        gridview.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(KwaveCheckOut.this, R.style.AlertDialogTheme);

                //builderSingle.setIcon(R.drawable.ic_launcher);
                //builderSingle.setTitle("Payment Method");
                LayoutInflater inflater = KwaveCheckOut.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
                builderSingle.setView(dialogView);


                Button btnmmspot = (Button) dialogView.findViewById(R.id.btnMMpsot);
                Button btnCreditCard = (Button) dialogView.findViewById(R.id.btnCreditCard);
                Button btnOn9banking = (Button) dialogView.findViewById(R.id.btnOn9banking);
                btnmmspot.setVisibility(View.GONE);
                btnCreditCard.setVisibility(View.GONE);
                btnOn9banking.setVisibility(View.GONE);

                ListView listView = (ListView) dialogView.findViewById(R.id.listview);

                TextView title = (TextView) dialogView.findViewById(R.id.title);

                PaymentBtnAdapter btnAdapter = new PaymentBtnAdapter(KwaveCheckOut.this, payment_items);
                listView.setAdapter(btnAdapter);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KwaveCheckOut.this, android.R.layout.select_dialog_singlechoice);

                builderSingle.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                dd = builderSingle.create();
//                btnmmspot.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dd.dismiss();
//                        String arg = getString(R.string.enter_mmspot_pw);
//                        String arg2 = getString(R.string.mmspot_confirmation);
//                        String arg3 = getString(R.string.confirm);
//                        String arg4 = getString(R.string.cancel);
//                        String arg5 = getString(R.string.mmspot_password);
//
//                        a = DialogFragmentUniversalPW.newInstance(arg, arg2, arg3, arg4, arg5);
//                        a.mListener = CheckOut.this;
//                        a.show(getFragmentManager(), "");
//
//                    }
//                });
//                btnCreditCard.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dd.dismiss();
//                        AlertDialog.Builder builderInner = new AlertDialog.Builder(CheckOut.this);
//                        builderInner.setMessage(R.string.comming_soon);
//                        builderInner.setTitle(getString(R.string.credit_card));
//                        //}
//                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        builderInner.show();
//                    }
//                });
//                btnOn9banking.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dd.dismiss();
//                        AlertDialog.Builder builderInner = new AlertDialog.Builder(CheckOut.this);
//                        builderInner.setMessage(R.string.comming_soon);
//                        builderInner.setTitle(R.string.online_banking);
//                        //}
//                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        builderInner.show();
//
//                    }
//                });
//                //dd.getButton(dd.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorbutton));
//
                dd.show();

            }
        });


//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(CheckOut.this, EditSBShipping.class);
//                i.putExtra("city", citystr);
//                i.putExtra("state", statestr);
//                i.putExtra("postcode", postcodestr);
//                i.putExtra("username", namestr);
//                i.putExtra("contact", contactstr);
//                i.putExtra("email", emailstr);
//                i.putExtra("addr1", address1str);
//                i.putExtra("addr2", address2str);
//                i.putExtra("country", countrystr);
//                startActivityForResult(i, 1);
//
//            }
//        });

        checkoutLoad(userId);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            Intent i = getIntent();
            startActivity(i);
            this.finish();

        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            {
                Intent i = new Intent(KwaveCheckOut.this, MainActivity.class);
                i.putExtra("phistory", "1");
                setResult(RESULT_OK, i);
                this.finish();
            }
        }

    }

    @Override
    public void setOnSubmitListener(String arg) {
        if (arg.equals(getString(R.string.cancel))) {
            a.dismiss();
        } else {
            //arg as password;
//            check_out(userId, arg);
//            check_out_test();
            Helper.CloseKeyboard(KwaveCheckOut.this);
        }
    }

//    private void check_out_test() {
//        Intent i = new Intent(CheckOut.this, PaymentSuccessful.class);
//
//        startActivity(i);
//    }

//    public void check_out(final String user_id, final String pw) {
//        class getinfo extends AsyncTask<String, String, JSONObject> {
//            ACProgressFlower flowerDialog;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                flowerDialog = new ACProgressFlower.Builder(CheckOut.this)
//                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
//                        .themeColor(getResources().getColor(R.color.colorbutton))
//                        .fadeColor(Color.GRAY).build();
//                if (!flowerDialog.isShowing()) {
//                    flowerDialog.show();
//                }
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                super.onPostExecute(jsonObject);
//                if (flowerDialog.isShowing()) {
//                    flowerDialog.dismiss();
//                }
//                try {
//                    if (jsonObject.getString("success").equals("1")) {
//
//                        Toast.makeText(CheckOut.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(CheckOut.this, PaymentSuccessful.class);
//
//                        startActivityForResult(i, 2);
//
//                    } else {
//                        Toast.makeText(CheckOut.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(CheckOut.this, PaymentFailed.class);
//
//                        startActivityForResult(i, 2);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... params) {
//                urlLink url = new urlLink();
////                JSONObject json = url.check_out(user_id, pw, totalpointstr, namestr, emailstr, address1str, address2str, citystr, statestr, postcodestr, countrystr, contactstr);
//                JSONObject json = url.check_out2(user_id, pw, totalpointstr, namestr, emailstr, address1str, address2str, citystr, statestr, postcodestr, countrystr, contactstr, temp_tid);
//                return json;
//            }
//        }
//        getinfo ge = new getinfo();
//        ge.execute();
//    }


    public void checkoutLoad(final String user_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(KwaveCheckOut.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                flowerDialog.setCancelable(false);
                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
                items.clear();
                payment_items.clear();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
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

//                        name.setText(userinfo.getString("username"));
//                        address1.setText(address1str + " " + address2str + "\n" + citystr + " " + postcodestr + " " + statestr + "\n" + countrystr);
//
//                        address1.setText(address1str + "\n" + address2str + "\n" + citystr + " " + postcodestr + " " + statestr + "\n" + countrystr);
//                        if(address2str.equals("")){
//                            address1.setText(address1str  + "\n" + citystr + " " + postcodestr + " " + statestr + "\n" + countrystr);
//                        }

                        address1.setText(namestr + "\n" + emailstr + "\n" + contactstr + "\n" + userinfo.getString("icno") + "\n\n" + address1str + ",\n" + citystr + ", " + postcodestr + ", " + statestr + "," + countrystr);

//                        contact.setText(userinfo.getString("contact"));
//                        email.setText(userinfo.getString("email"));
//                        icno.setText(userinfo.getString("icno"));

                        temp_tid = jsonObject.getString("temp_tid");
                        JSONArray array = jsonObject.getJSONArray("array");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            items.add(new Shoppingbagitems(item.getString("cart_id"), item.getString("product_id"), item.getString("product_name"), item.getString("product_qty"), item.getString("product_img"), item.getString("amount_point"), item.getString("total_product_point"), item.getString("amount_airtime"),
                                    item.getString("supplier_name"), item.getString("shipping_cost"), item.getString("voucher_status")));
                        }

                        shippingfee.setText(": " + getString(R.string.currency) + jsonObject.getString("shipping_cost"));
                        totalpoint.setText(getResources().getString(R.string.currency) + jsonObject.getString("total"));

                        totalpointstr = jsonObject.getString("total").replaceAll(",", "");
//                        usercredit = jsonObject.getString("usercredit").replaceAll(",", "");
//                        totalpointairtimestr = jsonObject.getString("grant_total_mairtime").replace(",", "");
                        voucher_status = jsonObject.getString("voucher_status");
                        if (jsonObject.getString("voucher_status").equals("1")) {

                            voucher_ly.setVisibility(View.VISIBLE);
                            voucher_total_amount.setText(" " + getString(R.string.currency) + jsonObject.getString("voucher_amount"));
                            voucher_total_amount.setText(" " + getString(R.string.currency) + jsonObject.getString("voucher_discount"));

                            JSONArray voucher_array = jsonObject.getJSONArray("voucher_array");
                            for (int i = 0; i < voucher_array.length(); i++) {
                                JSONObject json = voucher_array.getJSONObject(i);
                                voucher_items.add(new VoucherItem(json.getString("voucher_code"), json.getString("amount"), json.getString("discount")));
                            }
                        } else {
                            voucher_ly.setVisibility(View.GONE);
                        }

                        if (jsonObject.getString("voucher_qualify").equals("1")) {
                            voucher_entitlement.setVisibility(View.VISIBLE);
                        }
                        voucher_qualify_status = jsonObject.getString("voucher_qualify");
                        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView absListView, int i) {
                                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                                    Log.i("a", "scrolling stopped...");


                                    if (voucher_qualify_status.equals("1")) {
                                        voucher_entitlement.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    voucher_entitlement.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                            }
                        });

                        JSONArray jsonArray = jsonObject.getJSONArray("payment_methods");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            payment_items.add(new Paymentmethod(jo.getString("id"), jo.getString("title"), jo.getString("img_url"), jo.getString("payment_link"), jo.getString("status")));
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.getcheckoutinfoKwave(user_id,SavePreferences.getApplanguage(KwaveCheckOut.this));
                return json;
            }
        }

        getinfo ge = new getinfo();
        ge.execute();
    }

    private class PaymentBtnAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Paymentmethod> items;
        private LayoutInflater layoutInflater;

        public PaymentBtnAdapter(Context context, ArrayList<Paymentmethod> items) {
            layoutInflater = LayoutInflater.from(context);
            this.items = items;
            this.context = context;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = layoutInflater.inflate(R.layout.payment_btn_list_item2, null);
            //Button btn = (Button) view.findViewById(R.id.btn);

            final Paymentmethod item = items.get(position);
            ImageView btn = (ImageView) view.findViewById(R.id.btn);
            final MMspotPaymentButton mmspot_paymentbt = (MMspotPaymentButton) view.findViewById(R.id.mmspot_paymentbt);

            ImageLoader.getInstance().displayImage(item.img, new ImageViewAware(btn, false));

            final String link = item.payment_link;
            if (link.equals("")) {
                mmspot_paymentbt.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mmspot_paymentbt.callOnClick();
                    }
                });
                mmspot_paymentbt.setBackground(getResources().getDrawable(R.drawable.mmspot_logo));
                mmspot_paymentbt.mmspotPaymentInfo(context, totalpointstr, "Android " + temp_tid);

                mmspot_paymentbt.redemptionResult(new RedemptionCallBack() {
                    @Override
                    public void onSuccess(String status, String transaction_id,String reference, String datetime, String merchant_name, String amount_paid, String mairtime, String mrs, String message) {
                        System.out.println(status);
                        System.out.println(transaction_id);
                        System.out.println(reference);
                        System.out.println(datetime);
                        System.out.println(merchant_name);
                        System.out.println(amount_paid);
                        System.out.println(mairtime);
                        System.out.println(mrs);
                        System.out.println(message);

                        PaymentProcess(userId, transaction_id, temp_tid, datetime, message);
                    }

                    @Override
                    public void onError(String status, String message) {
                        System.out.println(status);
                        System.out.println(message);
                        Intent intent = new Intent(context, PaymentFailed.class);
                        intent.putExtra("Errmessage", message);
                        startActivity(intent);
                    }

                    @Override
                    public void onOTP(String s) {
                        //temp for dev
                        System.out.println(a);
                    }
                });



            } else {
                btn.setVisibility(View.VISIBLE);
                mmspot_paymentbt.setVisibility(View.GONE);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (link.equals("")) {
                            dd.dismiss();

//                        if (validateCheckOut()) {
//
//                            String arg = getString(R.string.enter_mmspot_pw);
//                            String arg2 = getString(R.string.mmspot_confirmation);
//                            String arg3 = getString(R.string.confirm);
//                            String arg4 = getString(R.string.cancel);
//                            String arg5 = getString(R.string.mmspot_password);
//
//                            a = DialogFragmentUniversalPW.newInstance(arg, arg2, arg3, arg4, arg5);
//                            a.mListener = CheckOut.this;
//                            a.show(getFragmentManager(), "");
//                        }


                        } else {
                            Intent intent = new Intent(KwaveCheckOut.this, PaymentWebView.class);
                            intent.putExtra("url", item.payment_link);
                            startActivity(intent);
                        }
                    }
                });
            }

            return view;
        }
    }

    private void PaymentProcess(final String user_id, final String getwayTID, final String temp_tid, final String paymentDate, final String message) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(KwaveCheckOut.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                flowerDialog.setCancelable(false);
                try {
                    if (!flowerDialog.isShowing()) {
                        flowerDialog.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if (flowerDialog.isShowing()) {
                        flowerDialog.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        Intent intent = new Intent(context, PaymentSuccessful.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().PaymentProcess(userId, getwayTID, temp_tid, paymentDate);
            }
        }
        new getinfo().execute();
    }

    class CheckOutItemAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Shoppingbagitems> items;
        private ArrayList<VoucherItem> voucher_items;

        public CheckOutItemAdapter(Context context, ArrayList<Shoppingbagitems> items, ArrayList<VoucherItem> voucher_items) {
            this.context = context;
            this.items = items;
            this.voucher_items = voucher_items;
        }

        @Override
        public int getCount() {
            if (voucher_status.equals("0")) {
                return items.size();
            } else {
                return items.size() + voucher_items.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position < items.size()) {
                convertView = View.inflate(context, R.layout.shoppinglistitem, null);
                final Shoppingbagitems item = items.get(position);

                ImageView img = (ImageView) convertView.findViewById(R.id.img);
                ImageView label_voucher = (ImageView) convertView.findViewById(R.id.image_label_voucher);
                ImageView up = (ImageView) convertView.findViewById(R.id.imageView4);
                ImageView down = (ImageView) convertView.findViewById(R.id.down);
                ImageView deleteitem = (ImageView) convertView.findViewById(R.id.deleteitem);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView pts = (TextView) convertView.findViewById(R.id.pts);
                TextView totalpts = (TextView) convertView.findViewById(R.id.totalpts);
                TextView productby = (TextView) convertView.findViewById(R.id.productby);
                EditText qty = (EditText) convertView.findViewById(R.id.qty);

                TextView tvunitprice = (TextView) convertView.findViewById(R.id.tvunitprice);
                TextView tvtotal = (TextView) convertView.findViewById(R.id.tvtotal);
                TextView tvshippingfee = (TextView) convertView.findViewById(R.id.tvshippingfee);
                TextView shippingfee = (TextView) convertView.findViewById(R.id.shippingfee);

                ImageLoader.getInstance().displayImage(item.getProduct_img(), new ImageViewAware(img, false));

                name.setText(Html.fromHtml(item.getProduct_name()));

                qty.setText(item.getProduct_qty());
                qty.setEnabled(false);
                Helper.disableEditText(qty);
                pts.setText((getString(R.string.currency) + item.getAmount_point()));
                productby.setText("");
                totalpts.setText(getString(R.string.currency) + item.getTotal_amount_point());
                up.setVisibility(View.INVISIBLE);
                down.setVisibility(View.INVISIBLE);
                deleteitem.setVisibility(View.INVISIBLE);

                tvshippingfee.setText(item.getSupplier_name());

                if (item.getVoucher_status().equals("1")) {
                    label_voucher.setVisibility(View.VISIBLE);
                } else {
                    label_voucher.setVisibility(View.INVISIBLE);
                }

                Locale current_locale = getResources().getConfiguration().locale;
                if (current_locale.toString().toLowerCase().contains("en")) {
                    label_voucher.setImageResource(R.drawable.label_mbooster_voucher);
                } else if (current_locale.toString().toLowerCase().contains("zh")) {
                    label_voucher.setImageResource(R.drawable.label_mbooster_voucher_cn);
                }
            } else if (position == items.size()) {
                convertView = View.inflate(context, R.layout.checkout_voucher_header, null);
            } else {
                convertView = View.inflate(context, R.layout.checkout_voucher_item, null);

                TextView code = (TextView) convertView.findViewById(R.id.code);
                TextView amount = (TextView) convertView.findViewById(R.id.amount);

                code.setText(voucher_items.get(position - items.size() - 1).code);
                amount.setText(voucher_items.get(position - items.size() - 1).discount + "%");
            }

            return convertView;
        }
    }


    public boolean validateCheckOut() {
        String t = totalpointstr.trim();
        t = t.replace(",", "");
        t = t.replace("MYR", "");
        t = t.replace(" ", "");

        String userC = usercredit.trim();
        userC = userC.replace(",", "");
        userC = userC.replace("MYR", "");
        userC = userC.replace(" ", "");

//        if (SavePreferences.getUserID(CheckOut.this).equals("20002")) {
//            return true;
//        } else {

        if (Double.parseDouble(t) > Double.parseDouble(userC)) {
            Toast.makeText(this, R.string.invalid_mairtime, Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
//        }

    }

    class Shoppingbagitems {
        String cart_id, product_id, product_name, product_qty, product_img, amount_point, total_amount_point, amount_airtime, supplier_name, shippingcost, voucher_status;

        public Shoppingbagitems(String cart_id, String product_id, String product_name, String product_qty, String product_img, String amount_point, String total_amount_point, String amount_airtime, String supplier_name, String shippingcost, String voucher_status) {
            this.cart_id = cart_id;
            this.product_id = product_id;
            this.product_name = product_name;
            this.product_qty = product_qty;
            this.product_img = product_img;
            this.amount_point = amount_point;
            this.total_amount_point = total_amount_point;
            this.amount_airtime = amount_airtime;
            this.supplier_name = supplier_name;
            this.shippingcost = shippingcost;
            this.voucher_status = voucher_status;
        }

        public String getSupplier_name() {
            return supplier_name;
        }

        public String getProduct_name() {
            return product_name;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getAmount_airtime() {
            return amount_airtime;
        }

        public String getAmount_point() {
            return amount_point;
        }

        public String getCart_id() {
            return cart_id;
        }

        public String getProduct_img() {
            return product_img;
        }

        public String getProduct_qty() {
            return product_qty;
        }

        public void setProduct_qty(String product_qty) {
            this.product_qty = product_qty;
        }

        public String getShippingcost() {
            return shippingcost;
        }

        public String getTotal_amount_point() {
            return total_amount_point;
        }

        public String getVoucher_status() {
            return voucher_status;
        }
    }

    class Shoppingbagvendor {
        String name;
        String subtotal;
        String subshipping_cost;
        ArrayList<Shoppingbagitems> items;

        public Shoppingbagvendor(String name, ArrayList<Shoppingbagitems> items, String subotal, String subshipping_cost) {
            this.name = name;
            this.items = items;
            this.subtotal = subotal;
            this.subshipping_cost = subshipping_cost;
        }

        public ArrayList<Shoppingbagitems> getItems() {
            return items;
        }

        public String getName() {
            return name;
        }

        public String getSubshipping_cost() {
            return subshipping_cost;
        }

        public String getSubtotal() {
            return subtotal;
        }
    }

    class VoucherItem {
        String code, amount ,discount;

        public VoucherItem(String code, String amount,String discount) {
            this.code = code;
            this.amount = amount;
            this.discount = discount;
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

        String pagename = "(Android) Check Out";
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
//                            checkoutLoad(userId);
                            ((Activity) context).finish();
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
