package org.app.mbooster.activity_folder;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.fragment_folder.Edit;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText fullname, email, password, repeatpassword, birthday, gender;
    private TextView register_message;
    private RelativeLayout submit;
    private Calendar myCalendar = Calendar.getInstance();
    private String birthdatsqlstr;

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        ToobarSetting();

        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password.setTransformationMethod(new PasswordTransformationMethod());
        repeatpassword = (EditText) findViewById(R.id.repeatpassword);
        repeatpassword.setTransformationMethod(new PasswordTransformationMethod());
        birthday = (EditText) findViewById(R.id.birthday);
        gender = (EditText) findViewById(R.id.gender);

        submit = (RelativeLayout) findViewById(R.id.submit);

        final DatePickerDialog dpd = new DatePickerDialog(RegisterActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        dpd.getDatePicker().setMaxDate(new Date().getTime());

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();

            }
        });

        gender.setTag("");
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence colors[] = new CharSequence[]{getString(R.string.register_male)
                                                        , getString(R.string.register_female)};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle(getString(R.string.register_gender));
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        gender.setText(colors[which]);
                        if(which == 0) {
                            gender.setTag("Male");
                        }else{
                            gender.setTag("Female");
                        }

                    }
                });
                builder.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnamestr = fullname.getText().toString();
                String emailstr = email.getText().toString();
                String passwordstr = password.getText().toString();
                String repeatpasswordstr = repeatpassword.getText().toString();
                String birthdaystr = birthday.getText().toString();
                String genderstr = (String) gender.getTag();


                if (fullnamestr.trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter fullname", Toast.LENGTH_SHORT).show();
                    fullname.requestFocus();
                } else if (fullnamestr.trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter fullname", Toast.LENGTH_SHORT).show();
                    fullname.requestFocus();
                } else if (emailstr.trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                } else if (!Helper.isValidEmail(emailstr)) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                } else if (!isValidPassword(passwordstr) || passwordstr.contains(" ") || passwordstr.contains("  ")) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters long and must contain at least 1 number and 1 letter and no space", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                } else if (!repeatpasswordstr.equals(passwordstr)) {
                    Toast.makeText(RegisterActivity.this, "Passwords not match", Toast.LENGTH_SHORT).show();
                    repeatpassword.requestFocus();
                    //Turn off as iOS cannot pass testing due to unused variable
//                } else if (birthdaystr.trim().equals("")) {
//                    Toast.makeText(RegisterActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
//                } else if (genderstr==null || genderstr.trim().equals("")) {
//                    Toast.makeText(RegisterActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                } else {
                    if(birthdatsqlstr == null){
                        birthdatsqlstr = "";
                    }
//                    Toast.makeText(RegisterActivity.this, "all data valid.", Toast.LENGTH_SHORT).show();
                    user_register(fullnamestr, emailstr, passwordstr, birthdatsqlstr, genderstr);
                }


            }
        });
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String namestr = name.getText().toString().trim();
//                String emailstr = email.getText().toString().trim();
//                String passwordstr = password.getText().toString();
//                String retypepasswordstr = retypepassword.getText().toString();
//
//                if (emailstr.equals("")) {
//                    //Toast.makeText(RegisterActivity.this, "Please enter Email", Toast.LENGTH_SHORT).show();
//                    email.requestFocus();
//                    email.setError(getString(R.string.please_enter_email));
//                } else if (!Helper.isValidEmail(emailstr)) {
////                    Toast.makeText(RegisterActivity.this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
//                    email.requestFocus();
//                    email.setError(getString(R.string.please_enter_a_valid_email));
//                } else if (namestr.equals("")) {
//                    // Toast.makeText(RegisterActivity.this, "Please enter Full Name", Toast.LENGTH_SHORT).show();
//                    name.requestFocus();
//                    name.setError(getString(R.string.please_enter_name));
//                } else if (!isValidPassword(passwordstr) || passwordstr.contains(" ") || passwordstr.contains("  ")) {
////                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters long and must contain at least 1 number and 1 letter and no space", Toast.LENGTH_SHORT).show();
//                    password.requestFocus();
//                    password.setError(getString(R.string.passwordatleast6char));
//                } else if (retypepasswordstr.equals("") || !passwordstr.equals(retypepasswordstr)) {
////                    Toast.makeText(RegisterActivity.this, "Passwords does not match", Toast.LENGTH_SHORT).show();
//                    retypepassword.requestFocus();
//                    retypepassword.setError(getString(R.string.password_mismatch));
//                } else {
//                    user_register(namestr, emailstr, passwordstr);
//                }
//            }
//        });

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String myFormatsql = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdfsql = new SimpleDateFormat(myFormatsql, Locale.US);

        birthday.setText(sdf.format(myCalendar.getTime()));

        birthdatsqlstr = sdfsql.format(myCalendar.getTime());
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "((?!\\d+$)(?![a-zA-Z]+$)[a-zA-Z\\d]{6,})";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void user_register(final String namestr, final String emailstr, final String passwordstr, final String birthdatsqlstr, final String genderstr) {

        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(RegisterActivity.this)
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
//                        Intent intent = new Intent(RegisterActivity.this, VerificationRegister.class);
//                        intent.putExtra("name", namestr);
//                        intent.putExtra("email", emailstr);
//                        SavePreferences.setUserID(RegisterActivity.this, jsonObject.getString("user_id"));
//                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().userRegister(namestr, emailstr, passwordstr, birthdatsqlstr, genderstr, SavePreferences.getApplanguage(RegisterActivity.this));
            }
        }
        new getinfo().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, signInActivity.class));
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
