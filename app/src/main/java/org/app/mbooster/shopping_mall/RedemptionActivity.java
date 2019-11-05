package org.app.mbooster.shopping_mall;

import android.content.res.AssetManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.R;
import org.app.mbooster.Redemption_tabs.PagerAdapter;
import org.app.mbooster.listAdapter_folder.ProductRedemptionAdapter;
import org.app.mbooster.model_folder.EndlessScrollListener;
import org.app.mbooster.model_folder.ProductModel;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class RedemptionActivity extends AppCompatActivity {

    Toolbar toolbar;
    String userId;
    GridView gridView;
    Spinner spinner_product;
    public static String productId;
    ArrayList<String> productName  = new ArrayList<>();

    private List<ProductModel> productItems = new ArrayList<>();
    private ProductRedemptionAdapter adapter;
    private boolean showEvOption = false;
    private boolean showMaOption = false;
    boolean bizUser = false;
    boolean mtiUser = false;
    private int pagenum = 0;
    RelativeLayout noproduct;

    ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemption_v2);
        apply((ViewGroup) findViewById(android.R.id.content));

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Helper.setupImageCache(this);
        Helper.CheckMaintenance(this);

        setToolbar();
        userId = SavePreferences.getUserID(this);


        spinner_product = findViewById(R.id.spinner_product);
        gridView = findViewById(R.id.gridview);

        progressbar = findViewById(R.id.progressbar);


        progressbar.setVisibility(View.VISIBLE);
        getProductSpinner();
        getProductList("0");
        productId = "0";
    }

    public void getProductList(String category_id) {
        productItems = new ArrayList<>();
        adapter = new ProductRedemptionAdapter(getApplicationContext(), productItems, bizUser, mtiUser);
        gridView.setAdapter(adapter);

        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                gridView.setOnScrollListener(null);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                progressbar.setVisibility(View.GONE);
                try {
                    String response = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("products");
                    productItems.addAll(JSonHelper.parseRedemptionList(jsonArray));

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    adapter.setShowEvOption(showEvOption);
                    adapter.setShowMaOption(showMaOption);
                    adapter.setBizUser(bizUser);
                    adapter.setMtiUserUser(mtiUser);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.getProductRedemption(userId,  SavePreferences.getApplanguage(RedemptionActivity.this), category_id);
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void getProductSpinner() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                gridView.setOnScrollListener(null);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if(jsonObject.getString("success").equals("1")){
                        JSONArray arr = new JSONArray(jsonObject.getString("category_array"));
                        productName.add("All");
                        for (int i=0; i<arr.length(); i++){
                            JSONObject objs = arr.getJSONObject(i);
                            productName.add(objs.getString("catname"));
                        }
                        spinner_product.setAdapter(new ArrayAdapter<String>(RedemptionActivity.this,  R.layout.spinner_item_divider, productName));

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        Log.d("On select",spinner_product.getSelectedItem().toString());
                                        spinnerSelect(spinner_product.getSelectedItem().toString());
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                        // your code here
                                    }

                                });

                            }
                        }, 2000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.getSpinnerProduct(userId, SavePreferences.getApplanguage(RedemptionActivity.this));
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void spinnerSelect(String s) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                gridView.setOnScrollListener(null);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if(jsonObject.getString("success").equals("1")){
                        JSONArray arr = new JSONArray(jsonObject.getString("category_array"));
                        if(s.equals("All")){
                            productId = "0";
                            progressbar.setVisibility(View.VISIBLE);
                            getProductList("0");
                        }else{
                            for (int i=0; i<arr.length(); i++){
                                JSONObject objs = arr.getJSONObject(i);
                                if(s.equals(objs.getString("catname"))){
                                    progressbar.setVisibility(View.VISIBLE);
                                    getProductList(objs.getString("catid"));
                                    productId = objs.getString("catid");
                                }
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.getSpinnerProduct(userId, SavePreferences.getApplanguage(RedemptionActivity.this));
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    protected void apply(ViewGroup vg) {
        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/gotham_book.ttf");

        for (int i = 0; i < vg.getChildCount(); ++i) {
            View v = vg.getChildAt(i);
            if (v instanceof TextView)
                ((TextView) v).setTypeface(type);
            else if (v instanceof ViewGroup)
                apply((ViewGroup) v);
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

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
}
