package org.app.mbooster.fragment_folder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.RegisterActivity;
import org.app.mbooster.activity_folder.signInActivity;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChangePwd extends AppCompatActivity {
    private Toolbar toolbar;
    EditText currentpassword, newpassword, confirmpassword;
    private RelativeLayout submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        ToobarSetting();

        currentpassword = (EditText) findViewById(R.id.currentpassword);
        newpassword = (EditText) findViewById(R.id.newpassword);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        submit = (RelativeLayout) findViewById(R.id.submit);
        Helper.CheckMaintenance(this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentpasswordstr = currentpassword.getText().toString();
                String newpasswordstr = newpassword.getText().toString();
                String confirmpasswordstr = confirmpassword.getText().toString();

                if (currentpasswordstr.trim().equals("")) {
                    currentpassword.requestFocus();
                    currentpassword.setError(getString(R.string.please_enter_your_current_password));
                } else if (currentpasswordstr.equals(newpasswordstr)) {
                    newpassword.requestFocus();
                    newpassword.setError(getString(R.string.your_new_password_cannot_same_with_old_one));
                } else if (!isValidPassword(newpasswordstr) || newpasswordstr.contains(" ") || newpasswordstr.contains("  ")) {
                    newpassword.requestFocus();
                    newpassword.setError(getString(R.string.passwordatleast6char));
                } else if (confirmpasswordstr.equals("") || !newpasswordstr.equals(confirmpasswordstr)) {
                    confirmpassword.requestFocus();
                    confirmpassword.setError(getString(R.string.password_mismatch));
                } else {
                    change_password(currentpasswordstr, newpasswordstr);
                }
            }
        });
    }

    private void change_password(final String currentpassword, final String newpassword) {

        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(ChangePwd.this)
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
                        finish();
                    }
                    Toast.makeText(ChangePwd.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().changePassword(SavePreferences.getUserID(ChangePwd.this), currentpassword, newpassword, SavePreferences.getApplanguage(ChangePwd.this));
            }
        }
        new getinfo().execute();
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "((?!\\d+$)(?![a-zA-Z]+$)[a-zA-Z\\d]{6,})";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
}
