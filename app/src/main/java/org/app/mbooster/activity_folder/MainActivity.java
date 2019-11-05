package org.app.mbooster.activity_folder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.app.mbooster.Dialog.DialogFragmentLanguage;
import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.Holder.ConstantHolder;
import org.app.mbooster.fragment_folder.FragmentRewardsInfo;
import org.app.mbooster.fragment_folder.Notification;
import org.app.mbooster.listAdapter_folder.CustomExpandableListAdapter;
import org.app.mbooster.listAdapter_folder.TabsPagerAdapter;
import org.app.mbooster.model_folder.ExpandableClassModel;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;

import org.app.mbooster.R;
import org.app.mbooster.shopping_mall.HomeFragment3;
import org.app.mbooster.shopping_mall.PurchaseHistoryF;
import org.app.mbooster.shopping_mall.RedemptionActivity;
import org.app.mbooster.shopping_mall.RequestAnItem.RequestAnItem2;
import org.app.mbooster.shopping_mall.Shopping_bag;
import org.app.mbooster.shopping_mall.Subcategory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import mcalls.mmspot.sdk.MMspot;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//Commit 1

public class MainActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener, DialogFragmentLanguage.onSubmitListener {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private String s;
    private String userId, lang;
    private RelativeLayout rllogin;
    private ImageView toolbar_cart;
    private RelativeLayout toolbarCartNotification;
    private TextView toolbarCartCount;
    private ImageView imgrequestitem;
    private int lastExpandedPosition = -1;
    private TextView point, tvpoint;
    private int t;
    private NetworkStateReceiver networkStateReceiver;

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private View list;
    private LinearLayout ly;
    private TextView tvlogin;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int aa = 0;
    private int bb = 0;

    private TabsPagerAdapter adapter;

    private ArrayList<category_item> items = new ArrayList<>();

    private int network = 0;

    static private String TAG = "FirebaseServer";

    private Context context;

    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    private ImageView mmspot_app;
    final String appPackageName = "asia.mcalls.mspot"; // getPackageName() from Context or Activity object

    public final int PAGER_PAGE = 1;

    private ImageView mblogo, imageView, menu_header;
    private View content;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        LogHelper.debug("width " + this.getResources().getDisplayMetrics().widthPixels + " height " + this.getResources().getDisplayMetrics().heightPixels );

        ObjectSetup();

        if (Helper.isNetworkAvailable(MainActivity.this)) {
            category_asyn(userId, true);
        }
        toolbarSetting();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void ObjectSetup() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Helper.setupImageCache(MainActivity.this);

        // firebase analyti
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            sendRegistrationToServer(token);
            Log.i("firebase token", token);
            Log.i("DeviceID", SavePreferences.getUUID(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        Locale current = getResources().getConfiguration().locale;
        Log.i("phone language", current.toString());

        userId = SavePreferences.getUserID(this);
        lang = SavePreferences.getApplanguage(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        content = findViewById(R.id.content);
        content.setVisibility(View.GONE);
        tvlogin = (TextView) findViewById(R.id.tvlogin);
        imgrequestitem = (ImageView) findViewById(R.id.imgrequestitem);
        tvpoint = (TextView) findViewById(R.id.tvpoint);
        point = (TextView) findViewById(R.id.point);
        mblogo = (ImageView) findViewById(R.id.mblogo);
        menu_header = (ImageView) findViewById(R.id.menu_header);
        imageView = (ImageView) findViewById(R.id.imageView);
        ly = (LinearLayout) findViewById(R.id.ly);

        fm = getSupportFragmentManager();
        if (current.toString().toUpperCase().contains("ZH")) {
            imgrequestitem.setImageResource(R.drawable.tcn_request);
        }

        mmspot_app = (ImageView) findViewById(R.id.mmspot_app);
        mmspot_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)), 2);
                    tabLayout.getTabAt(0).select();
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)), 2);
                    tabLayout.getTabAt(0).select();
                }
            }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void setOnSubmitListener(String arg, String arg2) {
        switchAppLanguage(arg, arg2);
    }

    //********************************************************************************************************************************************************//
    //Switch Language 2
    //********************************************************************************************************************************************************//
    private void switchAppLanguage(final String language, final String region) {

        Helper.setAppLocale(MainActivity.this, language, region);

        String lang;
        if (language.equals("EN")) {
            lang = "ENG";
        } else if (language.equals("zh") && region.equals("CN")) {
            lang = "SCN";
        } else {
            //tw
            lang = "TCN";
        }
        // changeLanguage(SavePreferences.getUserID(MainActivity.this), lang);
    }

    public void toolbarSetting() {

        setupToolbar();
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setLogo(0);
//        actionBar.setCustomView(customToolbar);
//        Toolbar parent =(Toolbar) customToolbar.getParent();
        toolbar.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0,0);
        toolbar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(list);
            }
        });
        toolbar_cart = findViewById(R.id.toolbar_cart);
        toolbarCartNotification = findViewById(R.id.toolbar_cart_notification);
        toolbarCartCount = findViewById(R.id.toolbar_cart_notification_count);
        rllogin = (RelativeLayout) findViewById(R.id.rllogin);
        rllogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent rai = new Intent(MainActivity.this, signInActivity.class);
                startActivity(rai);
            }
        });

        checkLogin();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


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
        user_mairtime(userId, lang);

        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        String pagename = "(Android) Main Page";
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

    public void onBackPressed() {

//        if (viewPager.getCurrentItem() != 0) {
//            viewPager.setCurrentItem(0, false);
//        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            String arg = getString(R.string.are_you_sure);
//            String arg2 = getString(R.string.exit_application);
//            String arg3 = getString(R.string.yes);
//            String arg4 = getString(R.string.cancel);
////            if (SavePreferences.getApplanguage(MainActivity.this).equals("CN")) {
////                arg = "您确定退出吗?";
////                arg2 = "退出";
////                arg3 = "退出";
////                arg4 = "否";
////            }
//            final Dialog dialog = new Dialog(MainActivity.this, R.style.AlertDialogTheme);
//            TypefaceUtil.overrideFont(MainActivity.this, "SERIF", "fonts/gotham_book.otf");
//            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            dialog.setContentView(R.layout.fragment_dialog_universal);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//
//            TextView content = (TextView) dialog.findViewById(R.id.content);
//            TextView title = (TextView) dialog.findViewById(R.id.title);
//            title.setText(arg2);
//            Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
//            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
//
//            btnConfirm.setText(arg3);
//            btnCancel.setText(arg4);
//
//
//            content.setText(arg);
//            btnConfirm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    finish();
//
//                }
//            });
//
//            btnCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//        }

    }

    //********************************************************************************************************************************************************///
    //Initialize Object
    //*********************************************************************************************************************************************************///

    public void initializeTab() {

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int w = size.x;

        adapter = new TabsPagerAdapter(getSupportFragmentManager(), MainActivity.this, w);
        adapter.addFragments(new HomeFragment3(), getString(R.string.home), "0");

//        adapter.addFragments(new
//
// ConvertActivity(), getString(R.string.tab_convert), "0");
        adapter.addFragments(new PurchaseHistoryF(), getString(R.string.purchase_history), "0");
//        adapter.addFragments(new cartFragment(), getString(R.string.shopping_bag), "0");
        adapter.addFragments(new FragmentRewardsInfo(), getString(R.string.tabs_rewards), "0");
        adapter.addFragments(new Notification(), getString(R.string.happening), "0");
//        adapter.addFragments(new ProfileFragment(), getString(R.string.account), "0");
        adapter.addFragments(new Fragment(), getString(R.string.request_item), "0");

        viewPager.setOffscreenPageLimit(PAGER_PAGE);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(getIntent().hasExtra("notification")){

                    if(getIntent().getStringExtra("notification").equals("notification")){
                        viewPager.setCurrentItem(3);
                    }else if(getIntent().getStringExtra("notification").equals("history")){
                        viewPager.setCurrentItem(1);
                    }

                }

            }
        }, 100);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment f = adapter.getItem(position);
//                if (position == 0) {
//                    f.onResume();
//                } else if (position == 3 && aa == 0) {
//
//                    final String appPackageName = "asia.mcalls.mspot"; // getPackageName() from Context or Activity object
//
//                    if (!Helper.openApp(MainActivity.this, appPackageName)) {
//                        try {
//                            startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)), 2);
//                            tabLayout.getTabAt(0).select();
//                        } catch (android.content.ActivityNotFoundException anfe) {
//                            startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)), 2);
//                            tabLayout.getTabAt(0).select();
//                        }
//                    } else {
//                        tabLayout.getTabAt(0).select();
//                    }
//
//                } else if (position == 2 && bb == 0) {
//                    Intent i = new Intent(MainActivity.this, Shopping_bag.class);
//                    startActivityForResult(i, 1);
//                    bb = 1;
//                }
                if (position == 0) {
                    f.onResume();
//                } else if (position == 2 && bb == 0) {
////                    Intent i = new Intent(MainActivity.this, Shopping_bag.class);
////                    startActivityForResult(i, 1);
//
//                    Intent i = new Intent(MainActivity.this, org.app.mbooster.activity_folder.RewardsInfo.class);
//                    startActivityForResult(i, 1);
//
//                    bb = 1;
                }
                else if (position == 4 && bb == 0) {
                    if (SavePreferences.getUserID(MainActivity.this).equals("0")) {
                        Intent i = new Intent(MainActivity.this, signInActivity.class);
                        startActivityForResult(i, 1);
                        bb = 1;
                    } else {
                        Intent i = new Intent(MainActivity.this, RequestAnItem2.class);
                        startActivityForResult(i, 1);
                        bb = 1;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() != 4) {
                    super.onTabSelected(tab);
                    // if (tab.getPosition() != 3) {
                    int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.colorbutton);
                    ImageView icon = (ImageView) tab.getCustomView().findViewById(R.id.icon);
                    icon.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    TextView text = (TextView) tab.getCustomView().findViewById(R.id.text);
                    text.setTextColor(Color.parseColor("#EBA63F"));
//                    text.setTextColor(getResources().getColor(android.R.color.black));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                //if (tab.getPosition() != 3) {
                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.colorDarkGreyTitle);
                ImageView icon = (ImageView) tab.getCustomView().findViewById(R.id.icon);
                icon.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                TextView text = (TextView) tab.getCustomView().findViewById(R.id.text);
                text.setTextColor(getResources().getColor(R.color.colorDarkGreyTitle));
                //}

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

        //get from res layout
        list = findViewById(R.id.list);

        int width = getResources().getDisplayMetrics().widthPixels / 5 * 4;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) list.getLayoutParams();
        params.width = width;
        list.setLayoutParams(params);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);
        expandableListDetail = ExpandableClassModel.getData(t);

        expandableListTitle = new ArrayList<String>();

        for (int i = 0; i < items.size(); i++) {
            category_item item = items.get(i);
            ArrayList<String> a = new ArrayList<String>();
            LogHelper.debug("[item.category_name] = " + item.category_name);
            //expandableListDetail.put(item.category_name, a);
            //expandableListTitle.add(item.category_name);
        }
        ArrayList<String> a = new ArrayList<String>();

        expandableListTitle.add(getString(R.string.account));
        expandableListDetail.put(getString(R.string.account), a);
//        expandableListTitle.add(getString(R.string.redemption));
//        expandableListDetail.put(getString(R.string.redemption), a);
        expandableListTitle.add(getString(R.string.drawer_order_n_transaction));
        expandableListDetail.put(getString(R.string.drawer_order_n_transaction), a);
        expandableListTitle.add(getString(R.string.request_an_item));
        expandableListDetail.put(getString(R.string.request_an_item), a);
        expandableListTitle.add(getString(R.string.tabs_rewards));
        expandableListDetail.put(getString(R.string.tabs_rewards), a);
        if(SavePreferences.getBizUser(this).equals("1")){
            expandableListTitle.add(getString(R.string.drawer_convert_to_ev));
            expandableListDetail.put(getString(R.string.drawer_convert_to_ev), a);
        }
        expandableListTitle.add(getString(R.string.help_and_contact_us));
        expandableListDetail.put(getString(R.string.help_and_contact_us), a);
        expandableListTitle.add(getString(R.string.change_english));
        expandableListDetail.put(getString(R.string.change_english), a);
        expandableListTitle.add(getString(R.string.drawer_tnc));
        expandableListDetail.put(getString(R.string.drawer_tnc), a);

        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail, items);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                    // parentItems.set(lastExpandedPosition, parentItems.get(lastExpandedPosition).replace("-", "+"));
                }
                lastExpandedPosition = groupPosition;
            }

        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                if (expandableListTitle.get(groupPosition).toString().equals("Change English") || expandableListTitle.get(groupPosition).toString().equals("更換中文")) {
                if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.change_english))){
                    s = expandableListTitle.get(groupPosition).toString();
//                    openFragment(s);
                    openFragment(ConstantHolder.CHANGE_LANGUAGE_TAG);
                } else if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.help_and_contact_us))) {
                    s = expandableListTitle.get(groupPosition).toString();
                    openFragment(s);
                } else if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.account))) {
                    startActivity(new Intent(MainActivity.this, org.app.mbooster.fragment_folder.ProfileActivity.class));
                } else if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.evoucher_listing))) {
                    startActivity(new Intent(MainActivity.this, eVoucherListing.class));
                } else if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.tabs_rewards))) {
//                    startActinoNetworkvity(new Intent(MainActivity.this, org.app.mbooster.activity_folder.RewardPointDisplay.class));
//                    startActivity(new Intent(MainActivity.this, org.app.mbooster.activity_folder.RewardsInfo.class));
                    if(viewPager!=null){
                        viewPager.setCurrentItem(2);
                        mDrawerLayout.closeDrawer(list);
                    }
                } else if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.request_an_item))) {
                    Intent i = new Intent(MainActivity.this, RequestAnItem2.class);
                    startActivityForResult(i, 1);
                } else if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.drawer_convert_to_ev))) {
                    if(SavePreferences.getMPConvert(MainActivity.this).equals("0")) {
                        Helper.showErrorMessage(MainActivity.this, "", getString(R.string.comming_soon));
                    }else {
                        Intent i = new Intent(context, MCPConvertActivity.class);
                        startActivityForResult(i, ConstantHolder.REQUEST_CONVERT_MCP);
                    }
                } else if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.drawer_tnc))) {
                    Intent i = new Intent(context, WebViewActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("url", getString(R.string.tnc_url));
                    extras.putString("title", getString(R.string.drawer_tnc));
                    i.putExtras(extras);
                    startActivity(i);
                } else if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.drawer_order_n_transaction))) {
                    if(viewPager!=null){
                        viewPager.setCurrentItem(1);
                        mDrawerLayout.closeDrawer(list);
                    }
//                    mDrawerLayout.closeDrawer(list);
//                    startActivity(new Intent(MainActivity.this, org.app.mbooster.activity_folder.ActivityOrderHistory.class));
                }else if (expandableListTitle.get(groupPosition).toString().equals(getString(R.string.redemption))) {
                    if (SavePreferences.getUserID(MainActivity.this).equals("0")) {
                        Intent i = new Intent(MainActivity.this, signInActivity.class);
                        startActivityForResult(i, 1);
                    } else {
                        Intent next = new Intent(getApplicationContext(), RedemptionActivity.class);
                        startActivity(next);
                    }

                }

                for (int i = 0; i < items.size(); i++) {
                    s = items.get(i).getCategory_name();
                    if (expandableListTitle.get(groupPosition).equals(s)) {
                        openFragment(s);
                    }
                }
                return false;
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                s = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).toString();
                openFragment(s);
                return false;
            }
        });


        if (getIntent().hasExtra("phistory")) {
            if (getIntent().getStringExtra("phistory").equals("1")) {
                Log.i("MainActivity", "load phistory");
                tabLayout.getTabAt(1).select();
                viewPager.setCurrentItem(1, false);
            }
        }

        //testing
        if (!SavePreferences.getMainActivitySelectTab(MainActivity.this).equals("0")) {
            tabLayout.getTabAt(1).select();
            viewPager.setCurrentItem(1, false);
            SavePreferences.setMainActivitySelectTab(MainActivity.this, "0");
        }

    }

    //********************************************************************************************************************************************************///
    //Open fragment function
    //*********************************************************************************************************************************************************///

    public void openFragment(String s){
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

        switch (s) {
            case ConstantHolder.CHANGE_LANGUAGE_TAG:
                if (Helper.isNetworkAvailable(MainActivity.this)) {
                    if(SavePreferences.getApplanguage(MainActivity.this).equals("SCN")){
                        //Tukar to Engulishu
                        Helper.setAppLocale(MainActivity.this, "en", "");
                        SavePreferences.setApplanguage(MainActivity.this, "ENG");

                    }else if (SavePreferences.getApplanguage(MainActivity.this).equals("ENG")){
                        //Tukar to John Cheenaa
                        Helper.setAppLocale(MainActivity.this, "zh", "CN");
                        SavePreferences.setApplanguage(MainActivity.this, "SCN");
                    }

                    Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(refresh);
                    finish();
                }
                break;
            case "更换语言至英文":
                if (Helper.isNetworkAvailable(MainActivity.this)) {
//                    changeLanguage(SavePreferences.getUserID(getApplicationContext()), "ENG");
                    Helper.setAppLocale(MainActivity.this, "en", "");
                    SavePreferences.setApplanguage(MainActivity.this, "ENG");
                    String mmspot_language = (SavePreferences.getApplanguage(MainActivity.this).equals("ENG")) ? "EN" : "CN";
//                    MMspot.setupMMspot("MBOOSTER", "a522b9dcaf2fce4882409882bccbdd4a", MainActivity.this, mmspot_language);
                    MMspot.setupMMspot("mbooster", MainActivity.this, mmspot_language);
                    Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(refresh);
                    finish();
                }
                break;
            case "Change Language to Chinese":
                if (Helper.isNetworkAvailable(MainActivity.this)) {
//                    changeLanguage(SavePreferences.getUserID(getApplicationContext()), "SCN");
                    Helper.setAppLocale(MainActivity.this, "zh", "CN");
                    SavePreferences.setApplanguage(MainActivity.this, "SCN");
                    String mmspot_language = (SavePreferences.getApplanguage(MainActivity.this).equals("ENG")) ? "EN" : "CN";
//                    MMspot.setupMMspot("MBOOSTER", "a522b9dcaf2fce4882409882bccbdd4a", MainActivity.this, mmspot_language);
                    MMspot.setupMMspot("mbooster", MainActivity.this, mmspot_language);
                    Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(refresh);
                    finish();
                }
                break;


        }

        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getCategory_name();
            if (s.equals(name)) {
                Intent intent = new Intent(MainActivity.this, Subcategory.class);
                intent.putExtra("category_id", items.get(i).getCategory_id());
                intent.putExtra("category_name", items.get(i).getCategory_name());
                startActivity(intent);
            }
        }

        if (s.equals(getString(R.string.help_and_contact_us))) {
            startActivity(new Intent(MainActivity.this, ContactUs.class));
        }

        if (s.equals(getString(R.string.account))) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }

        if (s.equals(getString(R.string.evoucher_listing))) {
            startActivity(new Intent(MainActivity.this, eVoucherListing.class));
        }

        tx.commit();
        mDrawerLayout.closeDrawer(list);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogHelper.debug("[onActivityResult] called");
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                tabLayout.getTabAt(1).select();
                viewPager.setCurrentItem(1, false);
                aa = 0;
                bb = 0;
            } else {
                try {
                    tabLayout.getTabAt(0).select();
                    viewPager.setCurrentItem(0, false);
                    aa = 0;
                    bb = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == 2) {
            tabLayout.getTabAt(0).select();
            viewPager.setCurrentItem(0, false);
            aa = 0;
            bb = 0;
        }

        if (requestCode == ConstantHolder.REQUEST_CONVERT_MCP && resultCode == RESULT_OK){
            //refresh page
            if(viewPager!=null){
                TabsPagerAdapter tempAdapter = (TabsPagerAdapter) viewPager.getAdapter();
                FragmentRewardsInfo f = (FragmentRewardsInfo) tempAdapter.instantiateItem(viewPager, 2);
                f.refreshPage();
                LogHelper.debug("[REQUEST_CONVERT_MCP] Refresh");
            }
        }
    }

    //********************************************************************************************************************************************************//
    //get category name and modify navbar;
    //********************************************************************************************************************************************************//
    public void category_asyn(final String userId, final boolean refresh) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                flowerDialog = new ACProgressFlower.Builder(MainActivity.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
                items.clear();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);


                try {
                    if (flowerDialog.isShowing()) {
                        flowerDialog.dismiss();
                    }
                    String response = jsonObject.getString("success");
                    if (response.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("category");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = (JSONObject) jsonArray.get(i);
                            category_item item = new category_item();
                            item.setCategory_id(json.getString("id"));
                            item.setCategory_name(json.getString("category_name"));

                            if (items.size() == 8) {
                                break;
                            } else {
                                items.add(item);
                            }
                        }
                        // + " M-Airtime"
                        point.setText(JSonHelper.getObjString(jsonObject, "total_point"));
                        point.setText(JSonHelper.getObjString(jsonObject, "total_point"));
                        String cart_count = JSonHelper.getObjString(jsonObject, "shopping_cart");
                        String happening = JSonHelper.getObjString(jsonObject, "happening");
                        String changes = JSonHelper.getObjString(jsonObject, "changes");

                        SavePreferences.setBizUser(MainActivity.this, JSonHelper.getObjString(jsonObject, "bizUser"));
                        SavePreferences.setMPConvert(MainActivity.this, JSonHelper.getObjString(jsonObject, "convert_function_on"));

                        Picasso.with(MainActivity.this).load(jsonObject.getString("mbooster_logo")).into(mblogo);
                        Picasso.with(MainActivity.this).load(jsonObject.getString("mmspot_logo")).into(imageView);

                        mblogo.setVisibility(View.VISIBLE);
                        ly.setVisibility(View.INVISIBLE);
//                        if (SavePreferences.getUserType(MainActivity.this).equals("0") && !SavePreferences.getUserID(MainActivity.this).equals("0")) {
//                            ly.setVisibility(View.VISIBLE);
//                            Picasso.with(MainActivity.this).load(jsonObject.getString("menu_header")).into(menu_header);
//                            mblogo.setVisibility(View.INVISIBLE);
//                        } else {
//                            ly.setVisibility(View.INVISIBLE);
//                            mblogo.setVisibility(View.VISIBLE);
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (refresh) {
                    initializeTab();
                }

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink userFunctions = new urlLink();
                JSONObject json = userFunctions.leftMenu(userId, lang);
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }


    public void user_mairtime(final String userId, final String lang) {
        class getinfo extends AsyncTask<String, String, JSONObject> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
//                    + " M-Airtime"
                    point.setText(jsonObject.getString("total_point"));
                    String cart_count = jsonObject.getString("shopping_cart");
                    String happening = jsonObject.getString("happening");

                    Picasso.with(MainActivity.this).load(jsonObject.getString("mbooster_logo")).into(mblogo);
                    Picasso.with(MainActivity.this).load(jsonObject.getString("mmspot_logo")).into(imageView);

                    mblogo.setVisibility(View.VISIBLE);
                    ly.setVisibility(View.INVISIBLE);
//                    if (SavePreferences.getUserType(MainActivity.this).equals("0") && !SavePreferences.getUserID(MainActivity.this).equals("0")) {
//                        ly.setVisibility(View.VISIBLE);
//                        mblogo.setVisibility(View.INVISIBLE);
//                    } else {
//                        ly.setVisibility(View.INVISIBLE);
//                        mblogo.setVisibility(View.VISIBLE);
//                    }
//                    adapter.UpdateNotification(2, cart_count);
//                    adapter.UpdateNotification(3, happening);
//                    ArrayList<String> newNotification = new ArrayList<>();
//                    newNotification.add("");
//                    newNotification.add("");
//                    newNotification.add(cart_count);
//                    newNotification.add(happening);
//                    newNotification.add("");
//                    adapter.setNotificationcount(newNotification);

                    //update CArt Cunt
                    if(!cart_count.equals("0")){
                        toolbarCartNotification.setVisibility(View.VISIBLE);
                        toolbarCartCount.setText(cart_count);
                    }else{
                        toolbarCartNotification.setVisibility(View.GONE);
                    }

                    //update custom tabview
                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        TabLayout.Tab tab = tabLayout.getTabAt(i);
                        //tab.setCustomView(adapter.getTabView(i));
                        if (tab != null && tab.getCustomView() != null) {
                            TextView b = (TextView) tab.getCustomView().findViewById(R.id.notificationtv);
                            View v = tab.getCustomView().findViewById(R.id.notificationrl);
                            if (i == 2 && !cart_count.equals("0")) {
                                if (b != null && i == 2) {
                                    //Old Cart Count
//                                    b.setText(cart_count + "");
//                                    v.setVisibility(View.VISIBLE);
                                }
                            } else if (i == 2 && cart_count.equals("0")) {
                                v.setVisibility(View.GONE);
                            }

                            if (i == 3 && !happening.equals("0")) {
                                if (b != null && i == 3) {
                                    b.setText(happening + "");
                                    v.setVisibility(View.VISIBLE);
                                }
                            } else if (i == 3 && happening.equals("0")) {
                                v.setVisibility(View.GONE);
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
                JSONObject json = userFunctions.leftMenu(userId, lang);
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    //********************************************************************************************************************************************************//
    //Switch Language
    //********************************************************************************************************************************************************//

//    public void changeLanguage(final String userId, final String lang) {
//        class getinfo extends AsyncTask<String, String, JSONObject> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                super.onPostExecute(jsonObject);
//                try {
//                    if (jsonObject.getString("success").equals("1")) {
//                        unregister();
//
//                        if (lang.equals("ENG")) {
//                            Helper.setAppLocale(MainActivity.this, "en", "");
//                            SavePreferences.setApplanguage(MainActivity.this,"ENG");
//                        } else {
//                            Helper.setAppLocale(MainActivity.this, "zh", "CN");
//                            SavePreferences.setApplanguage(MainActivity.this,"SCN");
//                        }
//                        Intent refresh = new Intent(MainActivity.this, MainActivity.class);
//                        startActivity(refresh);
//                        finish();
//                    } else {
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().changeLanguage(userId, lang);
//            }
//        }
//        new getinfo().execute();
//    }

//    public void changeLanguage(String lang){
//
//
//    }

    //unregister
    private void unregister() {
        this.unregisterReceiver(networkStateReceiver);
    }


    public class category_item {

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

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(list);
            } else if (viewPager.getCurrentItem() != 0) {
                viewPager.setCurrentItem(0, false);
            } else {
                String arg = getString(R.string.are_you_sure);
                String arg2 = getString(R.string.exit_application);
                String arg3 = getString(R.string.yes);
                String arg4 = getString(R.string.cancel);

                final Dialog dialog = new Dialog(MainActivity.this, R.style.AlertDialogTheme);
                TypefaceUtil.overrideFont(MainActivity.this, "SERIF", "fonts/gotham_book.otf");
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.fragment_dialog_universal);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                TextView content = (TextView) dialog.findViewById(R.id.content);
                TextView title = (TextView) dialog.findViewById(R.id.title);
                title.setText(arg2);
                Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                btnConfirm.setText(arg3);
                btnCancel.setText(arg4);


                content.setText(arg);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        }
        return super.onKeyDown(keyCode, event);

    }

    public void sendRegistrationToServer(final String token) {
        // Add custom implementation, as needed.

        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    Log.i("return", jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().setdevicetoken(SavePreferences.getUserID(MainActivity.this), token, SavePreferences.getUUID(MainActivity.this));
            }
        }
        new getinfo().execute();
    }


    public boolean isOnline() {

        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();

        } catch (Exception e) {

            return false;
        }
    }

    //********************************************************************************************************************************************************//
    //Update Info If user login/logout
    //********************************************************************************************************************************************************//
    public void openCart(View v){
        Intent i = new Intent(MainActivity.this, Shopping_bag.class);
        startActivity(i);
    }

    //********************************************************************************************************************************************************//
    //Update Info If user login/logout
    //********************************************************************************************************************************************************//
    private void checkLogin(){
        if (!SavePreferences.getUserID(MainActivity.this).equals("0")) {
            rllogin.setVisibility(View.GONE);
            toolbar_cart.setVisibility(View.VISIBLE);
        }else{
            rllogin.setVisibility(View.VISIBLE);
            toolbar_cart.setVisibility(View.GONE);
        }
    }

}