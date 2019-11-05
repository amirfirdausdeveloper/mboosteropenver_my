package org.app.mbooster.activity_folder;

import android.app.Activity;
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
import org.app.mbooster.Holder.ConstantHolder;
import org.app.mbooster.R;
import org.app.mbooster.listAdapter_folder.VoucherConversionAdapter;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.VoucherSelectorModel;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class VoucherSelectionActivity extends BaseActivity {

    Toolbar toolbar;

    TextView ev10Available, ev30Available, ev50Available;
    TextView selected10Text, selected30Text, selected50Text;
    TextView selectTotalQuantity, selectTotalValue;
    TextView allowTotalQuantity, allowTotalValue;
    LinearLayout selectTotalQuantityContainer, selectedTotalValueContainer;
    LinearLayout allowTotalQuantityContainer, allowTotalValueContainer;

    Button saveBtn;

    int totalEv10, totalEv30, totalEv50;

    LinearLayout ev10ll, ev30ll, ev50ll;

    AlertDialog adOverLoad;

    String product_id = "";
    String cart_id = "";

    VoucherSelectorModel mSelector;
//    Map<String, String> map;

    Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_selection);
        setupToolbar();
    }

    private void setupToolbar(){

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
                handleOnBackPress();
            }
        });

        initView();

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            product_id = extras.getString("product_id", "0");
            cart_id = extras.getString("cart_id", "0");
        }
        getVoucherSettingsAsync(SavePreferences.getUserID(this), cart_id, product_id);
    }

    private void initView(){
        ev10Available = findViewById(R.id.select_ev10_available);
        ev30Available = findViewById(R.id.select_ev30_available);
        ev50Available = findViewById(R.id.select_ev50_available);

        ev10ll = findViewById(R.id.select_ev10_container);
        ev30ll = findViewById(R.id.select_ev30_container);
        ev50ll = findViewById(R.id.select_ev50_container);

        selected10Text = findViewById(R.id.select_ev10_count);
        selected30Text = findViewById(R.id.select_ev30_count);
        selected50Text = findViewById(R.id.select_ev50_count);

        selectTotalQuantity = findViewById(R.id.total_quantity_selected_text);
        selectTotalValue = findViewById(R.id.total_value_select_text);
        allowTotalQuantity = findViewById(R.id.total_quantity_allow_text);
        allowTotalValue = findViewById(R.id.total_value_allow_text);

        selectTotalQuantityContainer = findViewById(R.id.total_quantity_selected_container);
        selectedTotalValueContainer = findViewById(R.id.total_value_select_container);
        allowTotalQuantityContainer = findViewById(R.id.total_quantity_allow_container);
        allowTotalValueContainer = findViewById(R.id.total_value_allow_container);
        saveBtn = findViewById(R.id.select_voucher_save);
    }

    private void setupView(){
        if(mSelector.getVoucherLimitType().equals("0")){
            selectTotalQuantityContainer.setVisibility(View.VISIBLE);
            allowTotalQuantityContainer.setVisibility(View.VISIBLE);
            allowTotalQuantity.setText(mSelector.getMaxVoucherQuantity());
        }else if(mSelector.getVoucherLimitType().equals("1")){
            allowTotalValueContainer.setVisibility(View.VISIBLE);
            allowTotalValue.setText(getString(R.string.MYR_CURRENCY_PREFIX) + mSelector.getMaxVoucherValueDisplay());
        }

        selectedTotalValueContainer.setVisibility(View.VISIBLE);

        selected10Text.setText(mSelector.getUseEV10()+getString(R.string.mbooster_evoucher_count_postfix));
        selected30Text.setText(mSelector.getUseEV30()+getString(R.string.mbooster_evoucher_count_postfix));
        selected50Text.setText(mSelector.getUseEV50()+getString(R.string.mbooster_evoucher_count_postfix));
        totalEv10 = Integer.valueOf(mSelector.getUseEV10());
        totalEv30 = Integer.valueOf(mSelector.getUseEV30());
        totalEv50 = Integer.valueOf(mSelector.getUseEV50());

        ev10Available.setText(getString(R.string.select_voucher_available)
                .replace("$VALUE$", mSelector.getAvailableEV10()));
        ev30Available.setText(getString(R.string.select_voucher_available)
                .replace("$VALUE$", mSelector.getAvailableEV30()));
        ev50Available.setText(getString(R.string.select_voucher_available)
                .replace("$VALUE$",  mSelector.getAvailableEV50()));

        ev10ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ev10Available = Integer.valueOf(mSelector.getAvailableEV10());
                popUpList(ConstantHolder.VOUCHER_TYPE.EV10, ev10Available,getString(R.string.select_ev10_quantity));
            }
        });
        ev30ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ev30Available = Integer.valueOf(mSelector.getAvailableEV30());
                popUpList(ConstantHolder.VOUCHER_TYPE.EV30, ev30Available,getString(R.string.select_ev30_quantity));
            }
        });
        ev50ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ev50Available = Integer.valueOf(mSelector.getAvailableEV50());
                popUpList(ConstantHolder.VOUCHER_TYPE.EV50, ev50Available,getString(R.string.select_ev50_quantity));
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPass()) {
                    postVoucherSelectionAsync();
                }
            }
        });

        updateTotal();
    }

    private boolean checkPass(){
        try{

            LogHelper.debug("[checkPass] map.get(voucher_limit_type) = " + mSelector.getVoucherLimitType());
            if(mSelector.getVoucherLimitType().equals("0")) {
                int totalQuantity = totalEv10 + totalEv30 + totalEv50;
                int maxVoucherQuantity = Integer.valueOf(mSelector.getMaxVoucherQuantity());
                if (totalQuantity > maxVoucherQuantity) {
                    LogHelper.debug("[checkPass][max_voucher_qty] pop");
                    popDialog(getString(R.string.select_voucher_exceed_limit).replace("$VALUE$", mSelector.getMaxVoucherQuantity()));
                    return false;
                }
            }else{
                double totalVoucherValue = (totalEv10*10) + (totalEv30*30) + (totalEv50*50);
//                DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                double maxVoucherValue = decimalFormat.parse(map.get("max_voucher_value")).doubleValue();
                if (totalVoucherValue > mSelector.getMaxVoucherValue()) {
                    LogHelper.debug("[checkPass][max_voucher_value] pop");
                    String replaceMsg = getString(R.string.MYR_CURRENCY_PREFIX) + mSelector.getMaxVoucherValue();
                    popDialog(getString(R.string.select_voucher_exceed_value).replace("$VALUE$", replaceMsg));
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void updateTotal(){
        int totalQuantity = totalEv10 + totalEv30 + totalEv50;
        selectTotalQuantity.setText(String.valueOf(totalQuantity));
        int totalVoucherValue = (totalEv10*10) + (totalEv30*30) + (totalEv50*50);
        DecimalFormat format = new DecimalFormat("0.00");
        selectTotalValue.setText(getString(R.string.MYR_CURRENCY_PREFIX)+ format.format(totalVoucherValue));
    }

    @Override
    public void noNetwork() {
        new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogTheme)
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

    public void handleOnBackPress() {
        this.finish();
    }

    private void popDialog(String msg){
        if(adOverLoad!=null){
            adOverLoad.isShowing();
            return;
        }
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
        popupBuilder.setMessage(msg);
        popupBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                postVoucherSelectionAsync();
            }
        });
        popupBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        popupBuilder.create().show();
    }


    private void popUpList(final ConstantHolder.VOUCHER_TYPE type, int totalAvailableCount, String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.list_evoucher_conversion);
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
        int[] arrayInteger = new int[totalAvailableCount+1];
        for(int i = 0; i <=totalAvailableCount; i ++){
            arrayInteger[i] = i;
        }

        VoucherConversionAdapter adapter = new VoucherConversionAdapter(context, arrayInteger);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int value = (int) parent.getAdapter().getItem(position);
                switch (type){
                    case EV10:
                        totalEv10 = value;
                        selected10Text.setText(value+getString(R.string.mbooster_evoucher_count_postfix));
                        break;
                    case EV30:
                        totalEv30 = value;
                        selected30Text.setText(value+getString(R.string.mbooster_evoucher_count_postfix));
                        break;
                    case EV50:
                        totalEv50 = value;
                        selected50Text.setText(value+getString(R.string.mbooster_evoucher_count_postfix));
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

    public void postVoucherSelectionAsync(){
        class getinfo extends AsyncTask<String, String, JSONObject> {

            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                flowerDialog = new ACProgressFlower.Builder(VoucherSelectionActivity.this)
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
                String success = "";
                try {
                    LogHelper.debug("[postVoucherSelectionAsync] = " + jsonObject.toString());
                    success = JSonHelper.getObjString(jsonObject, "success");
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                if(success.equals("1")){
                    setResult(RESULT_OK);
                    finish();
                }


            }

            @Override
            protected JSONObject doInBackground(String... params) {

                urlLink userFunctions = new urlLink();
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SavePreferences.getUserID(context));
                map.put("cart_id", cart_id);
                map.put("evqty_10", String.valueOf(totalEv10));
                map.put("evqty_30", String.valueOf(totalEv30));
                map.put("evqty_50", String.valueOf(totalEv50));
                JSONObject json = userFunctions.postVoucherSelection(map);
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    public void getVoucherSettingsAsync(final String user_id, final String cart_id, final String product_id)  {
        class getinfo extends AsyncTask<String, String, JSONObject> {

            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                flowerDialog = new ACProgressFlower.Builder(VoucherSelectionActivity.this)
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
                    LogHelper.debug("[getVoucherSettingsAsync] = " + jsonObject.toString());
//                    map = JSonHelper.parseUsableVoucher(jsonObject);
                    mSelector = JSonHelper.parseUsableVoucher(jsonObject);
                    setupView();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }


            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink userFunctions = new urlLink();
                JSONObject json = userFunctions.getVoucherSelection( user_id, cart_id, product_id);
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

}
