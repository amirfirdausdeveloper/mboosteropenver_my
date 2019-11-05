package org.app.mbooster.shopping_mall.Notification;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.fragment_folder.Notification;
import org.app.mbooster.listAdapter_folder.NotificationAdapter;
import org.app.mbooster.listAdapter_folder.NotificationListAdapter;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.NotificationItem;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotificationList extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {


    private AssetManager assetManager;
    private TextView toolbar_title;
    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    private Context context;
    private int network = 0;
    private Toolbar toolbar;

    private AlertDialog alertDialog;
    private String notification_id;

    private TextView norecord;
    private SwipeRefreshLayout swipe;

    private RecyclerView recyclerview;
    private NotificationListAdapter adapter;
    private ArrayList<NotificationItem> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        Intent intent = getIntent();
        intent.getExtras();
        String title = intent.getStringExtra("title");
        final String notification_id = intent.getStringExtra("notification_id");

        toolbar_title.setText(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);

        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), MainActivity.class);
                next.putExtra("notification","notification");
                startActivity(next);
            }
        });

//        items.add(new NotificationItem("title", "subtitle", "04 NOV", "web_url"));
//        items.add(new NotificationItem("title", "subtitle", "03 NOV", "web_url"));
//        items.add(new NotificationItem("title", "subtitle", "01 NOv", "web_url"));

        norecord = (TextView) findViewById(R.id.norecord);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new NotificationListAdapter(context,items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NotificationList.this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NotificationList(notification_id);
            }
        });

        adapter.setOnRecyclerViewListener(new NotificationListAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(NotificationList.this,NotificationDetails.class);
                intent.putExtra("id",items.get(position).id);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

        NotificationList(notification_id);
    }

    private void replaceFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.viewpager, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void NotificationList(final String id){
        class getinfo extends AsyncTask<String,String,JSONObject>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                items.clear();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                swipe.setRefreshing(false);
                try{
                    items.clear();
                    JSONArray array = jsonObject.getJSONArray("array");
                    for (int i =0;i<array.length();i++){
                        JSONObject json = array.getJSONObject(i);
                        items.add(new NotificationItem(json.getString("id"), json.getString("title"), json.getString("subtitle"), json.getString("datetime"),json.getString("url"), json.getString("read")));
                    }
                    if(items.isEmpty()){
                        norecord.setVisibility(View.VISIBLE);
                    }else{
                        norecord.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().happening(SavePreferences.getUserID(NotificationList.this),id);
            }
        }
        new getinfo().execute();
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

        String pagename = "(Android) Happening Page";
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

}
