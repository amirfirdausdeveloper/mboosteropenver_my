package org.app.mbooster.activity_folder;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.ConnectionDetector;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import mcalls.mmspot.sdk.LoginCallBack;
import mcalls.mmspot.sdk.MMspotLoginButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static org.app.mbooster.Helper.Helper.buttonEffect;

public class signInActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    private EditText email, password;

    private Button login;

    static Activity activity;

    Context context;

    private String e;

    private String p;

    private int REQUEST_EXTERNAL_STORAGE_RESULT;

    private Toolbar toolbar;

    private ConnectionDetector connectionCheck;

    boolean NetworkOn = false;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private CheckBox rmbme;

    private String login_language = "";

    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};

    private final String APP_PACKAGE_NAME = "org.app.mbooster";

    private Tracker mTracker;

    private FirebaseAnalytics mFirebaseAnalytics;

    private MMspotLoginButton mmspot_login_button;

    private TextView forgetpassword, register,textView_click;

    private LinearLayout linearLayout2;

    private RelativeLayout alltext;

    private ImageView close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = activity = this;

        setContentView(R.layout.activity_sign_in);

//        firebase analytic
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Helper.CheckMaintenance(this);

        pref = getSharedPreferences("mboosterMY", 0); // 0 - for private mode
        editor = pref.edit();
        editor.putString("language", "EN");
        editor.commit();

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, MULTIPLE_PERMISSIONS);
        }

        Locale current = getResources().getConfiguration().locale;

        if (current.toString().toLowerCase().contains("en")) {
            login_language = "ENG";
        } else if (current.toString().toLowerCase().contains("zh")) {
            login_language = "TCN";
        }

        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout2.setVisibility(View.VISIBLE);
        alltext = (RelativeLayout) findViewById(R.id.alltext);
        alltext.setVisibility(View.INVISIBLE);

        mmspot_login_button = (MMspotLoginButton) findViewById(R.id.mmspot_login_button);
        mmspot_login_button.createInstance(signInActivity.this);
        mmspot_login_button.loginResult(new LoginCallBack() {
            @Override
            public void onSuccess(String id,String msisdn, String status, String token,String secret, String message) {
                System.out.println(msisdn);
                System.out.println(status);
                System.out.println(token);
                System.out.println(message);
                System.out.println(secret);
                SavePreferences.setUserType(getApplicationContext(),"0");
                LoginWithMMspot(msisdn, token);
            }

            @Override
            public void onError(String id, String msisdn, String status, String message) {
                System.out.println(msisdn);
            }

        });

        forgetpassword = (TextView) findViewById(R.id.forgetpassword);
        register = (TextView) findViewById(R.id.register);
        //register.setText(Html.fromHtml("<font color='#f8a23f'><u>Register</u></font>"));
        // ^^^^ Hardcoded the text and colour so language switching won't work. New code below to change color and text
        register.setText(getResources().getString(R.string.register));
        // register.setTextColor(0xFFF06D2F); -> To change colour if needed
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signInActivity.this, ForgetPwd.class);
                startActivity(intent);
            }
        });

        close_btn = (ImageView) findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initialize();

        TextView textView7 = findViewById(R.id.textView7);
        TextView textView16 = findViewById(R.id.textView16);
        textView_click = findViewById(R.id.textView_click);

        textView7.setVisibility(View.GONE);
        textView16.setVisibility(View.GONE);
        textView_click.setVisibility(View.GONE);

        textView_click.setText(Html.fromHtml("<u>" + getString(R.string.ifyoudonthavemmspot2) + "</u>"));
        textView_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = "asia.mcalls.mspot"; // getPackageName() from Context or Activity object
                Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);

                if (intent == null) {
                    try {
                        getPackageManager().getPackageInfo(appPackageName, 0);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (final Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String pagename = "(Android) Login Page";
        mTracker.setScreenName(pagename);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mFirebaseAnalytics.setCurrentScreen(this, pagename, null /* class override */);

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
                    // no permissions granted.
                }
                return;
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void initialize() {

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        rmbme = (CheckBox) findViewById(R.id.rmbme);

        if (pref.contains("rmbme")) {

            email.setText(pref.getString("rmbme", ""));

            // password.setText(pref.getString("password", ""));
            rmbme.setChecked(true);
        }

        e = email.getText().toString().trim();
        p = password.getText().toString();
        login = (Button) findViewById(R.id.login);


        buttonEffect(login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ((Button) findViewById(R.id.login)).performClick();
                    Helper.CloseKeyboard(signInActivity.this);
                    return true;
                }
                return false;
            }
        });


        setSupportActionBar(toolbar);
        setTitle("");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOperation(email.getText().toString(),password.getText().toString());
            }
        });
    }

    public void onClick(View v) {

        e = email.getText().toString().trim();

        p = password.getText().toString();

        switch (v.getId()) {

            case R.id.login:

                if (e.equals("") || p.equals("")) {

                    Toast.makeText(this, "请输入所有空格", Toast.LENGTH_SHORT).show();

                } else {

                    checkConnection(e, p);

                }

                break;

            default:
                break;

        }
    }

    public void checkConnection(String email, String password) {

        //Check Connection
        connectionCheck = new ConnectionDetector(getApplicationContext());
        NetworkOn = connectionCheck.isConnectingToInternet();

        if (!NetworkOn) {

            Toast.makeText(this, "Currently not connection", Toast.LENGTH_SHORT).show();

        } else {

            loginOperation(email, password);
//            checkMaintenance(email, password);
            login.setEnabled(false);
        }

    }

    private void LoginWithMMspot(final String msisdn, final String token) {
        SavePreferences.setMMspotLoginToken(signInActivity.this, token);
        getUserID(msisdn);
    }

    private void getUserID(final String msisdn) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(signInActivity.this)
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
                        SavePreferences.setUserID(getApplicationContext(), jsonObject.getString("user_id"));

                        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        SavePreferences.setUUID(getApplicationContext(), androidId);
                        JSONObject datazzz = jsonObject.getJSONObject("dataz");
                        Log.i("dataz", datazzz.toString());
                        if(jsonObject.has("dataz")){
                            JSONObject dataz = jsonObject.getJSONObject("dataz");
                            SavePreferences.setMMSPOTNAME(getApplicationContext(), dataz.getString("fullname"));
                            SavePreferences.setMMSPOTEMAIL(getApplicationContext(), dataz.getString("email"));
                            SavePreferences.setMMSPOTCONTACT(getApplicationContext(), dataz.getString("alternateno"));
                        }

                        if (jsonObject.getString("newusers").equals("1")) {
                            startActivity(new Intent(signInActivity.this, FirstTimeLogin_v2.class));
                            finish();
                        } else {

                            Intent intent = new Intent(signInActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getUserLoginInfo(msisdn, SavePreferences.getApplanguage(signInActivity.this));
            }
        }
        new getinfo().execute();
    }

    public void loginOperation(final String email, final String password) {

        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {

            String data = new urlLink().submitLogin(email, password, SavePreferences.getApplanguage(signInActivity.this));

            String response;

            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                flowerDialog = new ACProgressFlower.Builder(signInActivity.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();

                if (!flowerDialog.isShowing()) {

                    flowerDialog.show();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {

//                JSONObject jsonObject = new JSONParser().getJSONFromUrl(new urlLink().loginURL, data);
//                Log.i("login", jsonObject.toString());
//                Log.i("data", data.toString());
                urlLink uLink = new urlLink();
                return uLink.loginWithEmailOrMMspot(email, password);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                super.onPostExecute(jsonObject);

                if (flowerDialog.isShowing()) {

                    flowerDialog.dismiss();
                }
                try {

                    LogHelper.debug("[loginOperation] jsonObject = " + jsonObject.toString());
                    if (jsonObject.getString("success").equals("1")) {

                        SavePreferences.setUserEmail(getApplicationContext(), email);
//                        SavePreferences.setUserPassword(getApplicationContext(), password);
//                        SavePreferences.setUserName(getApplicationContext(), jsonObject.getString("user_username"));
//                        SavePreferences.setUserID(getApplicationContext(), jsonObject.getString("user_id"));
//                        SavePreferences.setUserRole(getApplicationContext(), jsonObject.getString("role_name"));

                        SavePreferences.setUserID(getApplicationContext()
                                , JSonHelper.getObjString(jsonObject, "user_id"));

                        String userType = JSonHelper.getObjString(jsonObject, "user_type_name");
                        if(userType.equals("mmspot")) {
                            SavePreferences.setUserType(getApplicationContext(), "0");
                            SavePreferences.setMMSPOTNAME(getApplicationContext()
                                    , JSonHelper.getObjString(jsonObject, "mmspot_fullname"));
                            SavePreferences.setMMSPOTEMAIL(getApplicationContext()
                                    , JSonHelper.getObjString(jsonObject, "mmspot_email"));
                            SavePreferences.setMMSPOTCONTACT(getApplicationContext()
                                    , JSonHelper.getObjString(jsonObject, "mmspot_contact"));
                        }else{
                            SavePreferences.setUserName(context, email);
                            SavePreferences.setUserType(getApplicationContext(), "1");
                        }

                        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        SavePreferences.setUUID(getApplicationContext(), androidId);


                        String msg = JSonHelper.getObjString(jsonObject, "msg");
                        Toast.makeText(signInActivity.this, msg, Toast.LENGTH_LONG);

                        Intent intent = new Intent(signInActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                        //if (jsonObject.getString("newusers").equals("0")) {
//                            getLanguage(jsonObject.getString("user_id"));

                        // } else {
//                            startActivity(new Intent(signInActivity.this, FirstTimeLogin_v2.class));
//                            finish();
                        // }

                        editor.putString("rmbme", email);
                        editor.commit();
//                        if (rmbme.isChecked()) {
//                            // editor.putString("password", password);
//                        } else {
//                            if (pref.contains("rmbme")) {
//                                editor.remove("rmbme");
//                                //  editor.remove("password");
//                                editor.commit();
//                            }
//                        }
                    } else {
//                        if (SavePreferences.getApplanguage(signInActivity.this).equals("SCN")) {
//                            Toast.makeText(signInActivity.this, "账号或者密码错误", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(signInActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
//                        }
                        Toast.makeText(signInActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        login.setEnabled(true);

                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }

        }

        new AsyncTaskRunner().execute();
    }


}
