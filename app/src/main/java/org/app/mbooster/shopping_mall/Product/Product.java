package org.app.mbooster.shopping_mall.Product;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.WrapContentViewPager;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.DescriptionAnimation2;
import org.app.mbooster.shopping_mall.Shopping_bag;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Product extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private List<String> spinneritemscolor;
    private String productid, userId;
    private ArrayList<String> imgs = new ArrayList<>();
    private String product_id;
    private String product_name;
    private String amount_point;
    private String amount_airtime;
    private String supplier_name;
    private String color;
    private String wished;
    private TextView name, pts,pts2, productby, nationwide, norefund, toolbar_title, tvaddtobag, tvqty, originaprice;
    private Spinner spinner;
    private EditText quantity;
    private ArrayList<Tabs> tabs = new ArrayList<>();

    private TabLayout tabLayout;
    private WrapContentViewPager viewPager;

    private SliderLayout mDemoSlider;
    private Toolbar toolbar;
    static ArrayAdapter<String> adapter2;
    private TextView stockstatus, stockstatus2, shippingfree;
    private LinearLayout spinners, addtobag;
    private List<String> spinneritemsqty;
    private Button buynow;
    private Integer ProductQTT;
    private String TAG = "Product";
    private RelativeLayout price_rl;

    private ACProgressFlower flowerDialog;

    ImageView iamgeview_new_item;
    ImageView label_voucher;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    private Context context;
    private int network = 0;

    private AlertDialog alertDialog;

    private AssetManager assetManager;

    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        context = activity = this;

        LogHelper.info("[Page][Product]");

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
                Intent next = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(next);
            }
        });
        Intent i = getIntent();
        i.getExtras();
        productid = i.getStringExtra("productid");
        //Log.i("productid",productid);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        userId = SavePreferences.getUserID(this);
        name = (TextView) findViewById(R.id.name);
        pts = (TextView) findViewById(R.id.pts);
        pts2 = (TextView) findViewById(R.id.pts2);
        productby = (TextView) findViewById(R.id.productby);
        nationwide = (TextView) findViewById(R.id.nationwide);
        norefund = (TextView) findViewById(R.id.norefund);
        originaprice = (TextView) findViewById(R.id.originaprice);
        iamgeview_new_item = (ImageView) findViewById(R.id.iamgeview_new_item);
        label_voucher = (ImageView) findViewById(R.id.label_voucher);
        price_rl = (RelativeLayout) findViewById(R.id.price_rl);

//        desc = (TextView) findViewById(R.id.textmoldesc);
        spinner = (Spinner) findViewById(R.id.spinner);
        tvqty = (TextView) findViewById(R.id.tvqty);
//        keyf = (TextView) findViewById(R.id.description);
//        tvdesc = (TextView) findViewById(R.id.dd);
        tvaddtobag = (TextView) findViewById(R.id.tvaddtobag);

        flowerDialog = new ACProgressFlower.Builder(Product.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(getResources().getColor(R.color.colorbutton))
                .fadeColor(Color.GRAY).build();
        //spinner2 = (Spinner) findViewById(R.id.spinner2);
        quantity = (EditText) findViewById(R.id.quantity);
        quantity.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (quantity.getText().toString().matches("^0")) {
                    // Not allowed for 0 input heading
                    quantity.setText("");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });


        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels / 10 * 4;
        android.view.ViewGroup.LayoutParams layoutParams = mDemoSlider.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mDemoSlider.setLayoutParams(layoutParams);
        stockstatus = (TextView) findViewById(R.id.stockstatus);
        stockstatus2 = (TextView) findViewById(R.id.stockstatus2);
        shippingfree = (TextView) findViewById(R.id.shippingfree);
        buynow = (Button) findViewById(R.id.buynow);

        // LanguageSetting();
        spinners = (LinearLayout) findViewById(R.id.spinners);
        addtobag = (LinearLayout) findViewById(R.id.addtobag);

        getProductDetails();

        addtobag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinneritemscolor.size() != 0) {
                    if (addtobagvalidate()) {
//                        Log.i("userid", userId);
//                        Log.i("product_id", productid);
//                        Log.i("product_qty", quantity.getText().toString());
//                        Log.i("product_color", spinner.getSelectedItem().toString());
                        addtobag(userId, productid, quantity.getText().toString(), spinner.getSelectedItem().toString());


                    }
                } else {
//                    if (SavePreferences.getApplanguage(Product.this).equals("CN")) {
//                        Toast.makeText(Product.this, "此物品现在没有库存.", Toast.LENGTH_SHORT).show();
//                    } else {
                    Toast.makeText(Product.this, R.string.there_is_no_availalbe_stock, Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });

        //buynow
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinneritemscolor.size() != 0) {
                    if (addtobagvalidate()) {
//                        Log.i("userid", userId);
//                        Log.i("product_id", productid);
//                        Log.i("product_qty", quantity.getText().toString());
//                        Log.i("product_color", spinner.getSelectedItem().toString());
                        buynow(userId, productid, quantity.getText().toString(), spinner.getSelectedItem().toString());
                    }
                } else {
//                    if (SavePreferences.getApplanguage(Product.this).equals("CN")) {
//                        Toast.makeText(Product.this, "此物品现在没有库存.", Toast.LENGTH_SHORT).show();
//                    } else {
                    Toast.makeText(Product.this, R.string.there_is_no_availalbe_stock, Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (flowerDialog.isShowing()) {
            flowerDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        Intent next = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(next);
    }

    private void initializeTAB() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (WrapContentViewPager) findViewById(R.id.viewpager);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int w = size.x;


        viewPager.setAdapter(new SamplePagerAdapter(tabs));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.reMeasureCurrentPage(position);
                Log.i("position", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public boolean addtobagvalidate() {
        if (quantity.getText().toString().length() == 0) {

            Toast.makeText(this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            return true;
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

//                    Log.i("product666",jsonObject.toString());

                    if (response.equals("1")) {
                        JSONArray imgarray = jsonObject.getJSONArray("image");

                        product_id = jsonObject.getString("product_id");
                        product_name = jsonObject.getString("product_name");
                        amount_point = jsonObject.getString("amount_point");
                        amount_airtime = jsonObject.getString("amount_airtime");
                        supplier_name = jsonObject.getString("supplier_name");

                        TextView persentsign = (TextView) findViewById(R.id.persentsign);
                        TextView persentage = (TextView) findViewById(R.id.persentage);
                        TextView persentoff = (TextView) findViewById(R.id.persentoff);
                        final Typeface tvFont2 = Typeface.createFromAsset(assetManager, "fonts/gotham_book_bold.ttf");
                        persentage.setTypeface(tvFont2);
                        persentoff.setTypeface(tvFont2);

                        persentage.setText(jsonObject.getString("discount_perc"));
                        name.setText(Html.fromHtml(product_name));

//                        loadkeyf = jsonObject.getString("key_features");
//                        loadinthebox = jsonObject.getString("in_the_box");
//                        loadwarranty = jsonObject.getString("warranty");
//                        loadproductspec = jsonObject.getString("mobile_desc");

                        label_voucher.setVisibility(View.GONE);
                        iamgeview_new_item.setVisibility(View.GONE);
                        if(jsonObject.getString("voucher").equals("1")) {
                            label_voucher.setVisibility(View.VISIBLE);
                        }
                        if(jsonObject.getString("product_label").equals("1")){
                            iamgeview_new_item.setVisibility(View.VISIBLE);
                        }

                        Locale current_locale = getResources().getConfiguration().locale;
                        if (current_locale.toString().toLowerCase().contains("en")) {
                            iamgeview_new_item.setImageResource(R.drawable.label_new);
                            label_voucher.setImageResource(R.drawable.label_mbooster_voucher);
                        } else if (current_locale.toString().toLowerCase().contains("zh")) {
                            iamgeview_new_item.setImageResource(R.drawable.label_new_cn);
                            label_voucher.setImageResource(R.drawable.label_mbooster_voucher_cn);
                        }


                        JSONArray jsontabs = jsonObject.getJSONArray("tabs");
                        for (int i = 0; i < jsontabs.length(); i++) {
                            JSONObject jtab = jsontabs.getJSONObject(i);
                            tabs.add(new Tabs(jtab.getString("name"), jtab.getString("details")));
                        }

                        initializeTAB();

                        pts.setText(getResources().getString(R.string.currency) + amount_point);
                        pts2.setText(getResources().getString(R.string.currency) + amount_point);
                        productby.setText(jsonObject.getString("supplier_name"));


                        try {
                            if (jsonObject.getString("amount_cost").equals("0.00") || jsonObject.getString("amount_cost").equals("0")) {
                                price_rl.setVisibility(View.INVISIBLE);
                                originaprice.setText("");
                                originaprice.setVisibility(View.GONE);
                                persentsign.setVisibility(View.GONE);
                                persentage.setVisibility(View.GONE);
                                persentoff.setVisibility(View.GONE);
                                pts.setTextColor(Color.BLACK);
                                pts2.setTextColor(Color.BLACK);
                            } else {
                                price_rl.setBackgroundResource(R.drawable.mobile_item_price2);
                                originaprice.setText(getResources().getString(R.string.currency) + jsonObject.getString("amount_cost"));
                                originaprice.setPaintFlags(originaprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                originaprice.setVisibility(View.VISIBLE);
                                persentsign.setVisibility(View.VISIBLE);
                                persentage.setVisibility(View.VISIBLE);
                                persentoff.setVisibility(View.VISIBLE);
                                pts.setTextColor(Color.WHITE);

                            }
                        } catch (Exception e) {
                            //if json not return 0.00 in string
                            originaprice.setText("");
                        }

                        shippingfree.setText(getString(R.string.shipping_fee) + " " + jsonObject.getString("product_shipping"));

                        HashMap<String, String> url_maps = new HashMap<String, String>();

                        for (int i = 0; i < imgarray.length(); i++) {

                            Log.i("product img", urlLink.nonePath + "products/" + imgarray.getString(i));
                            imgs.add(imgarray.getString(i));

                            JSONObject jj = imgarray.getJSONObject(i);
                            url_maps.put(String.valueOf(i), jj.getString("product_img"));
                        }


                        for (String name : url_maps.keySet()) {
                            //CustomImageSliderView cc = new CustomImageSliderView(Product.this);
                            TextSliderView textSliderView = new TextSliderView(Product.this);
                            // initialize a SliderLayout
                            textSliderView
                                    //.description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside);

                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", name);

                            mDemoSlider.addSlider(textSliderView);


                        }
                        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mDemoSlider.setCustomAnimation(new DescriptionAnimation2());
                        mDemoSlider.stopAutoCycle();
                        mDemoSlider.animate().cancel();

                        JSONArray jsarray = jsonObject.getJSONArray("color");
                        spinneritemscolor = new ArrayList<>();
                        spinneritemsqty = new ArrayList<>();
                        for (int i = 0; i < jsarray.length(); i++) {
                            if (!jsarray.getJSONObject(i).getString("product_qty").equals("0")) {
                                spinneritemscolor.add(jsarray.getJSONObject(i).getString("product_color"));
                                spinneritemsqty.add(jsarray.getJSONObject(i).getString("product_qty"));

                            }
                        }
                        if (spinneritemscolor.size() != 0) {
                            adapter2 = new ArrayAdapter<String>(Product.this, R.layout.spinner_item_divider, spinneritemscolor);
                            spinner.setAdapter(adapter2);
                        } else {
                            stockstatus.setVisibility(View.GONE);
                            spinners.setVisibility(View.INVISIBLE);
                            stockstatus2.setVisibility(View.VISIBLE);
                            //stockstatus.setTextColor(Color.RED);
                        }

                        stockstatus.setVisibility(View.GONE);
                        stockstatus2.setVisibility(View.GONE);

                        if (spinneritemscolor.size() == 1 && spinneritemscolor.get(0).equals("")) {
                            spinner.setVisibility(View.GONE);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.getProductDetails(userId, productid, SavePreferences.getApplanguage(Product.this));

                int maxLogSize = 1000;
                for (int i = 0; i <= json.toString().length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > json.toString().length() ? json.toString().length() : end;
                    Log.v(TAG, json.toString().substring(start, end));
                }
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void addtobag(final String userId, final String productid, final String productqty, final String productcolor) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Product.this)
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
                        Toast.makeText(Product.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } else if (response.equals("2")) {
                        Toast.makeText(Product.this, R.string.out_of_stock, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.addtobag(userId, productid, productqty, productcolor, productcolor, SavePreferences.getApplanguage(Product.this),"");
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void buynow(final String userId, final String productid, final String productqty, final String productcolor) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Product.this)
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
                        Toast.makeText(Product.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } else if (response.equals("2")) {
                        Toast.makeText(Product.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                    Intent i = new Intent(Product.this, Shopping_bag.class);
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.addtobag(userId, productid, productqty, productcolor,productcolor,SavePreferences.getApplanguage(Product.this),"");
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    class Tabs {
        String title, details;

        public Tabs(String title, String details) {
            this.title = title;
            this.details = details;
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

        String pagename = "(Android) Product Page";
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
                                getProductDetails();
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


    class SamplePagerAdapter extends PagerAdapter {

        ArrayList<Tabs> tabs;

        public SamplePagerAdapter(ArrayList<Tabs> tabs) {
            this.tabs = tabs;
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return tabs.get(position).title;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.product_pager_item,
                    container, false);
            container.addView(view);
            TextView title = (TextView) view.findViewById(R.id.text);
            title.setText(tabs.get(position).details);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
