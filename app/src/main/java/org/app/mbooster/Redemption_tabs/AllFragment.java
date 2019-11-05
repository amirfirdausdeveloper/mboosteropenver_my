package org.app.mbooster.Redemption_tabs;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.listAdapter_folder.ProductListingAdapter;
import org.app.mbooster.listAdapter_folder.ProductRedemptionAdapter;
import org.app.mbooster.model_folder.EndlessScrollListener;
import org.app.mbooster.model_folder.ProductModel;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.Product.Product2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends Fragment {


    public AllFragment() {
        // Required empty public constructor
    }

    GridView gridView;
    RelativeLayout noproduct;
    ProgressBar progressbar;
    String userId,strtext;
    private List<ProductModel> productItems = new ArrayList<>();
    private ProductRedemptionAdapter adapter;
    private boolean showEvOption = false;
    private boolean showMaOption = false;
    boolean bizUser = false;
    boolean mtiUser = false;
    private int pagenum = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all, container, false);

        strtext = /*getArguments().getString("Subcategoryid");*/ "12";
        userId = SavePreferences.getUserID(getActivity().getApplicationContext());

        noproduct = v.findViewById(R.id.noproduct);
        progressbar = v.findViewById(R.id.progressbar);
        gridView = v.findViewById(R.id.gridview);
        progressbar = v.findViewById(R.id.progressbar);
        adapter = new ProductRedemptionAdapter(getActivity().getApplicationContext(), productItems, bizUser, mtiUser);

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

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onclick","click");
            }
        });

        progressbar.setVisibility(View.VISIBLE);
        getProductList(0);

        return v;
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
                progressbar.setVisibility(View.GONE);
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
