package org.app.mbooster.shopping_mall;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.NetworkStateReceiver;

public class PurchaseSummary extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private EditText name, email, contact, address1, address2, city, state, country, postcode;
    private TextView tvname, tvemail, tvcontact, tvaddr1, tvaddr2, tvcity, tvstate, tvcountry, tvpostcode, title, toolbar_title;
    private String namestr, emailstr, contactstr, address2str, address1str, citystr, statestr, countrystr, postcodestr;
    private Button submit;
    private Toolbar toolbar;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_summary);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
//        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);

        //toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no view has focus:
                View view = PurchaseSummary.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                finish();
            }
        });

        context = activity = this;

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        contact = (EditText) findViewById(R.id.contact);
        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        country = (EditText) findViewById(R.id.country);
        postcode = (EditText) findViewById(R.id.postcode);
        submit = (Button) findViewById(R.id.submit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvname = (TextView) findViewById(R.id.tvname);
        tvemail = (TextView) findViewById(R.id.tvemail);
        tvcontact = (TextView) findViewById(R.id.tvcontact);
        tvaddr1 = (TextView) findViewById(R.id.tvaddr1);
        tvaddr2 = (TextView) findViewById(R.id.tvaddr2);
        tvcity = (TextView) findViewById(R.id.tvcity);
        tvstate = (TextView) findViewById(R.id.tvstate);
        tvcountry = (TextView) findViewById(R.id.tvcountry);
        tvpostcode = (TextView) findViewById(R.id.tvpostcode);
        title = (TextView) findViewById(R.id.title);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

//        if(SavePreferences.getApplanguage(PurchaseSummary.this).equals("CN")){
//            tvname.setText("名字");
//            tvemail.setText("电邮");
//            tvcontact.setText("电话");
//            tvaddr1.setText("地址 1");
//            tvaddr2.setText("地址 2");
//            tvcity.setText("城市");
//            tvstate.setText("州属");
//            tvcountry.setText("国家");
//            tvpostcode.setText("邮政编号");
//            submit.setText("更改");
//            title.setText("运输详情");
//            toolbar_title.setText("运输详情");
//        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                // Check if no view has focus:
                View view = PurchaseSummary.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
//                Log.i("name" , name.getText().toString() +" "+ String.valueOf(name.getText().toString().length()));
//                Log.i("email" , email.getText().toString() +" "+  String.valueOf(email.getText().toString().length()));
//                Log.i("contact" , contact.getText().toString()+" "+  String.valueOf(contact.getText().toString().length()));
//                Log.i("address1" , address1.getText().toString()+" "+  String.valueOf(address1.getText().toString().length()));
//                Log.i("city" , city.getText().toString()+" "+  String.valueOf(city.getText().toString().length()));
//                Log.i("state" , state.getText().toString()+ " "+ String.valueOf(state.getText().toString().length()));
//                Log.i("country" , country.getText().toString()+ " "+ String.valueOf(country.getText().toString().length()));
//                Log.i("postcode" , postcode.getText().toString()+ " "+ String.valueOf(postcode.getText().toString().length()));
                if (name.getText().toString().length() == 0 || email.getText().toString().length() == 0 || contact.getText().toString().length() == 0 ||
                        address1.getText().toString().length() == 0 || city.getText().toString().length() == 0 || state.getText().toString().length() == 0 ||
                        country.getText().toString().length() == 0 || postcode.getText().toString().length() == 0) {

//                    if(SavePreferences.getApplanguage(PurchaseSummary.this).equals("CN")){
//                        Toast.makeText(PurchaseSummary.this, "请填入所有空格", Toast.LENGTH_SHORT).show();
//                    }else{
                    Toast.makeText(PurchaseSummary.this, R.string.please_fill_in_all_blanks, Toast.LENGTH_SHORT).show();

//                    }
                } else {
                    if (Helper.isValidEmail(email.getText().toString())) {
                        Intent k = new Intent();
                        k.putExtra("name", name.getText().toString());
                        k.putExtra("email", email.getText().toString());
                        k.putExtra("contact", contact.getText().toString());
                        k.putExtra("address", address1.getText().toString());
                        k.putExtra("address2", address2.getText().toString());
                        k.putExtra("city", city.getText().toString());
                        k.putExtra("state", state.getText().toString());
                        k.putExtra("country", country.getText().toString());
                        k.putExtra("postcode", postcode.getText().toString());
                        setResult(RESULT_OK, k);
                        Helper.CloseKeyboard(PurchaseSummary.this);
                        finish();
                    } else {
//                        if(SavePreferences.getApplanguage(PurchaseSummary.this).equals("CN")) {
//                            Toast.makeText(PurchaseSummary.this, "电邮无效", Toast.LENGTH_SHORT).show();
//                        }else{
                        Toast.makeText(PurchaseSummary.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
            }
        });
        Intent i = getIntent();
        i.getExtras();

        name.setText(i.getStringExtra("name"));
        email.setText(i.getStringExtra("email"));
        contact.setText(i.getStringExtra("contact"));
        address1.setText(i.getStringExtra("address1"));
        address2.setText(i.getStringExtra("address2"));
        city.setText(i.getStringExtra("city"));
        state.setText(i.getStringExtra("state"));
        country.setText(i.getStringExtra("country"));
        postcode.setText(i.getStringExtra("postcode"));
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
            new AlertDialog.Builder(context,R.style.AlertDialogTheme)
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


    public boolean isOnline() {


        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) {

            return false;
        }

    }


}
