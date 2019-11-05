package org.app.mbooster.fragment_folder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Dialog.DialogFragmentPhoto;
import org.app.mbooster.Dialog.DialogFragmentUniversal;
import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Helper.ChoosePhoto;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.activity_folder.WebViewActivity;
import org.app.mbooster.activity_folder.signInActivity;
import org.app.mbooster.model_folder.ConnectionDetector;
import org.app.mbooster.Helper.JSONParser;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity implements DialogFragmentUniversal.onSubmitListener, DialogFragmentPhoto.onSubmitListener {
    private Toolbar toolbar;

    private TextView username, usercode, useremail, userregion, userphone, userdate, useraddress, profileusername, useric, profileusercompany;
    private TextView tvaddr, tvemail, tvphno, tvmmspotid, tveditprofile, tvlogout;
    private TextView tvedit_profile;
    private TextView tvMti;
    private Button btnMt;

    private CircleImageView profileimage;
    LinearLayout changepasswordly, addressbooksly, changeavatarly, logout, lyeditprofile;
    private String userId;
    ProgressDialog progressDialog;
    ConnectionDetector connectionCheck;
    boolean NetworkOn = false;
    DialogFragmentPhoto fragment1;
    private String profilepicbase64;
    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private TextView version;

    private DialogFragmentUniversal dialog;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    public static final String TAG_MSISDN = "msisdn";
    public static final String TAG_TOKEN = "token";
    public static final String TAG_AUTH = "auth";
    public static final int REQUEST_BIND_ACCOUNT = 1001;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    private ScrollView scrollView;
    private ProgressBar progressbar;

    private TextView login;

    private ChoosePhoto choosePhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile3);
        Log.i("qweqweqwe", "qweqweqwe");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        ToobarSetting();
        pref = getSharedPreferences("MboosterMY", 0); // 0 - for private mode
        editor = pref.edit();

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        version = (TextView) findViewById(R.id.version);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String current_version = pInfo.versionName;
            version.setText("Mbooster.my " + current_version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        profileusername = (TextView) findViewById(R.id.profileusername);
        usercode = (TextView) findViewById(R.id.ucode);
        useremail = (TextView) findViewById(R.id.uemail);
        userphone = (TextView) findViewById(R.id.uphone);
        useraddress = (TextView) findViewById(R.id.uaddress);
        tvedit_profile = (TextView) findViewById(R.id.tvedit_profile);

        tvmmspotid = (TextView) findViewById(R.id.tvmmspotid);
        tvphno = (TextView) findViewById(R.id.tvphno);
        tvemail = (TextView) findViewById(R.id.tvemail);
        tvaddr = (TextView) findViewById(R.id.tvaddr);
        tveditprofile = (TextView) findViewById(R.id.tvedit_profile);
        tvlogout = (TextView) findViewById(R.id.tvlogout);

        profileusercompany = (TextView) findViewById(R.id.profileusercompany);

        profileimage = (CircleImageView) findViewById(R.id.profileimage);
        changeavatarly = (LinearLayout) findViewById(R.id.changeavatarly);
        lyeditprofile = (LinearLayout) findViewById(R.id.lyeditprofile);

        logout = (LinearLayout) findViewById(R.id.logout);

        btnMt = findViewById(R.id.mti_bind_btn);
        tvMti = findViewById(R.id.mti_id_tv);

        btnMt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindAccountWebView();
            }
        });
        tvMti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TEsting
                bindAccountWebView();
            }
        });

        tvedit_profile.setText(Html.fromHtml("<u>" + getString(R.string.edit_profile) + "</u>"));

        lyeditprofile.setClickable(true);
        lyeditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, Edit.class));
            }
        });
        tvedit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("profile edit", "clicked");
                startActivity(new Intent(ProfileActivity.this, Edit.class));
            }
        });


        userId = SavePreferences.getUserID(ProfileActivity.this);

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choosePhoto = new ChoosePhoto(ProfileActivity.this);

            }
        });
        login = (TextView) findViewById(R.id.login);
        login.setText(Html.fromHtml("<font color='blue'><u>Login</u></font>"));
        login.setVisibility(View.GONE);
        Log.i("id ", SavePreferences.getUserID(ProfileActivity.this));
        if (SavePreferences.getUserID(ProfileActivity.this).equals("0")) {
            login.setVisibility(View.GONE);
            logout.setClickable(true);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileActivity.this, signInActivity.class));
                }
            });
            tvlogout.setText(getString(R.string.login));
            lyeditprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileActivity.this, signInActivity.class));
                }
            }); //temp hidden
            tvedit_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileActivity.this, signInActivity.class));
                }
            });
            profileimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileActivity.this, signInActivity.class));
                }
            });

        } else {
            checkConnection();
            tvlogout.setText(getString(R.string.account_logout));
            logout.setClickable(true);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String arg = getString(R.string.areyousurelogout);
                    String arg2 = getString(R.string.logging_out);
                    String arg3 = getString(R.string.confirm);
                    String arg4 = getString(R.string.cancel);
//                if (SavePreferences.getApplanguage(getContext()).equals("CN")) {
//                    arg = "您确定登出吗?";
//                    arg2 = "登出";
//                    arg3 = "登出";
//                    arg4 = "不";
//                }

                    final Dialog dialog = new Dialog(ProfileActivity.this, R.style.AlertDialogTheme);
                    TypefaceUtil.overrideFont(ProfileActivity.this, "SERIF", "fonts/gotham_book.otf");
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
                            Logout();
                            finish();
//                            Intent i = new Intent(ProfileActivityBK.this, signInActivity.class);
                            Intent i = new Intent(ProfileActivity.this, MainActivity.class); //open public
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                        }
                    });


                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }


        Helper.CheckMaintenance(ProfileActivity.this);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void bindAccountWebView(){
        StringBuilder encodeBuilder = new StringBuilder();
        encodeBuilder.append(TAG_MSISDN);
        encodeBuilder.append("=");
        encodeBuilder.append(Helper.encode64(Helper.encode64(profileusername.getText().toString())));
        encodeBuilder.append("&");
        encodeBuilder.append(TAG_TOKEN);
        encodeBuilder.append("=");
        encodeBuilder.append("MTI");
        encodeBuilder.append("&");
        encodeBuilder.append(TAG_AUTH);
        encodeBuilder.append("=");
        encodeBuilder.append(Helper.encode64(Helper.encode64("MTI"+profileusername.getText().toString())));

        LogHelper.debug("[encodeBuilder] toString" + encodeBuilder.toString().trim());

        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.mmspot_bind_url));
        builder.append(encodeBuilder.toString());

        //http://mmspot.com/mti/web/sync?msisdn=601131139001&token=MTI&auth=MTI601131139001
        //http://mmspot.com/mti/web?msisdn=NjAxMTMxMTM5MDAx&token=TVRJ&auth=TVRJNjAxMTMxMTM5MDAx

        Intent i = new Intent(ProfileActivity.this, WebViewActivity.class);
        Bundle extras = new Bundle();
        extras.putString("url", builder.toString());
        extras.putString("title", getString(R.string.profile_bind_acocunt));
        i.putExtras(extras);
        startActivityForResult(i, REQUEST_BIND_ACCOUNT);
    }

    private void ToobarSetting() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
               Intent next = new Intent(getApplicationContext(),MainActivity.class);
               startActivity(next);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent next = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(next);
    }

    public void setData() {
        startActivity(getIntent());
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pref.getString("refreshprofile", "0").equals("1")) {
            setData();
            editor.putString("refreshprofile", "0").commit();
        }
    }

    private void Logout() {
        SavePreferences.setUserEmail(ProfileActivity.this, "");
        SavePreferences.setUserPassword(ProfileActivity.this, "");
        SavePreferences.setUserName(ProfileActivity.this, "");
        SavePreferences.setUserID(ProfileActivity.this, "0");
        SavePreferences.setUserRole(ProfileActivity.this, "");
        SavePreferences.setMMSPOTCONTACT(ProfileActivity.this, "0");
        SavePreferences.setMMSPOTEMAIL(ProfileActivity.this, "");
        SavePreferences.setMMSPOTNAME(ProfileActivity.this, "0");
    }


    public void checkConnection() {
        //Check Connection
        connectionCheck = new ConnectionDetector(ProfileActivity.this);
        NetworkOn = connectionCheck.isConnectingToInternet();

        if (!NetworkOn) {
            Toast.makeText(ProfileActivity.this, "Currently not connection", Toast.LENGTH_SHORT).show();
            Log.i("Internet Connection", "Currently not connection");
        } else {
            profileImage(userId);
            Log.i("Internet Connection", "Currently connecting");
        }
    }

    //********************************************************************************************************************************************************//
    //load ProfileFragment function part
    //********************************************************************************************************************************************************//

    public void profileImage(final String id) {

        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {

            String data = new urlLink().submitgetUserData(ProfileActivity.this);
            String response;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressbar.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jsonObject = new JSONParser().getJSONFromUrl(new urlLink().URLgetUser, data);
                Log.i("json", jsonObject.toString());
                return jsonObject;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                progressbar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Log.i("Running onpost", jsonObject.toString());
                try {
                    response = jsonObject.getString("success");
                    if (response.equals("1")) {

                        String companyname = jsonObject.getString("user_username");
                        String ownername = jsonObject.getString("user_fname");
                        String companycode = jsonObject.getString("user_dealer");
                        String companyemail = jsonObject.getString("user_email");
                        String companyphone = jsonObject.getString("user_contact_tel");
                        String companyregister_date = jsonObject.getString("register_date");
                        String companyregion = jsonObject.getString("user_region");
                        String companyuser_new_ic = jsonObject.getString("user_ic");
                        String company_address = jsonObject.getString("user_address");
//                        String mtiDesc = jsonObject.getString("MtiDescription");
//                        String mtiId = jsonObject.getString("MtiMemberID");
//                        int mtiResultCode = jsonObject.getInt("MtiResultCode");

                        tvMti.setVisibility(View.GONE);
                        btnMt.setVisibility(View.GONE);
//
//                        if(mtiResultCode == 1){
//                            btnMt.setVisibility(View.GONE);
//                            tvMti.setVisibility(View.VISIBLE);
//                            StringBuilder builder = new StringBuilder();
//                            builder.append(getString(R.string.profile_mti_prefix));
//                            builder.append(mtiId);
//                            tvMti.setText(builder.toString());
//                        }else{
//                            tvMti.setVisibility(View.GONE);
//                            btnMt.setVisibility(View.VISIBLE);
//                        }

                        String image = jsonObject.getString("user_avatar");
                        if (image.equals("")) {
                            profileimage.setImageResource(R.drawable.people);
                        } else {
                            ImageLoader.getInstance()
                                    .displayImage(jsonObject.getString("user_avatar"), new ImageViewAware(profileimage, false));
                        }
//                        profileusercompany.setText(companyname);
//                        profileusername.setText(ownername);
                        profileusername.setText(companyname);
                        usercode.setText(companycode);
                        useremail.setText(companyemail);
                        userphone.setText(companyphone);
                        useraddress.setText(company_address);

                        if(jsonObject.has("dataz")){
                            JSONObject dataz = jsonObject.getJSONObject("dataz");
                            SavePreferences.setMMSPOTNAME(getApplicationContext(), dataz.getString("fullname"));
                            SavePreferences.setMMSPOTEMAIL(getApplicationContext(), dataz.getString("email"));
                            SavePreferences.setMMSPOTCONTACT(getApplicationContext(), dataz.getString("alternateno"));
                        }

                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }


        }

        new AsyncTaskRunner().execute();
    }


    //********************************************************************************************************************************************************//
    //upload ProfileFragment function part
    //********************************************************************************************************************************************************//

    public void uploadavatar(final String profilepicbase64, final String id) {

        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {

            String data = new urlLink().submituserprofilepic(profilepicbase64, id, SavePreferences.getApplanguage(ProfileActivity.this));
            String response, message;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jsonObject = new JSONParser().getJSONFromUrl(new urlLink().user_changeavatar, data);
                Log.i("json", jsonObject.toString());
                return jsonObject;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                Log.i("Running on", jsonObject.toString());
                try {
                    response = jsonObject.getString("success");
                    message = jsonObject.getString("message");
                    if (response.equals("1")) {

                        //profileImage(userId);

                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }


        }

        new AsyncTaskRunner().execute();
    }

    /***********************************************************************
     * Function part
     **********************************************************************/
    @Override
    public void setOnSubmitListener(String arg) {
        Log.d("arg", arg.toString());
        if (arg == "Yes" || arg == "登出") {
            SavePreferences.clear(ProfileActivity.this.getApplicationContext());
            startActivity(new Intent(ProfileActivity.this.getApplicationContext(), signInActivity.class));
        } else {
            try {
                fragment1.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChoosePhoto.CHOOSE_PHOTO_INTENT) {
                if (data != null && data.getData() != null) {
                    choosePhoto.handleGalleryResult(data);
                } else {
                    choosePhoto.handleCameraResult(choosePhoto.getCameraUri());
                }
            }else if (requestCode == ChoosePhoto.SELECTED_IMG_CROP) {
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), choosePhoto.getCropImageUrl());
                    Bitmap bMapScaled;
                    bMapScaled = Bitmap.createScaledBitmap(photo, 200, 200, true);
                    profilepicbase64 = BitmapToString(bMapScaled);
                    profileimage.setImageURI(choosePhoto.getCropImageUrl());
                    uploadavatar(profilepicbase64,userId);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Some error occur", Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == REQUEST_BIND_ACCOUNT && resultCode == RESULT_OK){
                refreshUserInfo();
            }
        }
    }

    private void refreshUserInfo(){
        profileImage(userId);
    }

    public String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //100是原本

            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();

            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;

        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }


}
