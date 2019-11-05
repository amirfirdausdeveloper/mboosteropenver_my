package org.app.mbooster.shopping_mall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.listAdapter_folder.ProductListingAdapter;
import org.app.mbooster.model_folder.EndlessScrollListener;
import org.app.mbooster.model_folder.ProductModel;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.Product.Product2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class SubcategoryTab extends Fragment {
    private GridView gridView;
    private List<ProductModel> productItems = new ArrayList<>();
    private ProductListingAdapter adapter;
    private String strtext, userId;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private RelativeLayout noproduct;
    private ListView listview;
    private ArrayList<String> namearray = new ArrayList<>();
    private AssetManager assetManager;
    private TextView tvnoproduct;
    private int pagenum = 0;

    private boolean showEvOption = false;
    private boolean showMaOption = false;
    boolean bizUser = false;
    boolean mtiUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strtext = getArguments().getString("Subcategoryid");
        Log.d("strtext",strtext);
        assetManager = getActivity().getApplicationContext().getAssets();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pref = getActivity().getSharedPreferences("MboosterTW", 0); // 0 - for private mode
        editor = pref.edit();

        Log.d("Testing", "This PAge");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_subcategory_tab, container, false);


        userId = SavePreferences.getUserID(getActivity().getApplicationContext());
        gridView = (GridView) myInflatedView.findViewById(R.id.gridview);
        listview = (ListView) myInflatedView.findViewById(R.id.listview);
        noproduct = (RelativeLayout) myInflatedView.findViewById(R.id.noproduct);
        tvnoproduct = (TextView) myInflatedView.findViewById(R.id.tvnoproduct);
//        adapter = new ProductListAdapter(getActivity().getApplicationContext(), productItems);
        adapter = new ProductListingAdapter(getActivity().getApplicationContext(), productItems, bizUser, mtiUser);

        gridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                if (page != 0) {
                    page = page * 100;
                }
                getProductList(page);

                System.out.println("page number = " + page);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        listview.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                if (page != 0) {
                    page = page * 20;
                }
                getProductList(page);

                System.out.println("page number = " + page);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        if (pref.getString("productview", "gridview").equals("gridview")) {
            gridView.setAdapter(adapter);
            listview.setVisibility(View.GONE);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProductListingAdapter tempAdapter = (ProductListingAdapter) parent.getAdapter();
                    ProductModel tempProduct = tempAdapter.getItem(position);
                    if(tempProduct.getQuantity()>0) {
                        LogHelper.debug(tempProduct.getProductname() + " available");
                        Intent i = new Intent(getActivity().getApplicationContext(), Product2.class);
                        i.putExtra("productid", tempProduct.getProductid());
                        startActivity(i);
                    }else{
                        LogHelper.debug(tempProduct.getProductname() + " sold out");
                    }
//                    Intent i = new Intent(getActivity().getApplicationContext(), Product2.class);
////                    Log.i("productid",productItems.get(position).getProductid());
//                    i.putExtra("productid", productItems.get(position).getProductid());
//                    startActivity(i);
                }
            });


        } else {
            listview.setAdapter(adapter);
            gridView.setVisibility(View.GONE);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity().getApplicationContext(), Product2.class);
                    i.putExtra("productid", productItems.get(position).getProductid());
                    startActivity(i);
                }
            });
        }

        if (SavePreferences.getApplanguage(getContext()).equals("CN")) {
            tvnoproduct.setText("即将推出");
        }

        //setGridViewHeightBasedOnChildren(gridView,2);
        getProductList(0);
        return myInflatedView;
    }


    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;
        try {
            View listItem = listAdapter.getView(0, null, gridView);
            listItem.measure(0, 0);
            totalHeight = listItem.getMeasuredHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        editor.putInt(getArguments().getString("Subcategoryid"), totalHeight);
        editor.commit();
        gridView.setLayoutParams(params);

    }

    public void getProductList(final int index) {
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
                    String response = jsonObject.getString("success");
                    if (response.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("array");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject json = jsonArray.getJSONObject(i);
//                            productItems.add(new ProductItem(json.getString("product_id"), json.getString("product_name"), json.getString("amount_point"), json.getString("amount_airtime"), "", json.getString("product_img"), json.getString("amount_cost"), json.getString("voucher"), json.getString("product_label"),json.getString("discount_perc"),json.getString("bundle_voucher")));
//                            // Log.i("name2",json.getString("product_name"));
//                            namearray.add(json.getString("product_name"));
//                        }
                        productItems.addAll(JSonHelper.parseProductList(jsonArray));

                        showEvOption = JSonHelper.getObjBoolean(jsonObject, "showEvOption", false);
                        showMaOption = JSonHelper.getObjBoolean(jsonObject, "showMaOption", false);
                        bizUser = JSonHelper.getObjString(jsonObject, "bizUser").equals("1");
                        mtiUser = JSonHelper.getObjString(jsonObject, "showPV").equals("1");


                        if (productItems.size() == 0) {
                            noproduct.setVisibility(View.VISIBLE);
                        } else {
                            noproduct.setVisibility(View.INVISIBLE);
                        }

                        if(jsonObject.getString("hasNext").equals("true")){
                            if(pagenum == Integer.parseInt(jsonObject.getString("nextPage"))){
                                gridView.setOnScrollListener(null);
                            }else{
                                pagenum = Integer.parseInt(jsonObject.getString("nextPage"));
                                gridView.setOnScrollListener(new EndlessScrollListener() {
                                    @Override
                                    public boolean onLoadMore(int page, int totalItemsCount) {
                                        // Triggered only when new data needs to be appended to the list
                                        // Add whatever code is needed to append new items to your AdapterView
//                                    if (page != 0) {
//                                        page = page * 100;
//                                    }
                                        getProductList(pagenum);

                                        System.out.println("page number = " + pagenum);
                                        return false; // ONLY if more data is actually being loaded; false otherwise.
                                    }
                                });
                            }
                        }else{
                            gridView.setOnScrollListener(null);
                        }


                    } else {
                        if (productItems.size() == 0) {
                            noproduct.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    adapter.setShowEvOption(showEvOption);
                    adapter.setShowMaOption(showMaOption);
                    adapter.setBizUser(bizUser);
                    adapter.setMtiUserUser(mtiUser);
                    adapter.notifyDataSetChanged();
                    // setGridViewHeightBasedOnChildren(gridView,2);
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                JSONObject json = url.getProductList(userId, strtext, index);
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

}
