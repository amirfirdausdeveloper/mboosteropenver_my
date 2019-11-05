package org.app.mbooster.shopping_mall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.activity_folder.signInActivity;
import org.app.mbooster.listAdapter_folder.TabsPagerAdapterSubCa;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Subcategory extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private RelativeLayout rllogin;
    private String userId;
    private ArrayList<Subcategory_item> items = new ArrayList<>();
    private String category_id;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerAdapterSubCa adapter;
    private SharedPreferences pref;
    private ImageView imgrequestitem;
    private Menu _menu = null;
    private SharedPreferences.Editor editor;
    private String name;
    private String subcategory_id;

    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        apply((ViewGroup) findViewById(android.R.id.content));

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Helper.setupImageCache(this);
        Helper.CheckMaintenance(this);

        // firebase analytic
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        pref = getSharedPreferences("MboosterTW", 0); // 0 - for private mode
        editor = pref.edit();

        //        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);
        imgrequestitem = (ImageView) findViewById(R.id.imgrequestitem);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rllogin = (RelativeLayout) findViewById(R.id.rllogin);
        rllogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Subcategory.this, signInActivity.class));
            }
        });

        if(!SavePreferences.getUserID(Subcategory.this).equals("0")){
            rllogin.setVisibility(View.GONE);
        }

        Intent i = getIntent();
        i.getExtras();
        name = i.getStringExtra("category_name");
        if (i.hasExtra("subcategory_id")) {
            subcategory_id = i.getStringExtra("subcategory_id");
        } else {
            subcategory_id = "0";
        }
        Log.i("sub", subcategory_id);
        toolbar_title.setText(name);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);


        userId = SavePreferences.getUserID(this);
        category_id = i.getStringExtra("category_id");
        getSubcategory(userId, category_id);
        //Log.i("id", category_id);


        Locale current = getResources().getConfiguration().locale;
        Log.i("phone language", current.toString());
        if (current.toString().toUpperCase().contains("ZH")) {
            imgrequestitem.setImageResource(R.drawable.tcn_request);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();

        String pagename = "(Android) Product List";
        mTracker.setScreenName(pagename);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mFirebaseAnalytics.setCurrentScreen(this, pagename, null /* class override */);
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


    class PageAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;


        public PageAdapter(FragmentManager fm, int numTabs) {
            super(fm);
            this.mNumOfTabs = numTabs;
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putString("Subcategoryid", items.get(position).getCategory_id());
            SubcategoryTab a = new SubcategoryTab();
            a.setArguments(bundle);
//            Log.i("fragmentname", items.get(position).getCategory_name());
            return a;

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return items.get(position).getCategory_name();


        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

        }


    }


    public void getSubcategory(final String userId, final String category_id) {

        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Subcategory.this)
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
                        JSONArray jsonArray = jsonObject.getJSONArray("array");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Subcategory_item item = new Subcategory_item();
                            JSONObject json = jsonArray.getJSONObject(i);
                            item.setCategory_id(json.getString("category_id"));
                            item.setCategory_name(json.getString("category_name").replace("\n", ""));
                            item.setCategory_image(json.getString("category_img"));
                            item.setCategory_rank(json.getString("category_rank"));
                            item.setCategory_parent(json.getString("category_parent"));
                            items.add(item);

                        }
                    }

                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int w = size.x;
                    adapter = new TabsPagerAdapterSubCa(getSupportFragmentManager(), Subcategory.this);
                    for (int i = 0; i < items.size(); i++) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Subcategoryid", items.get(i).getCategory_id());
                        SubcategoryTab a = new SubcategoryTab();
                        a.setArguments(bundle);
                        adapter.addFragments(a, items.get(i).category_name, items.get(i).category_id);
                    }

                    viewPager.setAdapter(adapter);

                    viewPager.setOffscreenPageLimit(4);

                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                    tabLayout.setupWithViewPager(viewPager);
                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        TabLayout.Tab tab = tabLayout.getTabAt(i);
                        tab.setCustomView(adapter.getTabView(i));
                    }

                    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            viewPager.setCurrentItem(tab.getPosition());

                            Log.i("PRODUCT LIST", "Setting screen name: " + name);
                            String pagename = "(Android) Product List: " + items.get(tab.getPosition()).category_name;
                            mTracker.setScreenName(pagename);
                            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                            mFirebaseAnalytics.setCurrentScreen(Subcategory.this, pagename, null /* class override */);
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }


                    });


                    if (!subcategory_id.equals("0")) {
                        for (int i = 0; i < items.size(); i++) {

                            if (items.get(i).getCategory_id().equals(subcategory_id)) {
                                Log.i("sub", String.valueOf(i));
                                Log.i("itemssub", items.get(i).getCategory_id());
                                viewPager.setCurrentItem(i);
                                TabLayout.Tab tab = tabLayout.getTabAt(i);
                                tab.select();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink userFunctions = new urlLink();
                JSONObject json = userFunctions.getsubcategory(userId, category_id, SavePreferences.getApplanguage(Subcategory.this));
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }


    class Subcategory_item {

        String category_id;
        String category_name;
        String category_image;
        String category_rank;
        String category_parent;

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCategory_image() {
            return category_image;
        }

        public void setCategory_image(String category_image) {
            this.category_image = category_image;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getCategory_parent() {
            return category_parent;
        }

        public void setCategory_parent(String category_parent) {
            this.category_parent = category_parent;
        }

        public String getCategory_rank() {
            return category_rank;
        }

        public void setCategory_rank(String category_rank) {
            this.category_rank = category_rank;
        }
    }
}
