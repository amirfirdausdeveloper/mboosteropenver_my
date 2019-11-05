package org.app.mbooster.activity_folder;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.NetworkStateReceiver;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Base Model Activity with common feature
 * -Connection Checking
 * -Analytics initiate
 */
public abstract class BaseActivity extends AppCompatActivity implements  NetworkStateReceiver.NetworkStateReceiverListener{


    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;
    private NetworkStateReceiver networkStateReceiver;

    Context context;

    String pageName;

    protected int network = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        context = this;
        // Obtain the shared Tracker instance.
        LogHelper.debug("[BaseActivity] [AnalyticsApplication] ");
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        LogHelper.debug("[BaseActivity] [CalligraphyConfig] ");
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        LogHelper.debug("[BaseActivity] [setupImageCache] ");
        Helper.setupImageCache(BaseActivity.this);
        LogHelper.debug("[BaseActivity] [CheckMaintenance] ");
        Helper.CheckMaintenance(this);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        mTracker.setScreenName(pageName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mFirebaseAnalytics.setCurrentScreen(this, pageName, null /* class override */);
    }

    @Override
    public void networkAvailable() {
        network = 1;

    }

    @Override
    public void networkUnavailable() {
        network = 0;
        noNetwork();
    }

    public void setPageName(String _pageName){
        this.pageName = _pageName;
    }

    public abstract void noNetwork();

    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }

    }

}
