package org.app.mbooster.activity_folder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.listAdapter_folder.VoucherConversionAdapter;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MCPConvertActivity extends BaseActivity {

    double currentCreditPoints;

    private enum VOUCHER_TYPE{
        EV10,
        EV30,
        EV50
    }

    Dialog dialog;

    Toolbar toolbar;
    TextView tv_ev10total, tv_ev30total, tv_ev50total;
    TextView tv_ev10count, tv_ev30count, tv_ev50count;
    TextView tv_balance, tv_totalVoucher;
    TextView tv_mcpPoint;
    Button convertBtn;
    LinearLayout ev10_ll, ev30_ll, ev50_ll;


    public static final int[] VALUE_LIST = {0,1,2,3,4,5,6,7,8,9,10};
    public static final int VALUE10 = 10;
    public static final int VALUE30 = 30;
    public static final int VALUE50 = 50;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversion);

        setupToolbar();

        ev10_ll = findViewById(R.id.conversion_ev10_container);
        ev30_ll = findViewById(R.id.conversion_ev30_container);
        ev50_ll = findViewById(R.id.conversion_ev50_container);

        tv_ev10count = findViewById(R.id.conversion_ev10_count);
        tv_ev30count = findViewById(R.id.conversion_ev30_count);
        tv_ev50count = findViewById(R.id.conversion_ev50_count);

        tv_ev10total = findViewById(R.id.conversion_ev10_count_total);
        tv_ev30total = findViewById(R.id.conversion_ev30_count_total);
        tv_ev50total = findViewById(R.id.conversion_ev50_count_total);

        tv_balance = findViewById(R.id.conversion_balance);
        tv_totalVoucher = findViewById(R.id.conversion_total_value);

        convertBtn = findViewById(R.id.convert_btn);

        tv_mcpPoint = findViewById(R.id.rewards_point_points);

        getAllRewardsAsync(SavePreferences.getUserID(context), false);
        setupClickView();

    }

    private void setupToolbar(){
        toolbar = findViewById(R.id.toolbar);
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

    @Override
    public void noNetwork() {

    }

    private void setupClickView(){
        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = getSelectionData();
                if(map!=null) {
                    postConvertVoucher(map);
                }
            }
        });
        ev10_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpList(VOUCHER_TYPE.EV10,getString(R.string.select_ev10_quantity));
            }
        });
        ev30_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpList(VOUCHER_TYPE.EV30,getString(R.string.select_ev30_quantity));
            }
        });
        ev50_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpList(VOUCHER_TYPE.EV50,getString(R.string.select_ev50_quantity));
            }
        });
    }

    private void popUpList(final VOUCHER_TYPE type, String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.list_evoucher_conversion);
        dialog.setTitle(title);
        ListView list = dialog.findViewById(R.id.List);
        ImageView imageView_cancel = dialog.findViewById(R.id.imageView_cancel);
        TextView textView_title = dialog.findViewById(R.id.textView_title);
        textView_title.setText(title);
        imageView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        VoucherConversionAdapter adapter = new VoucherConversionAdapter(context, VALUE_LIST);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int value = (int) parent.getAdapter().getItem(position);
                double tempBalance = (double) tv_balance.getTag();
                switch (type){
                    case EV10:
                        if(value!=0 && (tempBalance-(value*10) < 0)){
                            showErrorMessage(getString(R.string.conversion_insufficient));
                            break;
                        }
                        tv_ev10count.setTag(value);
                        tv_ev10count.setText(value+context.getString(R.string.mbooster_evoucher_count_postfix));
                        break;
                    case EV30:
                        if(value!=0 && (tempBalance-(value*30) < 0)){
                            showErrorMessage(getString(R.string.conversion_insufficient));
                            break;
                        }
                        tv_ev30count.setText(value+context.getString(R.string.mbooster_evoucher_count_postfix));
                        tv_ev30count.setTag(value);
                        break;
                    case EV50:
                        if(value!=0 && (tempBalance-(value*50) < 0)){
                            showErrorMessage(getString(R.string.conversion_insufficient));
                            break;
                        }
                        tv_ev50count.setText(value+context.getString(R.string.mbooster_evoucher_count_postfix));
                        tv_ev50count.setTag(value);
                        break;
                }
                updateTotal();
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        list.setAdapter(adapter);
        dialog.show();
    }

    private void updateTotal(){
        try {
            int ev10total, ev30total, ev50total;
            int totalEv = 0;
            if (tv_ev10count.getTag() != null) {
                int ev10Count = (int) tv_ev10count.getTag();
                ev10total = ev10Count *VALUE10;
                totalEv+=ev10total;
                tv_ev10total.setText(String.valueOf(ev10total)+" " + getString(R.string.rewards_ev));
            }
            if (tv_ev30count.getTag() != null) {
                int ev30Count = (int) tv_ev30count.getTag();
                ev30total = ev30Count *VALUE30;
                totalEv+=ev30total;
                tv_ev30total.setText(String.valueOf(ev30total)+" " + getString(R.string.rewards_ev));
            }
            if (tv_ev50count.getTag() != null) {
                int ev50Count = (int) tv_ev50count.getTag();
                ev50total = ev50Count *VALUE50;
                totalEv+=ev50total;
                tv_ev50total.setText(String.valueOf(ev50total) +" " + getString(R.string.rewards_ev));
            }

            double balanceValue = currentCreditPoints - totalEv;
            DecimalFormat format = new DecimalFormat("0.00");
            tv_totalVoucher.setTag(totalEv);
            tv_totalVoucher.setText(String.valueOf(totalEv)+" " + getString(R.string.rewards_ev));
            tv_balance.setTag(balanceValue);
            tv_balance.setText(format.format(balanceValue)+ " " +getString(R.string.rewards_ev));//String.valueOf(balanceValue)+" " + getString(R.string.rewards_ev));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getSelectionData(){
        int ev10Count = 0;
        if (tv_ev10count.getTag() != null) {
            ev10Count = (int) tv_ev10count.getTag();
        }
        int ev30Count = 0;
        if (tv_ev30count.getTag() != null) {
            ev30Count = (int) tv_ev30count.getTag();
        }
        int ev50Count = 0;
        if (tv_ev50count.getTag() != null) {
            ev50Count = (int) tv_ev50count.getTag();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", SavePreferences.getUserID(MCPConvertActivity.this));
        map.put("evqty_10", String.valueOf(ev10Count));
        map.put("evqty_30", String.valueOf(ev30Count));
        map.put("evqty_50", String.valueOf(ev50Count));
        int totalVoucher = 0;
        if(tv_totalVoucher!=null && tv_totalVoucher.getTag()!=null) {
            totalVoucher = (int) tv_totalVoucher.getTag();
        }
        map.put("total_voucher_value", String.valueOf(totalVoucher));
        map.put("credit_before", String.valueOf(currentCreditPoints));
        double balanceValue = 0;
        if(tv_balance.getTag()!=null) {
            balanceValue = (double) tv_balance.getTag();
        }
        map.put("credit_after", String.valueOf(balanceValue));

        if(totalVoucher==0){
            showErrorMessage(getString(R.string.conversion_nopoint));
            return null;
        }

        return map;
    }

    public void postConvertVoucher(final HashMap<String, String> params){

        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(MCPConvertActivity.this)
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

                try {
                    LogHelper.debug("[postConvertVoucher] = " + jsonObject.toString());
                    String response = JSonHelper.getObjString(jsonObject, "status");
                    String msg =  JSonHelper.getObjString(jsonObject, "msg");
                    Toast.makeText(context, msg, Toast.LENGTH_LONG);
                    if (response.equals("1")) {
                        setResult(RESULT_OK);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }



            }

            @Override
            protected JSONObject doInBackground(String... data) {

                urlLink userFunctions = new urlLink();
                JSONObject json = userFunctions.postConvertCredit(params);
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();


    }

    public void getAllRewardsAsync(final String userId, final boolean refresh) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(MCPConvertActivity.this)
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

                try {
                    LogHelper.debug("[getAllRewardsAsync] = " + jsonObject.toString());
                    String response = jsonObject.getString("success");
                    if (response.equals("1")) {
                        String currentPointsText = JSonHelper.parseCreditPointsOnly(jsonObject);
                        tv_mcpPoint.setText(currentPointsText);
                        NumberFormat format = NumberFormat.getInstance();
                        Number number = format.parse(currentPointsText);
                        currentCreditPoints = number.doubleValue();
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        tv_balance.setTag(currentCreditPoints);
                        tv_balance.setText(decimalFormat.format(currentCreditPoints));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }



                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }

                if (refresh) {

                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink userFunctions = new urlLink();
                JSONObject json = userFunctions.getAllRewards(userId, SavePreferences.getApplanguage(context));
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    private void showErrorMessage(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }

}
