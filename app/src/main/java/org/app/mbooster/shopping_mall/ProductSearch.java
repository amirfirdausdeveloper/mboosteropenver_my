package org.app.mbooster.shopping_mall;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.listAdapter_folder.ProductListingAdapter;
import org.app.mbooster.model_folder.EndlessScrollListener;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.ProductModel;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.Product.Product2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductSearch extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private SearchView searchView1;
    private GridView gridview;
    private Toolbar toolbar;
    private String userId;
//    private ProductListAdapter adapter;
    private ProductListingAdapter adapter;
    private ArrayList<ProductModel> items = new ArrayList<>();
    private RelativeLayout noproduct;
    private TextView toolbar_title;
    private String q;
    private String lastq;
    private AssetManager assetManager;


    private boolean showEvOption = false;
    private boolean showMaOption = false;
    boolean bizUser;
    boolean mtiUser = false;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    private Context context;
    private int network = 0;

    private ProgressBar progressbar;
    private int pagenum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        context = activity = this;

        assetManager = ProductSearch.this.getApplicationContext().getAssets();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Helper.setupImageCache(ProductSearch.this);

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
                Intent next = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(next);
            }
        });
        Helper.CheckMaintenance(this);


        adapter = new ProductListingAdapter(ProductSearch.this, items, bizUser, mtiUser);
        searchView1 = (SearchView) findViewById(R.id.searchView1);
        int searchPlateId = searchView1.getContext().getResources().getIdentifier("android:id/search_plate", null, null);

        View searchPlate = searchView1.findViewById(searchPlateId);
        ViewGroup.LayoutParams layoutParams = searchPlate.getLayoutParams();
        layoutParams.height -= 20;
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        searchPlate.setLayoutParams(layoutParams);
        gridview = (GridView) findViewById(R.id.gridview);
        noproduct = (RelativeLayout) findViewById(R.id.noproduct);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        userId = SavePreferences.getUserID(this);

//        if (SavePreferences.getApplanguage(ProductSearch.this).equals("CN")) {
//            toolbar_title.setText("搜索");
//            searchView1.setQueryHint("搜索Mbooster");
//        }

        gridview.setAdapter(adapter);
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.equals(q)) {
                    q = query;
                    items.clear();
                    searchQuery(query, 0);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductListingAdapter tempAdapter = (ProductListingAdapter) parent.getAdapter();
                ProductModel tempProduct = tempAdapter.getItem(position);
                if(tempProduct.getQuantity()>0) {
                    Intent i = new Intent(ProductSearch.this, Product2.class);
//                Log.i("productid", items.get(position).getProductid());
                    i.putExtra("productid", tempProduct.getProductid());
                    startActivity(i);
                }else{
                    LogHelper.debug(tempProduct.getProductname() + " sold out");
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent next = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(next);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void searchQuery(final String arg, final int index) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (index == 0) {
                    items.clear();
                }
                progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                progressbar.setVisibility(View.GONE);
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("array");
                        if (jsonArray.length() > 0) {
                            noproduct.setVisibility(View.GONE);
                            items.addAll(JSonHelper.parseProductList(jsonArray));
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject json = jsonArray.getJSONObject(i);
//                                items.add(new ProductModel(json.getString("product_id"), json.getString("product_name"), json.getString("amount_point"), json.getString("amount_airtime"), "", json.getString("product_img"), json.getString("amount_cost"), json.getString("voucher"),json.getString("product_label"),json.getString("discount_perc"),json.getString("bundle_voucher")));
//                                items.addAll(JSonHelper.parseProductList(jsonArray));
//                            }


                            showEvOption = JSonHelper.getObjBoolean(jsonObject, "showEvOption", false);
                            showMaOption = JSonHelper.getObjBoolean(jsonObject, "showMaOption", false);
                            bizUser = JSonHelper.getObjString(jsonObject, "bizUser").equals("1");
                            mtiUser = JSonHelper.getObjString(jsonObject, "showPV").equals("1");
                            adapter = new ProductListingAdapter(ProductSearch.this, items, bizUser, mtiUser);
                            adapter.setShowMaOption(showMaOption);
                            adapter.setShowEvOption(showEvOption);
                            if(index == 0) {
                                gridview.setAdapter(adapter);
                            }

                            if(jsonObject.getString("hasNext").equals("true")){
                                pagenum = Integer.parseInt(jsonObject.getString("nextPage"));

                                gridview.setOnScrollListener(new EndlessScrollListener() {
                                    @Override
                                    public boolean onLoadMore(int page, int totalItemsCount) {

                                        searchQuery(q, pagenum);
                                        System.out.println("page number = " + pagenum);
                                        return true; // ONLY if more data is actually being loaded; false otherwise.
                                    }
                                });
                            }else{
                                gridview.setOnScrollListener(null);
                            }
                        } else {
                            if (items.size() == 0 && index == 0) {
                                noproduct.setVisibility(View.VISIBLE);
                            }
                        }

                    } else {
                        if (items.size() == 0 && index == 0) {
                            noproduct.setVisibility(View.VISIBLE);
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

                return url.searchQuery(userId, arg, index);
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
