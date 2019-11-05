package org.app.mbooster.shopping_mall.RequestAnItem;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.Product.Product2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RequestAnItemHistory extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private Toolbar toolbar;
    private TextView toolbar_title, empty;
    private ListView listview;
    private ArrayList<HistoryItem> items = new ArrayList<>();
    private LVAdapter adapter;


    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    private Context context;
    private int network = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_an_item_history);
        context = activity = this;

        Helper.CheckMaintenance(this);
        ObjectSetup();
        ToobarSetting();
//        LanguageSetting();

        HistoryList();

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
        Helper.setupImageCache(RequestAnItemHistory.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        empty = (TextView) findViewById(R.id.empty);
        empty.setVisibility(View.GONE);
        listview = (ListView) findViewById(R.id.listview);
        adapter = new LVAdapter(RequestAnItemHistory.this, items);
        listview.setAdapter(adapter);
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
//
//    private void LanguageSetting() {
//        if (SavePreferences.getApplanguage(RequestAnItemHistory.this).equals("CN")) {
//            toolbar_title.setText("历史记录");
//            empty.setText("哎呀, 没有任何历史记录");
//        }
//    }


    private void HistoryList() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(RequestAnItemHistory.this)
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
                    JSONArray array = jsonObject.getJSONArray("request");
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            //request_id,item_name,item_desc,item_img,item_point,item_status,datetime;
                            items.add(new HistoryItem(json.getString("request_id"), json.getString("item_name"), json.getString("item_desc"), json.getString("item_img"), json.getString("item_point"),
                                    json.getString("item_status"), json.getString("datetime"), json.getString("product_id"), json.getString("refno")));
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
                return new urlLink().requestanitemHistory(SavePreferences.getUserID(RequestAnItemHistory.this));
            }
        }
        new getinfo().execute();
    }


    private class LVAdapter extends BaseAdapter {
        Context context;
        ArrayList<HistoryItem> items;
        LayoutInflater layoutInflater;

        public LVAdapter(Context context, ArrayList<HistoryItem> items) {
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
            view = layoutInflater.inflate(R.layout.requesthistory_item, null);
            ViewHolder holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.desc = (TextView) view.findViewById(R.id.desc);
            holder.status = (TextView) view.findViewById(R.id.status);
            holder.datetime = (TextView) view.findViewById(R.id.datetime);
            holder.tvview = (Button) view.findViewById(R.id.view);

            holder.image = (ImageView) view.findViewById(R.id.image);

            HistoryItem item = items.get(i);
            holder.name.setText(item.getItem_name());
            holder.price.setText(item.getItem_point());
            holder.desc.setText(item.getItem_desc());
            holder.datetime.setText(item.getDatetime());
            holder.datetime.setText(item.getRefno());

//            0 = pending, 1 = approved, 2 = rejected
//            if(SavePreferences.getApplanguage(RequestAnItemHistory.this).equals("CN")){
//                if (item.getItem_status().equals("0")) {
//                    holder.status.setText("待定");
//                    holder.status.setTextColor(getResources().getColor(R.color.colorbutton));
//                } else if (item.getItem_status().equals("1")) {
//                    holder.status.setText("已接纳");
//                    holder.status.setTextColor(Color.GREEN);
//                } else {
//                    holder.status.setText("已拒绝");
//                    holder.status.setTextColor(Color.RED);
//                }
//            }else {

            if (item.getItem_status().equals("0")) {
                holder.status.setText(R.string.pending);
                holder.status.setTextColor(getResources().getColor(R.color.colorbutton));
            } else if (item.getItem_status().equals("1")) {
                holder.status.setText(R.string.approved);
                holder.status.setTextColor(Color.GREEN);
            } else {
                holder.status.setText(R.string.rejected);
                holder.status.setTextColor(Color.RED);
            }
            if(item.getItem_status().equals("1")){
                final String pid = item.getProduct_id();
                holder.tvview.setVisibility(View.VISIBLE);
                holder.tvview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RequestAnItemHistory.this,Product2.class);
                        intent.putExtra("productid", pid);
                        startActivity(intent);
                    }
                });
            }else{
                holder.tvview.setVisibility(View.INVISIBLE);
            }
//            }
            Log.i("path", item.getItem_img());
            if (!item.getItem_img().equals("")) {
                //Picasso.with(context).load(item.getItem_img()).into(holder.image);
                ImageLoader.getInstance().displayImage(item.getItem_img(), new ImageViewAware(holder.image, false));

            }


            return view;
        }

        class ViewHolder {
            TextView name, price, desc, status, datetime;
            ImageView image;
            Button tvview;

        }
    }

    class HistoryItem {
        String request_id, item_name, item_desc, item_img, item_point, item_status, datetime, product_id,refno;

        public HistoryItem(String request_id, String item_name, String item_desc, String item_img, String item_point, String item_status,
                           String datetime,String product_id,String refno) {
            this.request_id = request_id;
            this.item_name = item_name;
            this.item_desc = item_desc;
            this.item_img = item_img;
            this.item_point = item_point;
            this.item_status = item_status;
            this.datetime = datetime;
            this.product_id = product_id;
            this.refno = refno;

        }

        public String getDatetime() {
            return datetime;
        }

        public String getItem_desc() {
            return item_desc;
        }

        public String getItem_img() {
            return item_img;
        }

        public String getItem_name() {
            return item_name;
        }

        public String getItem_point() {
            return item_point;
        }

        public String getItem_status() {
            return item_status;
        }

        public String getRequest_id() {
            return request_id;
        }
        public String getProduct_id() {
            return product_id;
        }

        public String getRefno() {
            return refno;
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
