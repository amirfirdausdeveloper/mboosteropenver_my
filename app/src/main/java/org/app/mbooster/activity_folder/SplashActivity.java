package org.app.mbooster.activity_folder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.stephentuso.welcome.WelcomeHelper;

import org.app.mbooster.BuildConfig;
import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.Holder.ConstantHolder;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.Welcome.ChooseLanguage;
import org.app.mbooster.activity_folder.Welcome.OnBoadingActivity;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.CheckOut;
import org.json.JSONObject;
import org.app.mbooster.model_folder.VolleyCustomRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import mcalls.mmspot.sdk.MMspot;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private NetworkStateReceiver networkStateReceiver;

    static Activity activity;

    private Context context;

    private int network = 0;

    private final int SPLASH_DISPLAY_LENGHT = 1000;

    private ProgressBar progressBar;

    private final String APP_PACKAGE_NAME = "org.app.mbooster";

    private WelcomeHelper welcomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        context = activity = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);


        String mmspot_language = (SavePreferences.getApplanguage(SplashActivity.this).equals("SCN")) ? "CN" : "EN";
//        MMspot.setupMMspot("MBOOSTER", "a522b9dcaf2fce4882409882bccbdd4a", SplashActivity.this, mmspot_language);
        MMspot.setupMMspot("MBOOSTER", SplashActivity.this, mmspot_language);

        if(mmspot_language.equals("EN")) {
            Helper.setAppLocale(SplashActivity.this, "en", "");
            SavePreferences.setApplanguage(SplashActivity.this, "ENG");
        }else{
            Helper.setAppLocale(SplashActivity.this, "zh", "CN");
            SavePreferences.setApplanguage(SplashActivity.this, "SCN");
        }

        if (isOnline() == true) {
            //nextIntent();
            checkMaintenance();
        } else {
            Toast.makeText(SplashActivity.this, "There is no network connection!", Toast.LENGTH_SHORT).show();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void checkMaintenance() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(SplashActivity.this)
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
                    if (JSonHelper.getObjString(jsonObject, JSonHelper.MAINTAINANCE_ANDROID).equals(ConstantHolder.IS_MAINTAINANCE)) {
                        LogHelper.debug("[Splash][checkMaintenance]");
                        if (BuildConfig.DEBUG) {
                            checkAppVersion();
                            return;
                        }
//                        dialogMaintanance(jsonObject.getString("message"));
//                        Helper.CheckMaintenance(SplashActivity.this);

                        Helper.setupImageCache(context);
                        LayoutInflater factory = LayoutInflater.from(context);
                        final View indexDialogView = factory.inflate(R.layout.dialog_image_btn, null);
                        final AlertDialog index_dialog = new AlertDialog.Builder(context).create();
                        index_dialog.setView(indexDialogView);
                        index_dialog.setCancelable(false);
                        index_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        index_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        indexDialogView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //your business logic
                                index_dialog.dismiss();

                                String arg = getString(R.string.are_you_sure);
                                String arg2 = getString(R.string.exit_application);
                                String arg3 = getString(R.string.yes);
                                String arg4 = getString(R.string.cancel);

                                final Dialog dialog = new Dialog(SplashActivity.this, R.style.AlertDialogTheme);
                                TypefaceUtil.overrideFont(SplashActivity.this, "SERIF", "fonts/gotham_book.otf");
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
                                        ((Activity) (context)).finish();
                                    }
                                });

                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        checkMaintenance();
                                    }
                                });
                            }
                        });
//                            ImageLoader.getInstance().displayImage(jsonObject.getString(""), new ImageViewAware());
                        ImageLoader.getInstance().displayImage(jsonObject.getString("maintenance_img"), new ImageViewAware((ImageView) indexDialogView.findViewById(R.id.dialog_image), false), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                ((ProgressBar) indexDialogView.findViewById(R.id.progressbar)).setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                ((ProgressBar) indexDialogView.findViewById(R.id.progressbar)).setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                ((ProgressBar) indexDialogView.findViewById(R.id.progressbar)).setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                ((ProgressBar) indexDialogView.findViewById(R.id.progressbar)).setVisibility(View.INVISIBLE);
                            }
                        });
                        try {
                            index_dialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent intent = new Intent(context, SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            ((Activity) (context)).finish();
                        }
                    } else {
                        checkAppVersion();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().appMaintenance(SavePreferences.getApplanguage(SplashActivity.this));
            }
        }
        new getinfo().execute();
    }

    private void checkAppVersion() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(SplashActivity.this)
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
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String current_version = pInfo.versionName;

                    Log.i("version", current_version);
                    if (jsonObject.getString("check").equals("1")) {
                        //online version
                        String version = jsonObject.getString("version");
                        // String version = "2.0.4";

                        if (!version.equals(current_version)) {

                            dialogUpdate();

                        } else {
                            nextIntent();
                            //loginOperation(email, password);
                        }
                    } else {
                        nextIntent();
                        //loginOperation(email, password);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().appVersion();
            }
        }
        new getinfo().execute();
    }

    public void CheckAccountValid(final String user_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if (jsonObject.getString("status").equals("1")) {
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                finish();
//                Intent i = new Intent(context, signInActivity.class);
                                Intent i = new Intent(context, MainActivity.class); //open public
                                startActivity(i);
                            }

                        }, SPLASH_DISPLAY_LENGHT);
                    } else {
                        SavePreferences.setUserID(SplashActivity.this, "0");
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                finish();
//                Intent i = new Intent(context, signInActivity.class);
                                Intent i = new Intent(context, MainActivity.class); //open public
                                startActivity(i);
                            }
                        }, SPLASH_DISPLAY_LENGHT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().checkAccountValid(user_id);
            }
        }
        new getinfo().execute();
    }

    public void dialogMaintanance(final String msg) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.fragment_dialog_app_ver, null);
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView title = (TextView) view.findViewById(R.id.title);
        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        content.setText(msg);


//        if (language.toLowerCase().contains("CN")) {
//            title.setText("升级");
//            btnConfirm.setText("Ok");
//        } else {
        title.setText("System Maintenance");
        btnConfirm.setText("Ok");
//        }

        final Dialog d = new Dialog(activity);
        TypefaceUtil.overrideFont(activity, "SERIF", "fonts/gotham_book.otf");
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        d.setContentView(view);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCancelable(false);
        d.show();

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                d.dismiss();
            }
        });
    }

    public void dialogUpdate() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.fragment_dialog_app_ver, null);
        TextView content = (TextView) view.findViewById(R.id.content);
        //content.setText(msg);

        String language = Locale.getDefault().getDisplayLanguage();
//        if (language.toLowerCase().contains("CN")) {
//            TextView title = (TextView) view.findViewById(R.id.title);
//            Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
//            title.setText("升级");
//            btnConfirm.setText("升级");
//        }

        final Dialog d = new Dialog(activity);
        TypefaceUtil.overrideFont(activity, "SERIF", "fonts/gotham_book.otf");
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        d.setContentView(R.layout.fragment_dialog_app_ver);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCancelable(false);
        d.show();

        Button btnConfirm = (Button) d.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                intentGooglePlay();
            }
        });
    }

    public void intentGooglePlay() {

        try {

            startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE_NAME)), 2);

        } catch (android.content.ActivityNotFoundException anfe) {

            startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + APP_PACKAGE_NAME)), 2);

        }
    }


    public void nextIntent() {

        if (!SavePreferences.getUserID(SplashActivity.this).equals("0")) {
            CheckAccountValid(SavePreferences.getUserID(context));
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {


                    //FirstRun
                    if (SavePreferences.getFirstTimeApp(SplashActivity.this).equals("0")) {
                        Intent intent = new Intent(SplashActivity.this, ChooseLanguage.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent i = new Intent(context, MainActivity.class);   //open public
                        startActivity(i);
                    }
                    finish();

                }

            }, SPLASH_DISPLAY_LENGHT);
        }


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
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        nextIntent();
//                                        runHTTP();

                                    }
                                }, 2000);


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


    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();

        }

        return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
    }

}
