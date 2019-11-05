package org.app.mbooster.fragment_folder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileEdit extends AppCompatActivity {

    private TextView tvphno, tvemail, tvaddr1, tvaddr2, tvcity, tvpostcode, tvstate, tvcountry, tvsubmit, toolbar_title;
    private EditText phno, email, addr1, addr2, city, postcode, state, country;
    private RelativeLayout submit;
    private Toolbar toolbar;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);



        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        pref = getSharedPreferences("MboosterMY", 0); // 0 - for private mode
        editor = pref.edit();
        ObjectSetup();
        ToobarSetting();
       // LanguageSetting();
        getUser();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void ObjectSetup() {
        tvphno = (TextView) findViewById(R.id.tvphno);
        tvemail = (TextView) findViewById(R.id.tvemail);
        tvaddr1 = (TextView) findViewById(R.id.tvaddr1);
        tvaddr2 = (TextView) findViewById(R.id.tvaddr2);
        tvcity = (TextView) findViewById(R.id.tvcity);
        tvpostcode = (TextView) findViewById(R.id.tvpostcode);
        tvstate = (TextView) findViewById(R.id.tvstate);
        tvcountry = (TextView) findViewById(R.id.tvcountry);
        tvsubmit = (TextView) findViewById(R.id.tvsubmit);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        phno = (EditText) findViewById(R.id.phno);
        email = (EditText) findViewById(R.id.email);
        addr1 = (EditText) findViewById(R.id.addr1);
        addr2 = (EditText) findViewById(R.id.addr2);
        city = (EditText) findViewById(R.id.city);
        postcode = (EditText) findViewById(R.id.postcode);
        state = (EditText) findViewById(R.id.state);
        country = (EditText) findViewById(R.id.country);
        submit = (RelativeLayout) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Helper.isValidEmail(email.getText().toString())) {
                    updateInfo(phno.getText().toString(), email.getText().toString(), addr1.getText().toString(), addr2.getText().toString(),
                            city.getText().toString(), postcode.getText().toString(), state.getText().toString(), country.getText().toString());
                }else{
                    if(SavePreferences.getApplanguage(ProfileEdit.this).equals("SCN")) {
                        Toast.makeText(ProfileEdit.this, "电邮无效", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ProfileEdit.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


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
                finish();
            }
        });
    }

    private void getUser() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(ProfileEdit.this)
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
                    JSONObject json = jsonObject.getJSONObject("userinfo");
                    phno.setText(json.getString("contact"));
                    email.setText(json.getString("email"));
                    addr1.setText(json.getString("address"));
                    addr2.setText(json.getString("address2"));
                    city.setText(json.getString("city"));
                    postcode.setText(json.getString("postcode"));
                    state.setText(json.getString("state"));
                    country.setText(json.getString("country"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().userInfo(SavePreferences.getUserID(ProfileEdit.this));
                return null;
            }
        }
        new getinfo().execute();
    }

    private void updateInfo(final String contact, final String email, final String addr1, final String addr2,
                            final String city, final String postcode, final String state, final String country) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(ProfileEdit.this)
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
//                        if (SavePreferences.getApplanguage(ProfileEdit.this).equals("SCN")) {
//                            Toast.makeText(ProfileEdit.this, "更新完毕", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(ProfileEdit.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        }

                        editor.putString("refreshprofile","1").commit();
                        finish();
                    }
                    Toast.makeText(ProfileEdit.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().updateUserInfo(SavePreferences.getUserID(ProfileEdit.this), "", contact, email, addr1, addr2, city, postcode, state, country,SavePreferences.getApplanguage(ProfileEdit.this));
                 return null;
            }
        }
        new getinfo().execute();
    }
}
