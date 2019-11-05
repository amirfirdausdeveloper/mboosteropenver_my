package org.app.mbooster.activity_folder;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.SavePreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VerificationRegister extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText name, email, verifycode;
    private RelativeLayout submit;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_register);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        ToobarSetting();


        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        verifycode = (EditText) findViewById(R.id.verifycode);

        Intent intent = getIntent();
        intent.getExtras();

        name.setText(intent.getStringExtra("name"));
        email.setText(intent.getStringExtra("email"));
        user_id = SavePreferences.getUserID(this);

        submit = (RelativeLayout) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verifycodestr = verifycode.getText().toString().trim();

                if (verifycodestr.equals("")) {

                } else {
//                    VerifyRegister(verifycodestr);
                }

            }
        });
    }

//    private void VerifyRegister(final String verifycode) {
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
//                        finish();
//                    }
//                    Toast.makeText(VerificationRegister.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().VerifyRegister(SavePreferences.getUserID(VerificationRegister.this), SavePreferences.getApplanguage(VerificationRegister.this), verifycode);
//            }
//        }
//        new getinfo().execute();
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VerificationRegister.this, signInActivity.class));
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
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
