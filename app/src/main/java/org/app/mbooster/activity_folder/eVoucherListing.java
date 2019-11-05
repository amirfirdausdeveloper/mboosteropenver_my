package org.app.mbooster.activity_folder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.VoucherModel;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class eVoucherListing extends BaseActivity {

    private Toolbar toolbar;
    private TextView toolbar_title, empty;
    private ListView listview;
    private ArrayList<VoucherModel> items = new ArrayList<>();
    private LVAdapter adapter;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    private Context context;
    private int network = 0;

    String customId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evoucher_listing);
        context = activity = this;
        Helper.CheckMaintenance(this);
        ObjectSetup();
        ToobarSetting();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            customId = extras.getString("id", null);
        }
        eVoucherList();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void ObjectSetup() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Helper.setupImageCache(eVoucherListing.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        empty = (TextView) findViewById(R.id.empty);
        empty.setVisibility(View.GONE);
        listview = (ListView) findViewById(R.id.listview);
        adapter = new LVAdapter(eVoucherListing.this, items);
        listview.setAdapter(adapter);
    }

    private void ToobarSetting() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
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

    private void eVoucherList() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(eVoucherListing.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                if (!flowerDialog.isShowing()) {
//                    flowerDialog.show();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                try {
                    JSONArray array = jsonObject.getJSONArray("evoucherlist");
                    LogHelper.debug("[array333][VoucherModel] = "+ array.toString());
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            VoucherModel mVuocher = new VoucherModel();
                            mVuocher.setVoucher_id(JSonHelper.getObjString(json, "voucher_id"));
                            mVuocher.setVoucher_code(JSonHelper.getObjString(json, "voucher_code"));
                            mVuocher.setVoucher_amount(JSonHelper.getObjString(json, "voucher_amount"));
                            mVuocher.setStatusDesc(JSonHelper.getObjString(json, "statusDesc"));
                            mVuocher.setUsedDate(JSonHelper.getObjString(json, "usedDate"));
                            mVuocher.setExpiryDate(JSonHelper.getObjString(json, "expiryDate"));
                            items.add(mVuocher);
                        }
                    } else {
                        empty.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().userEvoucherList(eVoucherListing.this, customId);
            }
        }
        new getinfo().execute();
    }

    private class LVAdapter extends BaseAdapter {
        Context context;
        ArrayList<VoucherModel> items;
        LayoutInflater layoutInflater;

        public LVAdapter(Context context, ArrayList<VoucherModel> items) {
            this.context = context;
            this.items = items;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = layoutInflater.inflate(R.layout.evoucher_listing, null);
            ViewHolder holder = new ViewHolder();
            holder.voucher_code = (TextView) view.findViewById(R.id.vouchercode);
            holder.voucher_amount = (TextView) view.findViewById(R.id.vamount);
            holder.statusDesc = (TextView) view.findViewById(R.id.vstatus);
            holder.usedDate = (TextView) view.findViewById(R.id.vuseddate);
            holder.expiryDate = (TextView) view.findViewById(R.id.vexpirydate);
            VoucherModel item = items.get(i);
            holder.voucher_code.setText(item.getVoucher_code());
            holder.voucher_amount.setText(getString(R.string.voucher_amount)+" :" + item.getVoucher_amount());
            holder.statusDesc.setText(getString(R.string.voucher_status)+ " :" + item.getStatusDesc());
            holder.usedDate.setText(getString(R.string.voucher_used_date) +" :" + item.getUsedDate());
            holder.expiryDate.setText(getString(R.string.voucher_expiry_date)+ " :" + item.getExpiryDate());

            return view;
        }

        class ViewHolder {
            TextView voucher_code, voucher_amount, statusDesc, usedDate, expiryDate;
        }
    }

    @Override
    public void noNetwork() {
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

    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {

            return false;
        }
    }

}
