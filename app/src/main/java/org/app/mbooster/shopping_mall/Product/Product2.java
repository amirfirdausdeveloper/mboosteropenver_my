package org.app.mbooster.shopping_mall.Product;

import android.app.Activity;
import android.app.Dialog;
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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.Holder.ConstantHolder;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MCPConvertActivity;
import org.app.mbooster.activity_folder.signInActivity;
import org.app.mbooster.listAdapter_folder.ProductOptionAdapter;
import org.app.mbooster.listAdapter_folder.VoucherConversionAdapter;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.ProductModel;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.WrapContentViewPager;
import org.app.mbooster.model_folder.YoutubeSliderView;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.DescriptionAnimation2;
import org.app.mbooster.shopping_mall.Kwave.KwaveShoppingBag;
import org.app.mbooster.shopping_mall.Shopping_bag;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Product2 extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private List<String> spinneritemscolor;
    private String productid, userId;
    private ArrayList<String> imgs = new ArrayList<>();
//    private String product_id;
//    private String product_name;
//    private String amount_point;
//    private String event_id = "0";
//    private String discount_perc ="0";
//    private String evoucher_status;

    ProductModel mProduct = new ProductModel();

    private boolean bizUser = false;
    private boolean mtiUser = false;

    private ImageView radioCash, radioEv, radioMa;
    private TextView name, pts, productby, nationwide, norefund, toolbar_title, tvaddtobag, tvqty, originaprice, mppv;
    private TextView promo1;
    private Spinner spinner;
    private EditText quantity;
    private ArrayList<Tabs> tabs = new ArrayList<>();

    private LinearLayout voucherStatusContainer;
    private LinearLayout mppvContainer;

//    private int voucherValue;
//    private double maValue;
//    private double pvValue;
//    private double mpValue;

    private String remainVoucherValue;
    private String remainMaValue;

    private TabLayout tabLayout;
    private WrapContentViewPager viewPager;

    private SliderLayout mDemoSlider;
    private Toolbar toolbar;
    static ArrayAdapter<String> adapter2;
    private TextView stockstatus, stockstatus2, shippingfree;
    private TextView optionText;
    private TextView tvVoucher, tvMa, tvCash, tvVoucherRemain, tvMARemain;

    private LinearLayout price_rl;
    private LinearLayout cashRadioContainer, evRadioContainer, maRadioContainer;
    private LinearLayout spinners, addtobag;

    private RelativeLayout maContainer, voucherContainer, optionRL;
    private List<String> spinneritemsqty;
    private Button buynow;
    private Integer ProductQTT;
    private String TAG = "Product";

    private ACProgressFlower flowerDialog;
    private int sliderposition = 0;

    ImageView iamgeview_new_item;
    ImageView label_voucher;

    LinearLayout linear_mcalls;
    EditText editText_mcalls_no;
    Boolean ismcalls = false;

    private NetworkStateReceiver networkStateReceiver;
    private Activity activity;
    private Context context;
    private int network = 0;
    private int payOption = -1;
    private boolean hideCashPayment = false;
    private boolean showCashOption = false;
    private boolean showEvOption = false;
    private boolean showMaOption = false;

    private AlertDialog alertDialog;

    private AssetManager assetManager;

    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    private boolean gotyoutubevideo = false;

    private RelativeLayout viewall;

//    private List<String> spinneritemsredeemby;
//    static ArrayAdapter<String> adapter3;
//    private Spinner redeemby_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product2);

        LogHelper.info("[Page][Product2]");
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
        Helper.CheckMaintenance(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        Intent i = getIntent();
        i.getExtras();
        productid = i.getStringExtra("productid");
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        //for slider
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);

        userId = SavePreferences.getUserID(this);
        name = (TextView) findViewById(R.id.name);
        pts = (TextView) findViewById(R.id.pts);

        productby = (TextView) findViewById(R.id.productby);
        nationwide = (TextView) findViewById(R.id.nationwide);
        norefund = (TextView) findViewById(R.id.norefund);
        originaprice = (TextView) findViewById(R.id.originaprice);
        iamgeview_new_item = (ImageView) findViewById(R.id.iamgeview_new_item);
        label_voucher = (ImageView) findViewById(R.id.label_voucher);
        price_rl = findViewById(R.id.price_rl);
        viewall = (RelativeLayout) findViewById(R.id.viewall);
        voucherContainer = findViewById(R.id.voucher_container);
        maContainer = findViewById(R.id.ma_container);
        voucherStatusContainer = findViewById(R.id.voucher_status_container);
        evRadioContainer = findViewById(R.id.product_ev_container);
        maRadioContainer = findViewById(R.id.product_ma_container);
        cashRadioContainer = findViewById(R.id.product_cash_container);
        tvVoucherRemain = findViewById(R.id.product_ramain_ev);
        tvMARemain = findViewById(R.id.product_ramain_ma);
        tvVoucher = findViewById(R.id.product_ev);//voucher_value);
        tvMa = findViewById(R.id.product_ma);//ma_value);
        tvCash = findViewById(R.id.product_cash);
        mppv = findViewById(R.id.product_mp_pv);
        mppvContainer = findViewById(R.id.product_mp_pv_container);
        promo1 = findViewById(R.id.product_discount);

        radioCash = findViewById(R.id.radio_cash);
        radioEv = findViewById(R.id.radio_ev);
        radioMa = findViewById(R.id.radio_ma);

        optionText = findViewById(R.id.options_text);
        optionRL = findViewById(R.id.option_container);

//        desc = (TextView) findViewById(R.id.textmoldesc);
        spinner = (Spinner) findViewById(R.id.spinner);
        tvqty = (TextView) findViewById(R.id.tvqty);
//        keyf = (TextView) findViewById(R.id.description);
//        tvdesc = (TextView) findViewById(R.id.dd);
        tvaddtobag = (TextView) findViewById(R.id.tvaddtobag);

//        redeemby_spinner = (Spinner) findViewById(R.id.redeemby_spinner);

        linear_mcalls = findViewById(R.id.linear_mcalls);
        editText_mcalls_no = findViewById(R.id.editText_mcalls_no);

        flowerDialog = new ACProgressFlower.Builder(Product2.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(getResources().getColor(R.color.colorbutton))
                .fadeColor(Color.GRAY).build();
        //spinner2 = (Spinner) findViewById(R.id.spinner2);
        quantity = (EditText) findViewById(R.id.product_quantity);
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
        ViewGroup.LayoutParams layoutParams = mDemoSlider.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mDemoSlider.setLayoutParams(layoutParams);
        stockstatus = (TextView) findViewById(R.id.stockstatus);
        stockstatus2 = (TextView) findViewById(R.id.stockstatus2);
        shippingfree = (TextView) findViewById(R.id.shippingfree);
        buynow = (Button) findViewById(R.id.buynow);

        spinners = (LinearLayout) findViewById(R.id.spinners);
        addtobag = (LinearLayout) findViewById(R.id.addtobag);

        //addtobag
        addtobag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinneritemscolor.size() != 0) {
                    if (addtobagvalidate()) {
                        LogHelper.debug("[addtobag][payOption] " + payOption);
                        //Update Add To Bag
//                        addtobag(userId, productid, quantity.getText().toString(), spinner.getSelectedItem().toString(), redeemby_spinner.getSelectedItem().toString());
                        addToBag();
                    }
                } else {
                    Toast.makeText(Product2.this, R.string.there_is_no_availalbe_stock, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //buynow
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinneritemscolor.size() != 0) {
                    if (addtobagvalidate()) {
                        LogHelper.debug("[addtobag] [payOption] " + payOption);
                        //Update Buy Now
//                        buynow(userId, productid, quantity.getText().toString(), spinner.getSelectedItem().toString(), redeemby_spinner.getSelectedItem().toString());
//                        buynow(userId, productid, quantity.getText().toString(), optionText.getText().toString(), String.valueOf(payOption));
                        buyNow();
                    }
                } else {
                    Toast.makeText(Product2.this, R.string.there_is_no_availalbe_stock, Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppLinkIntent();

        getProductDetails();
        setupRadioClick();
        if (SavePreferences.getUserID(Product2.this).equals("0")) {
            addtobag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Product2.this, signInActivity.class));
                }
            });
            buynow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Product2.this, signInActivity.class));
                }
            });
        }
    }

    private void buyNow(){
        if(ismcalls){
            if(editText_mcalls_no.getText().toString().equals("")){
                if(SavePreferences.getApplanguage(Product2.this).equals("ENG")){
                    Toast.makeText(getApplicationContext(),"Please enter Topup Mcalls No",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"请输入充值Mcalls号码",Toast.LENGTH_LONG).show();
                }

            }else{
                buynow(userId, productid, quantity.getText().toString(), optionText.getText().toString(), String.valueOf(payOption));
            }
        }else{
            buynow(userId, productid, quantity.getText().toString(), optionText.getText().toString(), String.valueOf(payOption));
        }

    }

    private void addToBag(){
        if(ismcalls){
            if(editText_mcalls_no.getText().toString().equals("")){
                if(SavePreferences.getApplanguage(Product2.this).equals("ENG")){
                    Toast.makeText(getApplicationContext(),"Please enter Topup Mcalls No",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"请输入充值Mcalls号码",Toast.LENGTH_LONG).show();
                }
            }else{
                addtobag(userId, productid, quantity.getText().toString(), optionText.getText().toString(), String.valueOf(payOption));
            }
        }else{
            addtobag(userId, productid, quantity.getText().toString(), optionText.getText().toString(), String.valueOf(payOption));
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AppLinkIntent();
    }

    private void setupRadioClick(){
        evRadioContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRadioAction(ConstantHolder.RADIO_PAY_EV);
            }
        });
        maRadioContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRadioAction(ConstantHolder.RADIO_PAY_MMSPOT);
            }
        });
        cashRadioContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRadioAction(ConstantHolder.RADIO_PAY_CASH);
            }
        });
    }

    private void updateRadioAction(int value){
        payOption = value;
        switch (value){
            case ConstantHolder.RADIO_PAY_CASH:{
                LogHelper.debug("updateRadioAction " + ConstantHolder.RADIO_PAY_CASH);
                radioCash.setImageResource(R.drawable.radio_active);
                radioEv.setImageResource(R.drawable.radio_inactive);
                radioMa.setImageResource(R.drawable.radio_inactive);
                break;
            }
            case ConstantHolder.RADIO_PAY_EV:{
                LogHelper.debug("updateRadioAction " + ConstantHolder.RADIO_PAY_EV);

                radioCash.setImageResource(R.drawable.radio_inactive);
                radioEv.setImageResource(R.drawable.radio_active);
                radioMa.setImageResource(R.drawable.radio_inactive);

                break;
            }
            case ConstantHolder.RADIO_PAY_MMSPOT:{
                LogHelper.debug("updateRadioAction " + ConstantHolder.RADIO_PAY_MMSPOT);

                radioCash.setImageResource(R.drawable.radio_inactive);
                radioEv.setImageResource(R.drawable.radio_inactive);
                radioMa.setImageResource(R.drawable.radio_active);
                break;
            }
        }
    }

    private void AppLinkIntent() {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            Log.d("app link data", appLinkData.toString());
            productid = appLinkData.getQueryParameter("pid");
            Log.d("product_id", productid);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (flowerDialog.isShowing()) {
            flowerDialog.dismiss();
        }
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
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product2.this, ProductFullDesc.class);
                intent.putExtra("product_id", mProduct.getProductid());
                startActivity(intent);
            }
        });
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

                    Log.i("product666",jsonObject.toString());

                    if (response.equals("1")) {
                        JSONArray imgarray = jsonObject.getJSONArray("image");

                        mProduct.setProductid(JSonHelper.getObjString(jsonObject ,"product_id"));
                        mProduct.setProductname(JSonHelper.getObjString(jsonObject ,"product_name"));

                        if(JSonHelper.getObjString(jsonObject ,"is_mcalls_topup").equals("1")){
                            ismcalls = true;
                            linear_mcalls.setVisibility(View.VISIBLE);
                        }else {
                            linear_mcalls.setVisibility(View.GONE);
                        }
                        mProduct.setAmountcost(JSonHelper.getObjString(jsonObject ,"amount_cost"));
                        mProduct.setEventId(JSonHelper.getObjString(jsonObject , "event_id"));
                        mProduct.setVoucher_status(JSonHelper.getObjString(jsonObject, "evoucher_status"));
                        mProduct.setDiscount_perc(JSonHelper.getObjString(jsonObject, "discount_perc"));

                        String evText = JSonHelper.getObjString(jsonObject,"max_voucher_value", "0");
                        String maText = JSonHelper.getObjString(jsonObject,"splitpay_ma_value", "0");
                        String pvText = JSonHelper.getObjString(jsonObject,"pv_value", "0");
                        String mpText = JSonHelper.getObjString(jsonObject,"mp_value", "0");

                        DecimalFormat format = new DecimalFormat();

                        mProduct.setMaxVoucherValue(format.parse(evText).intValue());
                        mProduct.setMaxMAValue(format.parse(maText).doubleValue());
                        mProduct.setPvValue(format.parse(pvText).doubleValue());
                        mProduct.setMpValue(format.parse(mpText).doubleValue());

//                        product_id = jsonObject.getString("product_id");
//                        product_name = jsonObject.getString("product_name");
//                        amount_point = jsonObject.getString("amount_point");
//                        event_id = jsonObject.getString("event_id");
//                        evoucher_status = jsonObject.getString("evoucher_status");
//                        discount_perc = JSonHelper.getObjString(jsonObject, "discount_perc", "0" );
//                        voucherValue = format.parse(evText).intValue();
//                        maValue = format.parse(maText).doubleValue();
//                        pvValue = format.parse(pvText).doubleValue();
//                        mpValue = format.parse(mpText).doubleValue();

                        showCashOption = JSonHelper.getObjBoolean(jsonObject, "showCashOption", false);
                        showEvOption = JSonHelper.getObjBoolean(jsonObject, "showEvOption", false);
                        showMaOption = JSonHelper.getObjBoolean(jsonObject, "showMaOption", false);
                        bizUser = JSonHelper.getObjString(jsonObject, "bizUser").equals("1");
                        mtiUser = JSonHelper.getObjString(jsonObject, "showPV").equals("1");
                        remainVoucherValue = JSonHelper.getObjString(jsonObject, "remaining_value_with_voucher");
                        remainMaValue = JSonHelper.getObjString(jsonObject, "remaining_value_with_ma");
                        hideCashPayment = JSonHelper.getObjString(jsonObject, "hide_cash_payment_option").equals("1");

                        name.setText(Html.fromHtml(mProduct.getProductname()));
                        label_voucher.setVisibility(View.GONE);
                        iamgeview_new_item.setVisibility(View.GONE);
//                        if (jsonObject.getString("voucher").equals("1")) {
//                            label_voucher.setVisibility(View.VISIBLE);
//                        }
                        if(!mProduct.getDiscount_perc().equals("0")){
                            StringBuilder dcBuilder = new StringBuilder();
                            if(!mProduct.getDiscount_perc().contains("-")) {
                                dcBuilder.append("-");
                            }
                            dcBuilder.append(mProduct.getDiscount_perc());
                            dcBuilder.append("%");
                            promo1.setText(dcBuilder.toString());
                            promo1.setVisibility(View.VISIBLE);
                        }else{
                            promo1.setVisibility(View.GONE);
                        }
                        if (jsonObject.getString("product_label").equals("1")) {
                            iamgeview_new_item.setVisibility(View.VISIBLE);
                        }

                        Locale current_locale = getResources().getConfiguration().locale;
                        if (current_locale.toString().toLowerCase().contains("en")) {
                            iamgeview_new_item.setImageResource(R.drawable.label_new);
//                            label_voucher.setImageResource(R.drawable.label_mbooster_voucher);
                        } else if (current_locale.toString().toLowerCase().contains("zh")) {
                            iamgeview_new_item.setImageResource(R.drawable.label_new_cn);
//                            label_voucher.setImageResource(R.drawable.label_mbooster_voucher_cn);
                        }
                        JSONArray jsontabs = jsonObject.getJSONArray("tabs");
                        for (int i = 0; i < jsontabs.length(); i++) {
                            JSONObject jtab = jsontabs.getJSONObject(i);
                            tabs.add(new Tabs(jtab.getString("name"), jtab.getString("details")));
                        }

                        initializeTAB();

                        tvCash.setText(getResources().getString(R.string.currency) + jsonObject.getString("amount_point"));

                        pts.setText(getResources().getString(R.string.currency) + jsonObject.getString("amount_point"));
                        productby.setText(jsonObject.getString("supplier_name"));


                        try {
                            if (jsonObject.getString("amount_cost").equals("0.00") || jsonObject.getString("amount_cost").equals("0")) {
                                originaprice.setText("");
                                originaprice.setVisibility(View.GONE);
                            } else {
                                Log.d("AMOUNT ORIGINAL",mProduct.getAmountcost());
                                originaprice.setText(   getResources().getString(R.string.currency) + mProduct.getAmountcost());//jsonObject.getString("amount_cost"));
                                originaprice.setPaintFlags(originaprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                originaprice.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            //if json not return 0.00 in string
                            originaprice.setText("");
                        }

                        shippingfree.setText(getString(R.string.shipping_fee) + " " + jsonObject.getString("product_shipping"));

                        HashMap<String, String> url_maps = new HashMap<String, String>();

                        if (!jsonObject.getString("youtubelink").equals("")) {
                            gotyoutubevideo = true;
                            String youtubefullurl = "https://img.youtube.com/vi/" + jsonObject.getString("youtubelink") + "/maxresdefault.jpg";
                            YoutubeSliderView youtubeSliderView = new YoutubeSliderView(Product2.this);
                            youtubeSliderView
                                    .image(youtubefullurl)
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(Product2.this);
                            youtubeSliderView.bundle(new Bundle());
                            youtubeSliderView.getBundle()
                                    .putString("extra", jsonObject.getString("youtubelink"));
                            mDemoSlider.addSlider(youtubeSliderView);
                        }

                        for (int i = 0; i < imgarray.length(); i++) {

                            //imgs.add(imgarray.getString(i));

                            JSONObject jj = imgarray.getJSONObject(i);
                            url_maps.put(String.valueOf(i), jj.getString("product_img"));
                            Log.i("product img", jj.getString("product_img"));
                            imgs.add(jj.getString("product_img"));
                        }


                        for (String name : url_maps.keySet()) {
                            //CustomImageSliderView cc = new CustomImageSliderView(Product.this);
                            TextSliderView textSliderView = new TextSliderView(Product2.this);
                            // initialize a SliderLayout
                            textSliderView
                                    //.description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(Product2.this);
                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", "");

                            mDemoSlider.addSlider(textSliderView);
                        }

                        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mDemoSlider.setCustomAnimation(new DescriptionAnimation2());
                        mDemoSlider.stopAutoCycle();
                        mDemoSlider.addOnPageChangeListener(Product2.this);
                        mDemoSlider.animate().cancel();

                        JSONArray jsarray = jsonObject.getJSONArray("color");
                        spinneritemscolor = new ArrayList<>();
                        spinneritemsqty = new ArrayList<>();
//                        spinneritemsredeemby = new ArrayList<>();
                        for (int i = 0; i < jsarray.length(); i++) {
                            if (!jsarray.getJSONObject(i).getString("product_qty").equals("0")) {
                                spinneritemscolor.add(jsarray.getJSONObject(i).getString("product_color"));
                                spinneritemsqty.add(jsarray.getJSONObject(i).getString("product_qty"));
                                if(i == 0){
                                    optionText.setText(spinneritemscolor.get(i));
                                    optionText.setTag(spinneritemscolor.get(i));
                                }
                            }
                        }

                        if(showEvOption) {
                            evRadioContainer.setVisibility(View.VISIBLE);
                            tvVoucher.setText(Helper.doubleDecimalToString(mProduct.getMaxVoucherValue(), ConstantHolder.PATTERN_THOUSAND) + getString(R.string.postfix_ev));
                            tvVoucherRemain.setText(getString(R.string.currency) + remainVoucherValue);
                        }else{
                            evRadioContainer.setVisibility(View.GONE);
                        }

                        if(mProduct.getMaxMAValue() > 0 && showMaOption){
                            maContainer.setVisibility(View.GONE);
                            maRadioContainer.setVisibility(View.VISIBLE);
                            tvMa.setText(Helper.doubleDecimalToString(mProduct.getMaxMAValue(), ConstantHolder.PATTERN_THOUSAND)+getString(R.string.mairtime));
                            tvMARemain.setText(getString(R.string.currency)+remainMaValue);
//                            voucherStatusContainer.addView(addVoucher(R.mipmap.m_a_icon, Helper.doubleDecimalToString(maValue)));
                        }else{
                            maRadioContainer.setVisibility(View.GONE);
                        }

                        StringBuilder builder = new StringBuilder();
                        if(bizUser || mtiUser) {
                            mppvContainer.setVisibility(View.VISIBLE);
                            builder.append(mProduct.getMpValue());
                            builder.append(getString(R.string.postfix_mp).toUpperCase());
                            if (mtiUser) {
                                builder.append("/" + Helper.doubleDecimalToString(mProduct.getPvValue()) + getString(R.string.postfix_pv).toUpperCase());
                            }
                            mppv.setText(builder.toString());
                        }else{
                            mppvContainer.setVisibility(View.GONE);
                        }

                        if (spinneritemscolor.size() != 0) {
                            optionRL.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LogHelper.debug("[optionRL] Clicked");
                                    popUpOptionList();
                                }
                            });
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

                        //Toogle cash payment
                        if(showEvOption){
                            updateRadioAction(ConstantHolder.RADIO_PAY_EV);
                        }else if (showCashOption){
                            updateRadioAction(ConstantHolder.RADIO_PAY_CASH);
                        }

                        if (jsonObject.getString("APP_ORDER_DISABLE_STATUS").equals("1")) {
                            Log.i("AP_ORDER_DISABLE_STATUS", "show dialog");
                            LayoutInflater factory = LayoutInflater.from(Product2.this);
                            final View indexDialogView = factory.inflate(R.layout.dialog_image_btn, null);
                            final AlertDialog index_dialog = new AlertDialog.Builder(Product2.this).create();
                            index_dialog.setView(indexDialogView);
                            index_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            index_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            indexDialogView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //your business logic
                                    index_dialog.dismiss();
                                }
                            });
//                            ImageLoader.getInstance().displayImage(jsonObject.getString(""), new ImageViewAware());
                            ImageLoader.getInstance().displayImage(jsonObject.getString("APP_ORDER_DISABLE_IMG"), new ImageViewAware((ImageView) indexDialogView.findViewById(R.id.dialog_image), false));

                            addtobag.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    index_dialog.show();
                                }
                            });
                            buynow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    index_dialog.show();
                                }
                            });
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.getProductDetails(userId, productid, SavePreferences.getApplanguage(Product2.this));
                //Log.i("product",json.toString());

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

    private void popUpOptionList(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.list_evoucher_conversion);
        dialog.setTitle("Number of exchange");
        ListView list = dialog.findViewById(R.id.List);
        ProductOptionAdapter adapter = new ProductOptionAdapter(Product2.this, spinneritemscolor);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductOptionAdapter tempAdapter = (ProductOptionAdapter) parent.getAdapter();
                String value = tempAdapter.getItem(position);
                optionText.setText(value);
                optionText.setTag(value);
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        list.setAdapter(adapter);
        dialog.show();
    }

    private View addVoucher(int iconId, String value){
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.item_product_details_voucher, null);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int evoucherWidth = (context.getResources().getDisplayMetrics().widthPixels / 3) - context.getResources().getDimensionPixelOffset(R.dimen.event_ev_icon_right_m);
        params.width = evoucherWidth;
        view.setLayoutParams(params);

        ImageView icon = view.findViewById(R.id.voucher_icon);
        TextView name = view.findViewById(R.id.voucher_name);

        icon.setImageResource(iconId);
        name.setText(value);

        return view;

    }

    public void addtobag(final String userId, final String productid, final String productqty, final String productcolor, final String redeem_by) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Product2.this)
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
                    LogHelper.debug("[addtobag] + " + jsonObject.toString());
                    String response = jsonObject.getString("success");
                    if (response.equals("1")) {
                        Toast.makeText(Product2.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(Product2.this, Shopping_bag.class);
//                        startActivity(i);
                    } else if (response.equals("2")) {
                        Toast.makeText(Product2.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        Toast.makeText(Product2.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.addtobag(userId, productid, productqty, productcolor, redeem_by, SavePreferences.getApplanguage(Product2.this),editText_mcalls_no.getText().toString());
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void buynow(final String userId, final String productid, final String productqty, final String productcolor, final String redeem_by) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Product2.this)
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
                    LogHelper.debug("[buynow] + " + jsonObject.toString());
                    String response = jsonObject.getString("success");

                    if (response.equals("1")) {
                        Toast.makeText(Product2.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        if(!mProduct.getEventId().equals("5")) {
                            Intent i = new Intent(Product2.this, Shopping_bag.class);
                            startActivity(i);
                        }else{
                            Intent i = new Intent(Product2.this, KwaveShoppingBag.class);
                            startActivity(i);
                        }
                    } else if (response.equals("2")) {
                        Toast.makeText(Product2.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(Product2.this, Shopping_bag.class);
//                        startActivity(i);
                    } else if (response.equals("3")) {
                        Toast.makeText(Product2.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.addtobag(userId, productid, productqty, productcolor, redeem_by, SavePreferences.getApplanguage(Product2.this),editText_mcalls_no.getText().toString());
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
    public void onSliderClick(BaseSliderView slider) {
        if (!slider.getBundle().get("extra").equals("")) {

            ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isConnected()) {
                // If Wi-Fi connected
                Intent intent = new Intent(Product2.this, youtube.class);
                intent.putExtra("youtubelink", (String) slider.getBundle().get("extra"));
                startActivity(intent);
            }

            if (mobile.isConnected()) {
                // If Internet connected
                promptNetwork((String) slider.getBundle().get("extra"));

            }


        } else {
            if (gotyoutubevideo) {
                new ImageViewer.Builder(context, imgs)
                        .setStartPosition(sliderposition - 1)
                        .show();
            } else {
                new ImageViewer.Builder(context, imgs)
                        .setStartPosition(sliderposition)
                        .show();
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        sliderposition = position;
        Log.i("slider position: ", position + "");
    }

    @Override
    public void onPageScrollStateChanged(int state) {
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

    protected void promptNetwork(final String youtubelink) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You are not using Wi-Fi now. It may produce extra cost from your network provider. Continue?")
                .setTitle("Network Warning")
                .setCancelable(false)
                .setPositiveButton("Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Product2.this, youtube.class);
                                intent.putExtra("youtubelink", youtubelink);
                                startActivity(intent);
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }
}
