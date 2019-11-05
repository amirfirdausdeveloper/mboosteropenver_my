package org.app.mbooster.shopping_mall;

import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import org.app.mbooster.Dialog.DialogFragmentUniversalPW;
import org.app.mbooster.Dialog.DialogFragmentUniversalVoucher;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.shopping_mall.AddVoucher;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Voucher extends AppCompatActivity implements DialogFragmentUniversalVoucher.onSubmitListener {
    private ListView listview;
    private ArrayList<VoucherItem> items = new ArrayList<>();
    private VoucherAdapter adapter;
    private Toolbar toolbar;
    private TextView add_voucher_btn;
    private DialogFragmentUniversalVoucher a;
    private TextView total_voucher_amount_tv;
    private RelativeLayout no_voucher;

    private String cart_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Intent i = getIntent();
        i.getExtras();
        cart_id = i.getStringExtra("cart_id");
        String product_id = i.getStringExtra("product_id");

        Log.i("v69p", product_id);
        Log.i("v69c", cart_id);

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
                Log.i("setNavigationOnClickListener", "setNavigationOnClickListener666");
                Intent intent = new Intent();
                intent.putExtra("voucher_count", String.valueOf(items.size()));
                setResult(RESULT_OK);
                finish();
            }
        });

        listview = (ListView) findViewById(R.id.listview);
        adapter = new VoucherAdapter(this, items);
        listview.setAdapter(adapter);

        add_voucher_btn = (TextView) findViewById(R.id.add_voucher_btn);
        add_voucher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Voucher.this, AddVoucher.class);
                intent.putExtra("product_id", product_id);
                intent.putExtra("cart_id", cart_id);
                startActivityForResult(intent, 1);

            }
        });

        total_voucher_amount_tv = (TextView) findViewById(R.id.total_voucher_amount_tv);

        no_voucher = (RelativeLayout) findViewById(R.id.no_voucher);

        VoucherList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                VoucherList();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//        Intent intent = new Intent();
//        intent.putExtra("voucher_count", String.valueOf(items.size()));
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void setOnSubmitListener(String arg) {
        if (arg.equals(getString(R.string.cancel))) {
            a.dismiss();
        } else {
            if (arg.trim().length() != 0) {
                //arg as voucher_code;
                add_voucher(arg);
                //check_out_test();
                Helper.CloseKeyboard(Voucher.this);
            } else {
                Toast.makeText(this, R.string.enter_voucher_to_submit, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void add_voucher(final String voucher_code) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(Voucher.this)
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
                        VoucherList();
                    }
                    Toast.makeText(Voucher.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().add_voucher(SavePreferences.getUserID(Voucher.this), voucher_code.toUpperCase(),SavePreferences.getApplanguage(Voucher.this));
            }
        }
        new getinfo().execute();
    }

    private void VoucherList() {
        Log.i("VoucherList1", "inside VoucherList 1");
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                items.clear();
                flowerDialog = new ACProgressFlower.Builder(Voucher.this)
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
                    JSONArray vouchers = jsonObject.getJSONArray("array");
                    for (int i = 0; i < vouchers.length(); i++) {
                        JSONObject json = vouchers.getJSONObject(i);
                        items.add(new VoucherItem(json.getString("voucher_id"), json.getString("voucher_code"), json.getString("amount")));
                    }
                    String total_voucher_amount = jsonObject.getString("total_amount");
                    total_voucher_amount_tv.setText(getString(R.string.currency) + total_voucher_amount);
                    if (items.size() > 0) {
                        no_voucher.setVisibility(View.GONE);
                    } else {
                        no_voucher.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getProductEVoucherList(SavePreferences.getUserID(Voucher.this), cart_id);
            }
        }
        new getinfo().execute();
    }

    //remove_voucher
    private void remove_voucher(final String voucher_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        Toast.makeText(Voucher.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        VoucherList();
                    } else {
                        Toast.makeText(Voucher.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().product_remove_Evoucher(SavePreferences.getUserID(Voucher.this), voucher_id, SavePreferences.getApplanguage(Voucher.this));
            }
        }
        new getinfo().execute();
    }

    class VoucherAdapter extends BaseAdapter {
        ArrayList<VoucherItem> items;
        LayoutInflater layoutInflater;

        public VoucherAdapter(Context context, ArrayList<VoucherItem> items) {
            layoutInflater = LayoutInflater.from(context);
            this.items = items;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = layoutInflater.inflate(R.layout.voucher_list_item, null);
            SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);

            //set show mode.
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));

            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });

            RelativeLayout remove = (RelativeLayout) view.findViewById(R.id.bottom_wrapper);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("remove_voucher666", items.get(i).id);
                    remove_voucher(items.get(i).id);
                }
            });


            TextView code = (TextView) view.findViewById(R.id.code);
            TextView amount = (TextView) view.findViewById(R.id.amount);

            code.setText(items.get(i).code);
            amount.setText(getString(R.string.currency) + items.get(i).amount);

            return view;
        }
    }

    class VoucherItem {
        String id, code, amount;

        public VoucherItem(String id, String code, String amount) {
            this.id = id;
            this.code = code;
            this.amount = amount;
        }
    }
}