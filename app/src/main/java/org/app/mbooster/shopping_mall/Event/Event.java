package org.app.mbooster.shopping_mall.Event;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.listAdapter_folder.ProductListingAdapter;
import org.app.mbooster.model_folder.Category;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.ProductModel;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.Product.Product2;
import org.app.mbooster.shopping_mall.ProductSearch;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import io.apptik.widget.MultiSlider;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Event extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    private Context context;
    private int network = 0;

    private boolean showEvOption = false;
    private boolean showMaOption = false;
    private boolean bizUser = false;
    private boolean mtiUser = false;

    private AlertDialog alertDialog;
    private AssetManager assetManager;
    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Toolbar toolbar;
    private TextView toolbar_title;

    private String event_id, cat_id, maxprice, minprice;

    private GridViewWithHeaderAndFooter gridview;
    private List<ProductModel> productItems = new ArrayList<>();
    //    private GridAdapter adapter;
    private ProductListingAdapter adapter;

    private LinearLayout timer_countdown;
    private ImageView shopnow,imageView, day_tens, day_digits, hrs_tens, hrs_digits, min_tens, min_digits, sec_tens, sec_digits;
    private int[] timer_number = {R.drawable.timer_0, R.drawable.timer_1, R.drawable.timer_2, R.drawable.timer_3, R.drawable.timer_4, R.drawable.timer_5,
            R.drawable.timer_6, R.drawable.timer_7, R.drawable.timer_8, R.drawable.timer_9};

    private Spinner spinner;
    private TextView price_range;
    private static ArrayAdapter<String> adapter2;

    private RelativeLayout price_rangerl;

    ArrayList<String> strcategory_array = new ArrayList<>();
    ArrayList<Category> category_array = new ArrayList<>();
    private TextView noproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        context = activity = this;

        assetManager = getApplicationContext().getAssets();

        // firebase analytic
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
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

        Intent i = getIntent();
        i.getExtras();
        event_id = i.getStringExtra("event_id");
        Log.d("event_id", event_id);

        cat_id = "0";
        maxprice = "10000";
        minprice = "0";

        noproduct = (TextView) findViewById(R.id.noproduct);

        gridview = (GridViewWithHeaderAndFooter) findViewById(R.id.gridview);
//        adapter = new GridAdapter(this, productItems);
        adapter = new ProductListingAdapter(this, productItems, bizUser, mtiUser);
        gridview.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.spinner);
        price_range = (TextView) findViewById(R.id.price_range);

        price_rangerl = (RelativeLayout) findViewById(R.id.price_rangerl);

        price_rangerl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Event.this, R.style.AlertDialogTheme);
                TypefaceUtil.overrideFont(Event.this, "SERIF", "fonts/gotham_book.otf");
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.dialog_pricerange);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                MultiSlider multiSlider1 = (MultiSlider) dialog.findViewById(R.id.range_slider);

                final TextView min1 = (TextView) dialog.findViewById(R.id.minValue1);
                final TextView max1 = (TextView) dialog.findViewById(R.id.maxValue1);
                min1.setText(String.valueOf(multiSlider1.getThumb(0).getValue() * 100));
                max1.setText(String.valueOf(multiSlider1.getThumb(1).getValue() * 100));

                min1.setText(minprice);
                max1.setText(maxprice);

                multiSlider1.getThumb(0).setValue(Integer.parseInt(minprice) / 100);
                multiSlider1.getThumb(1).setValue(Integer.parseInt(maxprice) / 100);


                multiSlider1.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
                    @Override
                    public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                        if (thumbIndex == 0) {
                            min1.setText(String.valueOf(value * 100));
                        } else {
                            max1.setText(String.valueOf(value * 100));
                        }
                    }
                });

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        price_range.setText(min1.getText().toString() + " - " + max1.getText().toString());
                        minprice = min1.getText().toString();
                        maxprice = max1.getText().toString();
                        dialog.dismiss();
                        EventDetails(event_id, cat_id, maxprice, minprice);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventDetails(event_id, cat_id, maxprice, minprice);
                        price_range.setText("0 - 10000");
                        minprice = "0";
                        maxprice = "10000";
                        dialog.dismiss();
                    }
                });
            }
        });

        imageView = (ImageView) findViewById(R.id.image_view);
        shopnow = (ImageView) findViewById(R.id.shopnow);
        timer_countdown = (LinearLayout) findViewById(R.id.timer_countdown);
        day_tens = (ImageView) findViewById(R.id.day_tens);
        day_digits = (ImageView) findViewById(R.id.day_digits);
        hrs_tens = (ImageView) findViewById(R.id.hrs_tens);
        hrs_digits = (ImageView) findViewById(R.id.hrs_digits);
        min_tens = (ImageView) findViewById(R.id.min_tens);
        min_digits = (ImageView) findViewById(R.id.min_digits);
        sec_tens = (ImageView) findViewById(R.id.sec_tens);
        sec_digits = (ImageView) findViewById(R.id.sec_digits);

        shopnow.setVisibility(View.GONE);

         EventDetails(event_id, cat_id, maxprice, minprice);
//        Event_Load(event_id);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (productItems.get(position).getProduct_label().equals("5")) {
//                    Toast.makeText(context, "This item has been sold out", Toast.LENGTH_SHORT).show();
//                } else {
                ProductListingAdapter tempAdapter = (ProductListingAdapter) parent.getAdapter();
                ProductModel tempProduct = tempAdapter.getItem(position);
                if(tempProduct.getQuantity()>0) {
                    Intent i = new Intent(Event.this, Product2.class);
//                Log.i("productid", items.get(position).getProductid());
                    i.putExtra("productid", tempProduct.getProductid());
                    startActivity(i);
                }else{
                    LogHelper.debug(tempProduct.getProductname() + " sold out");
                }
//                    Intent intent = new Intent(Event.this, Product2.class);
//                    intent.putExtra("productid", productItems.get(position).getProductid());
//                    startActivity(intent);
//                }
            }
        });


    }

    private void Event_Load(final String event_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                category_array.clear();
                strcategory_array.clear();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {

                    if (jsonObject.getString("success").equals("1")) {
                        JSONArray array = jsonObject.getJSONArray("array");

                        category_array.add(new Category("0", "All"));
                        strcategory_array.add("All");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            category_array.add(new Category(json.getString("id"), json.getString("name")));
                            strcategory_array.add(json.getString("name"));
                        }

                        adapter2 = new ArrayAdapter<String>(Event.this, R.layout.spinner_item_divider, strcategory_array);
                        spinner.setAdapter(adapter2);
                        Log.d("cat num", strcategory_array.size() + "");
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cat_id = category_array.get(position).id;
                                EventDetails(event_id, cat_id, maxprice, minprice);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                LogHelper.debug("[Event_Load] 5");
                            }
                        });

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogHelper.error("[Event_Load][Error] " +e.getLocalizedMessage());
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().event_load(SavePreferences.getUserID(Event.this), event_id);
            }
        }
        new getinfo().execute();
    }

    private void EventDetails(final String event_id, final String category_id, final String maxprice, final String minprice) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                productItems.clear();
                adapter.notifyDataSetChanged();
                flowerDialog = new ACProgressFlower.Builder(Event.this)
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
                    LogHelper.debug("[EventDetails] " + jsonObject.toString());
                    if (jsonObject.getString("success").equals("1")) {
                        JSONObject details = jsonObject.getJSONObject("event_details");
                        String datetime = details.getString("event_end_datetime");
                        String event_img = details.getString("event_img");

                        showEvOption = JSonHelper.getObjBoolean(jsonObject, "showEvOption", false);
                        showMaOption = JSonHelper.getObjBoolean(jsonObject, "showMaOption", false);
                        bizUser = JSonHelper.getObjString(jsonObject, "bizUser").equals("1");
                        mtiUser = JSonHelper.getObjString(jsonObject, "showPV").equals("1");
                        EventCoundown(event_img, datetime);

                        JSONArray jsonArray = jsonObject.getJSONArray("event_products");
                        productItems.addAll(JSonHelper.parseProductList(jsonArray));
                        adapter.setShowEvOption(showEvOption);
                        adapter.setShowMaOption(showMaOption);
                        adapter.setBizUser(bizUser);
                        adapter.setMtiUserUser(mtiUser);
                        adapter.notifyDataSetChanged();

                        if (productItems.size() == 0) {
                            noproduct.setVisibility(View.VISIBLE);
                        } else {
                            noproduct.setVisibility(View.GONE);
                        }

                        if(details.get("show_countdown").equals("0")){
                            timer_countdown.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        adapter.notifyDataSetChanged();
                        noproduct.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().eventDetails(SavePreferences.getUserID(Event.this), event_id, category_id, maxprice, minprice);
            }
        }
        new getinfo().execute();
    }

    private void EventCoundown(final String event_img, final String datetime) {
        Log.d("countdown", "setting");
        ImageAware imageAware = new ImageViewAware(imageView);
        ImageLoader.getInstance().displayImage(event_img, imageAware);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        String dateStart = dateFormat.format(date);
        String dateStop = datetime;

        Date d1 = null;
        Date d2 = null;
        long init_milli = 0;
        try {
            d1 = dateFormat.parse(dateStart);
            d2 = dateFormat.parse(dateStop);

            //in milliseconds
            init_milli = d2.getTime() - d1.getTime();
            if (init_milli < 0) {
                init_milli = 0;
            }

            if (init_milli != 0) {
                //init timer
                long diffSeconds = init_milli / 1000 % 60;
                long diffMinutes = init_milli / (60 * 1000) % 60;
                long diffHours = init_milli / (60 * 60 * 1000) % 24;
                long diffDays = init_milli / (24 * 60 * 60 * 1000);

                sec_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffSeconds % 10)]));
                sec_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffSeconds / 10)]));
                min_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffMinutes % 10)]));
                min_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffMinutes / 10)]));
                hrs_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffHours % 10)]));
                hrs_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffHours / 10)]));
                day_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffDays % 10)]));
                day_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffDays / 10)]));
                //end init timer

                //counter
                CountDownTimer countDownTimer = new CountDownTimer(init_milli, 1000) {
                    @Override
                    public void onTick(long mf) {
    //                    Log.i("mf", String.valueOf(mf));
                        long diffSeconds = mf / 1000 % 60;
                        long diffMinutes = mf / (60 * 1000) % 60;
                        long diffHours = mf / (60 * 60 * 1000) % 24;
                        long diffDays = mf / (24 * 60 * 60 * 1000);

                        if (((int)( mf / 1000) % 10) == 9) {
                            sec_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffSeconds % 10)]));
                            sec_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffSeconds / 10)]));
                            min_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffMinutes % 10)]));
                            min_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffMinutes / 10)]));
                            hrs_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffHours % 10)]));
                            hrs_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffHours / 10)]));
                            day_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffDays % 10)]));
                            day_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffDays / 10)]));
                        } else {
                            sec_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int)(( mf / 1000) % 10)]));
                        }
                    }
                    @Override
                    public void onFinish() {
                        activity.finish();
                    }
                };

                //counter start
                countDownTimer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class GridAdapter extends BaseAdapter {
        Context context;
        List<ProductModel> items;
        LayoutInflater layoutInflater;

        public GridAdapter(Context context, List<ProductModel> items) {
            this.context = context;
            this.items = items;
            layoutInflater = LayoutInflater.from(context);
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

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.product_list_item2, null);
            }
            TextView pts = (TextView) convertView.findViewById(R.id.pts);
            TextView pts_discount = (TextView) convertView.findViewById(R.id.pts_discount);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView discount_perc = (TextView) convertView.findViewById(R.id.discount_perc);
            ImageView img = (ImageView) convertView.findViewById(R.id.img);
            ImageView label_voucher = (ImageView) convertView.findViewById(R.id.label_voucher);
            final ImageView addtobag = (ImageView) convertView.findViewById(R.id.addtobag);
            ImageView soldoutlabel = (ImageView) convertView.findViewById(R.id.soldoutlabel);
            LinearLayout discount_label = convertView.findViewById(R.id.discount_label);
            final ProductModel item = items.get(position);
            RelativeLayout eVoucherContainer = convertView.findViewById(R.id.e_voucher_icon);
            RelativeLayout maContainer = convertView.findViewById(R.id.m_a_icon);
            TextView eVoucherText = convertView.findViewById(R.id.e_voucher_text);
            TextView maText = convertView.findViewById(R.id.m_a_text);

            if(item.getDiscount_perc().equals("0")) {
                discount_label.setVisibility(View.GONE);
                pts_discount.setVisibility(View.INVISIBLE);
            }else{
                discount_label.setVisibility(View.VISIBLE);
                pts_discount.setVisibility(View.VISIBLE);
            }

            if(item.getVoucher_status().equals("1") && item.getMaxVoucherValue()>0) {
                eVoucherContainer.setVisibility(View.VISIBLE);
                eVoucherText.setText(String.valueOf(item.getMaxVoucherValue())+context.getString(R.string.postfix_ev));
            }else{
                eVoucherContainer.setVisibility(View.GONE);
            }

            if(item.getMaxMAValue()> 0 ) {
                maContainer.setVisibility(View.VISIBLE);
                maText.setText(String.valueOf(item.getMaxMAValue()));
            }else{
                maContainer.setVisibility(View.GONE);
            }

            discount_perc.setText(item.getDiscount_perc() + "%");

            if (item.getProduct_label().equals("5")) {
                soldoutlabel.setVisibility(View.VISIBLE);
                addtobag.setVisibility(View.GONE);
            } else {
                soldoutlabel.setVisibility(View.GONE);
                addtobag.setVisibility(View.VISIBLE);
            }

            label_voucher.setVisibility(View.GONE);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Event.this, Product2.class);
                    intent.putExtra("productid", item.getProductid());
                    startActivity(intent);
                }
            });



            ImageAware imageAware = new ImageViewAware(img);
            ImageLoader.getInstance().displayImage(item.getProductimg(), imageAware);

            pts.setText(context.getString(R.string.currency) + item.getProductpts());
            pts_discount.setText(context.getString(R.string.currency) + item.getAmountcost());
            pts_discount.setPaintFlags(pts_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            name.setText(item.getProductname());


            addtobag.setVisibility(View.GONE);

            return convertView;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void addtobag( final String productid, final String productqty, final String productcolor) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Event.this)
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
                    String response = jsonObject.getString("success");
                    if (response.equals("1")) {
                        Toast.makeText(Event.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } else if (response.equals("2")) {
                        Toast.makeText(Event.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.addtobag(SavePreferences.getUserID(Event.this), productid, productqty, productcolor, productcolor, SavePreferences.getApplanguage(Event.this),"");
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
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

        String pagename = "(Android) Event Page";
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
        if (alertDialog == null) {
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
                                EventDetails(event_id, cat_id, maxprice, minprice);

                            }

                        }
                    })
                    .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity) context).finish();
                        }
                    }).create();
        }

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
