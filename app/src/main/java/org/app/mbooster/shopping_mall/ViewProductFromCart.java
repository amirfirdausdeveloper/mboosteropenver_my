package org.app.mbooster.shopping_mall;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewProductFromCart extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private List<String> spinneritemscolor;
    private String productid, userId;
    private ArrayList<String> imgs = new ArrayList<>();
    private String product_id;
    private String product_name;
    private String amount_point;
    private String amount_airtime;
    private String supplier_name;
    private String isuccess;
    private ArrayList<String> image;
    private String csuccess;
    //String color;
    private String wished;
    private TextView name, pts, productby, description, tvdesc, norefund, nationwide;
    //Spinner spinner, spinner2;
    private EditText quantity;
    private ImageView wishedimg;
    //SearchView searchView1;
    private SliderLayout mDemoSlider;
    private Toolbar toolbar;
    static ArrayAdapter<String> adapter2;
    private TextView stockstatus, stockstatus2, shippingfree;

    private List<String> spinneritemsqty;
    private WebView webview;
    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    private Context context;
    private int network = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_from_cart);

        context = activity = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
                finish();
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
        tvdesc = (TextView) findViewById(R.id.dd);
        norefund = (TextView) findViewById(R.id.norefund);
        nationwide = (TextView) findViewById(R.id.nationwide);
        productby = (TextView) findViewById(R.id.productby);
        //spinner = (Spinner) findViewById(R.id.spinner);
        description = (TextView) findViewById(R.id.description);
        //spinner2 = (Spinner) findViewById(R.id.spinner2);
        quantity = (EditText) findViewById(R.id.quantity);
        //searchView1 = (SearchView) findViewById(R.id.searchView1);
        wishedimg = (ImageView) findViewById(R.id.wished);
        wishedimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishitem();
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
        //spinners = (LinearLayout) findViewById(R.id.spinners);


        webview = (WebView) findViewById(R.id.descriptionweb);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("http://")) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }
        });
        webview.getSettings().setSupportZoom(true);
        LanguageSetting();


        getProductDetails();
    }

    private void LanguageSetting() {
        if (SavePreferences.getApplanguage(ViewProductFromCart.this).equals("SCN")) {
            norefund.setText("售出的商品不给予退还");
            nationwide.setText("全国运送");
            tvdesc.setText("产品介绍");
        }
    }


    public void getProductDetails() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    String response = jsonObject.getString("success");
                    if (response.equals("1")) {
                        JSONArray imgarray = jsonObject.getJSONArray("image");

                        product_id = jsonObject.getString("product_id");
                        product_name = jsonObject.getString("product_name");
                        amount_point = jsonObject.getString("amount_point");
                        amount_airtime = jsonObject.getString("amount_airtime");
                        supplier_name = jsonObject.getString("supplier_name");
                        // isuccess = jsonObject.getString("isuccess");
                        // csuccess = jsonObject.getString("csuccess");
                        // color = jsonObject.getString("color");
                        wished = jsonObject.getString("wished");
                        if (wished.equals("1")) {
                            wishedimg.setImageResource(R.drawable.wished_btn);
                        }
                        name.setText(Html.fromHtml(product_name));
//                        Log.i("name",product_name);
                        pts.setText(amount_point + getString(R.string.currency));
                        // pts.setText(String.format("%,", amount_point) + " pts");
                        productby.setText("Product By : " + supplier_name);
                        //description.setText(Html.fromHtml(jsonObject.getString("longdesc")));

                        webview.loadData(jsonObject.getString("longdesc"), "text/html", "UTF-8");

                        shippingfree.setText("Shipping Cost\n" + jsonObject.getString("product_shipping"));
                        if (SavePreferences.getApplanguage(ViewProductFromCart.this).equals("SCN")) {
                            shippingfree.setText("运输费\n" + jsonObject.getString("product_shipping"));
                        }

                        HashMap<String, String> url_maps = new HashMap<String, String>();

                        for (int i = 0; i < imgarray.length(); i++) {
                            imgs.add(imgarray.getString(i));
                            JSONObject jj = imgarray.getJSONObject(i);
                            String path = jj.getString("product_img").replace(" ", "%20");
                            path = path.replace(":", "%3A");
                            path = path.replace("-", "%2D");
//                            Log.i("path", new urlLink().nonePath+"products/" + imgarray.getString(i));
                            url_maps.put(String.valueOf(i), new urlLink().nonePath + "products/" + path);
                        }
                        for (String name : url_maps.keySet()) {
                            //CustomImageSliderView cc = new CustomImageSliderView(Product.this);
                            TextSliderView textSliderView = new TextSliderView(ViewProductFromCart.this);
                            // initialize a SliderLayout
                            textSliderView
                                    //.description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
//                    .setOnSliderClickListener(this);

                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", name);

                            mDemoSlider.addSlider(textSliderView);
                        }
                        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mDemoSlider.setCustomAnimation(new DescriptionAnimation2());
                        mDemoSlider.setDuration(4000);

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
                            adapter2 = new ArrayAdapter<String>(ViewProductFromCart.this, R.layout.spinner_item_divider, spinneritemscolor);
                            // spinner.setAdapter(adapter2);
                        } else {
                            stockstatus.setVisibility(View.GONE);
                            //spinners.setVisibility(View.INVISIBLE);
                            stockstatus2.setVisibility(View.VISIBLE);
                            //stockstatus.setTextColor(Color.RED);
                        }
                        if (spinneritemscolor.size() == 1 && spinneritemscolor.get(0).equals("")) {
                            //spinner.setVisibility(View.GONE);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.getProductDetails(userId, productid, SavePreferences.getApplanguage(ViewProductFromCart.this));
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void wishitem() {
        class getinfo extends AsyncTask<String, String, JSONObject> {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    String response = jsonObject.getString("success");
                    if (response.equals("1")) {
                        wishedimg.setImageResource(R.drawable.wished_btn);
                    } else if (response.equals("2")) {
                        wishedimg.setImageResource(R.drawable.wish_btn);
                    }
                    Toast.makeText(ViewProductFromCart.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.wishitem(userId, productid);
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
