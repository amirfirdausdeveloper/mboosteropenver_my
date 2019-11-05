package org.app.mbooster.shopping_mall;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PurchaseHistoryItemDetails extends AppCompatActivity {

    private EditText billdate, recieptno, billmethod;
    private TextView invoiceId, textorderid;
    private EditText shipcounty, email, shipname, shipcontect, shipicno, qty;

    private TextView tracking, courier, shipaddr, productname, unitpts, totalpts, totalpoint;

    private String cart_id, piid;

    private ArrayList<TrackingInfo> trackingInfos = new ArrayList<>();

    private ImageView img;

    private LinearLayout tracking_linearlayout;

    private Toolbar toolbar;

    private AssetManager assetManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history_item_details);

        assetManager = getApplicationContext().getAssets();
        final Typeface tvFont = Typeface.createFromAsset(assetManager, "fonts/gotham_book_bold.ttf");

        cart_id = getIntent().getStringExtra("cart_id");
        piid = getIntent().getStringExtra("piid");

        LogHelper.debug("[Page][PurchaseHistoryItemDetails]");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        ToobarSetting();
        Helper.CheckMaintenance(this);

        invoiceId = (TextView) findViewById(R.id.invoiceid);
        invoiceId.setTypeface(tvFont);
        textorderid = (TextView) findViewById(R.id.textorderid);
        textorderid.setTypeface(tvFont);
        billdate = (EditText) findViewById(R.id.bdate);
        recieptno = (EditText) findViewById(R.id.recieptno);
        billmethod = (EditText) findViewById(R.id.bmethod);
        shipname = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        shipicno = (EditText) findViewById(R.id.icno);
        shipcontect = (EditText) findViewById(R.id.contact);
        shipaddr = (TextView) findViewById(R.id.address1);
        courier = (TextView) findViewById(R.id.courier);
        tracking = (TextView) findViewById(R.id.tracking);

        qty = (EditText) findViewById(R.id.qty);
        Helper.disableEditText(qty);
        productname = (TextView) findViewById(R.id.productname);
        totalpts = (TextView) findViewById(R.id.totalpts);
        unitpts = (TextView) findViewById(R.id.unitpts);
        img = (ImageView) findViewById(R.id.img);

        tracking_linearlayout = (LinearLayout) findViewById(R.id.tracking_linearlayout);

        Log.i("piid", cart_id + " " + piid);
        PurchaseItemInfo(cart_id, piid);
        Log.i("uid", SavePreferences.getUserID(this));

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
                Intent next = new Intent(getApplicationContext(), MainActivity.class);
                next.putExtra("notification","history");
                startActivity(next);
            }
        });
    }

    private void PurchaseItemInfo(final String cart_id, final String piid) {
        class getinfo extends AsyncTask<String, String, JSONObject> {

            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(PurchaseHistoryItemDetails.this)
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
                    invoiceId.setText("#" + jsonObject.getString("invoiceno"));
                    billdate.setText(jsonObject.getString("billingdate"));
                    recieptno.setText(getString(R.string.invoice_id) + ": " + jsonObject.getString("receiptno"));

                    if (jsonObject.getString("billing_method").equals("1")) {
                        billmethod.setText(getString(R.string.payment_method) + ": " + getString(R.string.mmspot));
                    } else if (jsonObject.getString("billing_method").equals("2")) {
                        billmethod.setText(getString(R.string.payment_method) + ": " + getString(R.string.online_banking));
                    } else if (jsonObject.getString("billing_method").equals("3")) {
                        billmethod.setText(getString(R.string.payment_method) + ": " + getString(R.string.credit_card));
                    }
                    email.setText(jsonObject.getString("email"));
                    shipcontect.setText(jsonObject.getString("contact"));
                    shipname.setText(jsonObject.getString("username"));
                    shipicno.setText(jsonObject.getString("icno"));
                    shipaddr.setText(jsonObject.getString("address"));
                    courier.setText(getString(R.string.courier_service) + " " + jsonObject.getString("courier"));
                    tracking.setText(getString(R.string.tracking_code) + " " + jsonObject.getString("tracking"));

                    JSONObject json = jsonObject.getJSONObject("product");
                    qty.setText(json.getString("product_qty"));
                    productname.setText(json.getString("product_name"));
                    totalpts.setText(json.getString("total_amount_point"));
                    unitpts.setText(json.getString("amount_point"));


                    ImageAware imageAware = new ImageViewAware(img);
                    ImageLoader.getInstance().displayImage(json.getString("product_img"), imageAware);

                    JSONArray trackingArray = jsonObject.getJSONArray("trackinginfo");
                    for (int i = 0; i < trackingArray.length(); i++) {
                        JSONObject j = trackingArray.getJSONObject(i);
                        trackingInfos.add(new TrackingInfo(j.getString("trackdatetime"), j.getString("trackinfo")));
                    }

                    LayoutInflater layoutInflater = LayoutInflater.from(PurchaseHistoryItemDetails.this);

                    for (int i = 0; i < trackingInfos.size(); i++) {
                        View v = layoutInflater.inflate(R.layout.tracking_info_layout, null);
                        TextView date = (TextView) v.findViewById(R.id.date);
                        TextView info = (TextView) v.findViewById(R.id.info);

                        date.setText(trackingInfos.get(i).date);
                        info.setText(trackingInfos.get(i).info);

                        tracking_linearlayout.addView(v);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().purchase_history_item_details(SavePreferences.getUserID(PurchaseHistoryItemDetails.this), piid, cart_id, SavePreferences.getApplanguage(PurchaseHistoryItemDetails.this));
            }
        }
        new getinfo().execute();
    }


    class TrackingInfo {
        String date, info;

        public TrackingInfo(String date, String info) {
            this.date = date;
            this.info = info;
        }
    }
}
