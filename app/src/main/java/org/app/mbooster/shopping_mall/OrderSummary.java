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
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.ShoppingBagItem;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderSummary extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private EditText name, email, contact,icno, city, state, country, postcode;
    private String namestr, emailstr, contactstr, address1str, icnostr, totalpointstr;
    private String userId, pi_id;
    private ListView listView;
    private Toolbar toolbar;
    private ArrayList<ShoppingBagItem> items = new ArrayList<>();
    private ShoppingListAdapter adapter;

    private TextView totalpoint, address1, courier, trecking,title,toolbar_title,tvtotalpoint;
    private String from;


    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Log.d("masuk","orderSUmmary");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Helper.setupImageCache(OrderSummary.this);
        Helper.CheckMaintenance(this);

        context = activity = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);

        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);

        //toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handleOnBackPress();
                Intent next = new Intent(getApplicationContext(), MainActivity.class);
                next.putExtra("notification","history");
                startActivity(next);
            }
        });

        userId = SavePreferences.getUserID(this);
        Intent i = getIntent();
        i.getExtras();
        pi_id = i.getStringExtra("pi_id");
        from = i.getStringExtra("from");
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        contact = (EditText) findViewById(R.id.contact);
        icno = (EditText) findViewById(R.id.icno);
        address1 = (TextView) findViewById(R.id.address1);
        // address2 = (EditText) findViewById(R.id.address2);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        country = (EditText) findViewById(R.id.country);
        postcode = (EditText) findViewById(R.id.postcode);
        totalpoint = (TextView) findViewById(R.id.totalpoint);
        courier = (TextView) findViewById(R.id.courier);
        trecking = (TextView) findViewById(R.id.tracking);
        listView = (ListView) findViewById(R.id.listView);
        title = (TextView) findViewById(R.id.title);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tvtotalpoint = (TextView) findViewById(R.id.tvtotalpoint);

//        if(SavePreferences.getApplanguage(OrderSummary.this).equals("CN")){
//            title.setText("运输详情");
//            toolbar_title.setText("购物详情");
//            tvtotalpoint.setText("总计 (GST incl.)");
//        }
//        adapter = new CheckOutItemAdapter(OrderSummary.this, items);

        adapter = new ShoppingListAdapter(OrderSummary.this, items);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);


        purchasedetail(userId);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            handleOnBackPress();
        }
        return false;
    }

    public void handleOnBackPress() {

//        onclick back arrow back to previous page
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        this.finish();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        if (listAdapter.getCount() <= 1) return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public void purchasedetail(final String user_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(OrderSummary.this)
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
                        //JSONObject userinfo = jsonObject.getJSONObject("userinfo");
                        //namestr, emailstr, contactstr, address2str,address1str, citystr, statestr, countrystr, postcodestr;
                        namestr = jsonObject.getString("username");
                        emailstr = jsonObject.getString("email");
                        contactstr = jsonObject.getString("contact");
                        address1str = jsonObject.getString("address");
                        icnostr = jsonObject.getString("shipping_icno");
                        // address2str = jsonObject.getString("address2");
//                        citystr = jsonObject.getString("city");
//                        statestr = jsonObject.getString("state");
//                        countrystr = jsonObject.getString("country");
//                        postcodestr = jsonObject.getString("postcode");

                        name.setText(jsonObject.getString("username"));
                        address1.setText(jsonObject.getString("address"));
                        //address2.setText(userinfo.getString("address2"));
//                        postcode.setText(userinfo.getString("postcode"));
//                        city.setText(userinfo.getString("city"));
//                        state.setText(userinfo.getString("state"));
                        contact.setText(jsonObject.getString("contact"));
                        email.setText(jsonObject.getString("email"));
                        icno.setText(jsonObject.getString("icno"));
                        courier.setText(getString(R.string.courier_service) + jsonObject.getString("courier"));
                        trecking.setText(getString(R.string.tracking_code) + jsonObject.getString("tracking"));

//                        if(SavePreferences.getApplanguage(OrderSummary.this).equals("CN")){
//                            courier.setText("快递服务: " + jsonObject.getString("courier"));
//                            trecking.setText("跟踪代码: " + jsonObject.getString("tracking"));
//                        }
//                        country.setText(userinfo.getString("country"));

                        JSONArray array = jsonObject.getJSONArray("array");
                        items.addAll(JSonHelper.parseShoppingBagItem(array, null));
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject item = array.getJSONObject(i);
//                            items.add(new ShoppingBagItem(item.getString("cart_id"), item.getString("product_id"), item.getString("product_name"), item.getString("product_qty"), item.getString("product_img"), item.getString("amount_point"), item.getString("amount_airtime"), "",item.getString("shipping_cost")));
//                        }
                        double totalpts = 1000;
                        for (int i = 0; i < items.size(); i++) {
                            totalpts += Double.parseDouble(items.get(i).getAmount_point().replace(",",""));
                        }
                        totalpoint.setText(Helper.StringToTwoDecimal(String.valueOf(jsonObject.getString("total"))) + getResources().getString(R.string.currency));
                        totalpoint.setText(Helper.TWMoney(String.valueOf(jsonObject.getString("total"))) + getResources().getString(R.string.currency));
                        totalpointstr = Helper.StringToTwoDecimal(String.valueOf(totalpts));

                        if(from.equals("2")) {
                            startActivity(new Intent(OrderSummary.this, PaymentSuccessful.class));
                        }
                    }

                } catch (Exception e) {
                    LogHelper.error(e.getLocalizedMessage());
                } finally {
                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(listView);
                }

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.purchasedetail(user_id, pi_id);
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
            tvusevoucher.setVisibility(View.GONE);

            RadioButton delivery_RdbBtn = (RadioButton) convertView.findViewById(R.id.deliveryRdbBtn);
            delivery_RdbBtn.setText(getResources().getString(R.string.delivery));
            RadioButton selfcollect_RdbBtn = (RadioButton) convertView.findViewById(R.id.selfcollectRdbBtn);
            selfcollect_RdbBtn.setText(getResources().getString(R.string.selfcollect));
            //TextView selfcollectinfo = (TextView) convertView.findViewById(R.id.selfcollectInfo);
            //selfcollectinfo.setText(getResources().getString(R.string.selfcollectinfo));

            delivery_RdbBtn.setVisibility(View.GONE);
            selfcollect_RdbBtn.setVisibility(View.GONE);

            if (item.getIs_allowed_self_collect_item().equals("1")) {
                delivery_RdbBtn.setVisibility(View.VISIBLE);
                selfcollect_RdbBtn.setVisibility(View.VISIBLE);
                //selfcollectinfo.setVisibility(View.VISIBLE);
                wholeview.getLayoutParams().height = 800;
                wholeview.requestLayout();
            }

            if (item.getIs_self_collect().equals("1")) {
                selfcollect_RdbBtn.setChecked(true);
                delivery_RdbBtn.setChecked(false);
            }else {
                selfcollect_RdbBtn.setChecked(false);
                delivery_RdbBtn.setChecked(true);
            }


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
//                    int q = Integer.parseInt(holder.qty.getText().toString());
//                    if (q != 1) {
//                        product_deduct_qty(item.getCart_id());
//                    }
                }
            });

            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    product_add_qty(item.getCart_id());
                }
            });

            delivery_RdbBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            selfcollect_RdbBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            deleteitem.setVisibility(View.GONE);
            label_voucher.setVisibility(View.GONE);
            return convertView;
        }

        class ViewHolder {
            EditText qty;
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

        if (network == 0) {
            new AlertDialog.Builder(context,R.style.AlertDialogTheme)
                    .setTitle(getString(R.string.no_network_notification))
                    .setCancelable(false)

                    .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(isOnline()==false){
                                networkUnavailable();
                            }else{
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
