package org.app.mbooster.Redemption_tabs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
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
import android.text.Html;
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
import org.app.mbooster.activity_folder.signInActivity;
import org.app.mbooster.listAdapter_folder.ProductOptionAdapter;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.ProductModel;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.WrapContentViewPager;
import org.app.mbooster.model_folder.YoutubeSliderView;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.DescriptionAnimation2;
import org.app.mbooster.shopping_mall.Kwave.KwaveShoppingBag;
import org.app.mbooster.shopping_mall.Product.Product2;
import org.app.mbooster.shopping_mall.Product.ProductFullDesc;
import org.app.mbooster.shopping_mall.Product.youtube;
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

public class RedeemProductActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private List<String> spinneritemscolor;
    private String productid, userId;
    private ArrayList<String> imgs = new ArrayList<>();
    ProductModel mProduct = new ProductModel();
    private boolean bizUser = false;
    private boolean mtiUser = false;
    private ImageView radioCash, radioEv, radioMa;
    private TextView name, pts, productby, nationwide, norefund, toolbar_title, tvaddtobag, tvqty, originaprice, mppv;
    private TextView promo1;
    private Spinner spinner;
    private EditText quantity;
    private ArrayList<RedeemProductActivity.Tabs> tabs = new ArrayList<>();

    private LinearLayout voucherStatusContainer;
    private LinearLayout mppvContainer;
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

    public RedeemProductActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_product);

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


        productby = (TextView) findViewById(R.id.productby);
        nationwide = (TextView) findViewById(R.id.nationwide);
        norefund = (TextView) findViewById(R.id.norefund);
        viewall = (RelativeLayout) findViewById(R.id.viewall);
        mppvContainer = findViewById(R.id.product_mp_pv_container);


        optionText = findViewById(R.id.options_text);
        optionRL = findViewById(R.id.option_container);

//        desc = (TextView) findViewById(R.id.textmoldesc);
        spinner = (Spinner) findViewById(R.id.spinner);
        tvqty = (TextView) findViewById(R.id.tvqty);
//        keyf = (TextView) findViewById(R.id.description);
//        tvdesc = (TextView) findViewById(R.id.dd);

//        redeemby_spinner = (Spinner) findViewById(R.id.redeemby_spinner);

        flowerDialog = new ACProgressFlower.Builder(RedeemProductActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(getResources().getColor(R.color.colorbutton))
                .fadeColor(Color.GRAY).build();
        //spinner2 = (Spinner) findViewById(R.id.spinner2);



        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels / 10 * 4;
        ViewGroup.LayoutParams layoutParams = mDemoSlider.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mDemoSlider.setLayoutParams(layoutParams);
        stockstatus = (TextView) findViewById(R.id.stockstatus);
        shippingfree = (TextView) findViewById(R.id.shippingfree);
        buynow = (Button) findViewById(R.id.buynow);

        spinners = (LinearLayout) findViewById(R.id.spinners);



        //buynow
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redeemNow();
//                Intent next = new Intent(getApplicationContext(),RedemptionLastActivity.class);
//                startActivity(next);
            }
        });
        AppLinkIntent();

        getProductDetails();
        if (SavePreferences.getUserID(RedeemProductActivity.this).equals("0")) {
            buynow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RedeemProductActivity.this, signInActivity.class));
                }
            });
        }
    }

    private void redeemNow() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(RedeemProductActivity.this)
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

                    Log.d("jsonObject",jsonObject.toString());
                    if (response.equals("1")) {
                        Intent next = new Intent(getApplicationContext(),RedemptionLastActivity.class);
                        next.putExtra("color",optionText.getText().toString());
                        next.putExtra("productId",productid);
                        startActivity(next);

                    } else if (response.equals("2")) {
                        Toast.makeText(RedeemProductActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        Toast.makeText(RedeemProductActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.addtobagRedeem(userId, productid);
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    private void buyNow(){
        buynow(userId, productid, optionText.getText().toString(), String.valueOf(payOption));
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AppLinkIntent();
    }



    private void updateRadioAction(int value){
        payOption = value;
        switch (value){
            case ConstantHolder.RADIO_PAY_CASH:{
                LogHelper.debug("updateRadioAction " + ConstantHolder.RADIO_PAY_CASH);
                break;
            }
            case ConstantHolder.RADIO_PAY_EV:{
                LogHelper.debug("updateRadioAction " + ConstantHolder.RADIO_PAY_EV);


                break;
            }
            case ConstantHolder.RADIO_PAY_MMSPOT:{
                LogHelper.debug("updateRadioAction " + ConstantHolder.RADIO_PAY_MMSPOT);

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


        viewPager.setAdapter(new RedeemProductActivity.SamplePagerAdapter(tabs));
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
                Intent intent = new Intent(RedeemProductActivity.this, ProductFullDesc.class);
                intent.putExtra("product_id", mProduct.getProductid());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
                        mProduct.setAmountcost(JSonHelper.getObjString(jsonObject ,"amount_point"));
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
                        if(!mProduct.getDiscount_perc().equals("0")){
                            StringBuilder dcBuilder = new StringBuilder();
                            if(!mProduct.getDiscount_perc().contains("-")) {
                                dcBuilder.append("-");
                            }
                            dcBuilder.append(mProduct.getDiscount_perc());
                            dcBuilder.append("%");
                        }else{
                        }
                        Locale current_locale = getResources().getConfiguration().locale;

                        JSONArray jsontabs = jsonObject.getJSONArray("tabs");
                        for (int i = 0; i < jsontabs.length(); i++) {
                            JSONObject jtab = jsontabs.getJSONObject(i);
                            tabs.add(new RedeemProductActivity.Tabs(jtab.getString("name"), jtab.getString("details")));
                        }

                        initializeTAB();

                        productby.setText(jsonObject.getString("supplier_name"));




                        shippingfree.setText(getString(R.string.shipping_fee) + " " + jsonObject.getString("product_shipping"));

                        HashMap<String, String> url_maps = new HashMap<String, String>();

                        if (!jsonObject.getString("youtubelink").equals("")) {
                            gotyoutubevideo = true;
                            String youtubefullurl = "https://img.youtube.com/vi/" + jsonObject.getString("youtubelink") + "/maxresdefault.jpg";
                            YoutubeSliderView youtubeSliderView = new YoutubeSliderView(RedeemProductActivity.this);
                            youtubeSliderView
                                    .image(youtubefullurl)
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(RedeemProductActivity.this);
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
                            TextSliderView textSliderView = new TextSliderView(RedeemProductActivity.this);
                            // initialize a SliderLayout
                            textSliderView
                                    //.description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(RedeemProductActivity.this);
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
                        mDemoSlider.addOnPageChangeListener(RedeemProductActivity.this);
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





                        StringBuilder builder = new StringBuilder();
                        if(bizUser || mtiUser) {
                            builder.append(mProduct.getMpValue());
                            builder.append(getString(R.string.postfix_mp).toUpperCase());
                            if (mtiUser) {
                                builder.append("/" + Helper.doubleDecimalToString(mProduct.getPvValue()) + getString(R.string.postfix_pv).toUpperCase());
                            }
                        }else{
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
                            //stockstatus.setTextColor(Color.RED);
                        }

                        stockstatus.setVisibility(View.GONE);

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
                            LayoutInflater factory = LayoutInflater.from(RedeemProductActivity.this);
                            final View indexDialogView = factory.inflate(R.layout.dialog_image_btn, null);
                            final AlertDialog index_dialog = new AlertDialog.Builder(RedeemProductActivity.this).create();
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
                JSONObject json = url.getProductDetails(userId, productid, SavePreferences.getApplanguage(RedeemProductActivity.this));
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
        ProductOptionAdapter adapter = new ProductOptionAdapter(RedeemProductActivity.this, spinneritemscolor);
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



    public void buynow(final String userId, final String productid, final String productcolor, final String redeem_by) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(RedeemProductActivity.this)
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
                        Toast.makeText(RedeemProductActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        if(!mProduct.getEventId().equals("5")) {
                            Intent i = new Intent(RedeemProductActivity.this, Shopping_bag.class);
                            startActivity(i);
                        }else{
                            Intent i = new Intent(RedeemProductActivity.this, KwaveShoppingBag.class);
                            startActivity(i);
                        }
                    } else if (response.equals("2")) {
                        Toast.makeText(RedeemProductActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(Product2.this, Shopping_bag.class);
//                        startActivity(i);
                    } else if (response.equals("3")) {
                        Toast.makeText(RedeemProductActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.addtobag(userId, productid, "1", productcolor, redeem_by, SavePreferences.getApplanguage(RedeemProductActivity.this),"");
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
                Intent intent = new Intent(RedeemProductActivity.this, youtube.class);
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

        ArrayList<RedeemProductActivity.Tabs> tabs;

        public SamplePagerAdapter(ArrayList<RedeemProductActivity.Tabs> tabs) {
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
                                Intent intent = new Intent(RedeemProductActivity.this, youtube.class);
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
