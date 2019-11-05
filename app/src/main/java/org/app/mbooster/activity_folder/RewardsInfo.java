package org.app.mbooster.activity_folder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;

import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class RewardsInfo extends BaseActivity{
    static Activity activity;
    Context context;
    private Toolbar toolbar;
    private NetworkStateReceiver networkStateReceiver;
    private int network = 0;
    private AlertDialog alertDialog;

    public static final String EMPTY_FIELD = "0";

    //View
    private LinearLayout pointContainer;
    private TextView mapoint;
    private ImageView menu_header;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rewards);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        context = activity = this;
        toolbar = findViewById(R.id.toolbar);
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
        mapoint = (TextView) findViewById(R.id.mapoint);
        menu_header = (ImageView) findViewById(R.id.menu_header);
//        user_mairtime(SavePreferences.getUserID(this), SavePreferences.getApplanguage(this));

        getAllRewardsAsync(SavePreferences.getUserID(this), false);
    }

    private void setupView(Map<String, String> _map){

        pointContainer = findViewById(R.id.point_info_container);
        if(pointContainer!=null){
            pointContainer.removeAllViews();
        }

        setupRewards(_map);
        setupVoucher(_map);
    }

    /**
     * Add reward view
     * @param _map - data from server
     */
    private void setupRewards(Map<String, String> _map){
        LayoutInflater inflater = LayoutInflater.from(RewardsInfo.this);
        View getCustomMMSpotView = setuRewardsView(inflater
                                            , getString(R.string.rewards_mmspot_title)
                                            , (_map.containsKey("total_ma"))?_map.get("total_ma"):EMPTY_FIELD
                                            , getString(R.string.rewards_m_a)
                                            , R.mipmap.icon_mmspot_green);
        pointContainer.addView(getCustomMMSpotView);

        if(_map.containsKey("package_series") && _map.get("package_series").equalsIgnoreCase("4")) {
            View getCustomMCredit = setuRewardsView(inflater
                    , getString(R.string.rewards_mbooster_credit)
                    , (_map.containsKey("total_credit_wallet")) ? _map.get("total_credit_wallet") : EMPTY_FIELD
                    , getString(R.string.rewards_cr)
                    , R.mipmap.icon_credit_point);
            pointContainer.addView(getCustomMCredit);
        }else{
            LogHelper.debug("[package_series] [not found]");
        }

        View getCustomMeVoucher = setuRewardsView(inflater
                , getString(R.string.rewards_mbooster_evcouher)
                , (_map.containsKey("total_ev_value"))?_map.get("total_ev_value"):EMPTY_FIELD
                , getString(R.string.rewards_ev)
                , R.mipmap.icon_evoucher);
        pointContainer.addView(getCustomMeVoucher);
    }

    private View setuRewardsView(LayoutInflater inflater, String name, String point, String pointType, int iconId){
        View customView= inflater.inflate(R.layout.item_rewards_point, null);

        ImageView customViewIcon = customView.findViewById(R.id.rewards_point_icon);
        TextView customViewName = customView.findViewById(R.id.rewards_point_name);
        TextView customViewPoint = customView.findViewById(R.id.rewards_point_points);
        TextView customViewPointType = customView.findViewById(R.id.rewards_point_type);

        customViewIcon.setImageResource(iconId);
        customViewName.setText(name);
        customViewPoint.setText(point);
        customViewPointType.setText(pointType);
        return customView;
    }

    /**
     * ADd VoucherModel User
     * @param _map - data from server
     */
    private void setupVoucher(Map<String, String> _map) {
        LayoutInflater inflater = LayoutInflater.from(RewardsInfo.this);
        View v10 = setupVoucherView(inflater
                , "10"
                , (_map.containsKey("total_ev10"))?_map.get("total_ev10"):EMPTY_FIELD
                , R.mipmap.icon_ev10);
        pointContainer.addView(v10);

        View v30 = setupVoucherView(inflater
                , "30"
                , (_map.containsKey("total_ev30"))?_map.get("total_ev30"):EMPTY_FIELD
                , R.mipmap.icon_ev30);
        pointContainer.addView(v30);

        View v50 = setupVoucherView(inflater
                , "50"
                , (_map.containsKey("total_ev50"))?_map.get("total_ev50"):EMPTY_FIELD
                , R.mipmap.icon_ev50);
        pointContainer.addView(v50);
    }


    private View setupVoucherView(LayoutInflater inflater, String name, String count, int iconId){
        View viewVoucher= inflater.inflate(R.layout.item_evoucher_info, null);
        ImageView voucherIcon = viewVoucher.findViewById(R.id.voucher_icon);
        TextView voucherName = viewVoucher.findViewById(R.id.voucher_name);
        TextView voucherCount = viewVoucher.findViewById(R.id.voucher_count);

        StringBuilder countBuilder = new StringBuilder();
        countBuilder.append(count);
        countBuilder.append(getString(R.string.mbooster_evoucher_count_postfix));

        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(getString(R.string.mbooster_evoucher_prefix));
        nameBuilder.append(" " + name);

        voucherName.setText(nameBuilder.toString());
        voucherCount.setText(countBuilder.toString());
        voucherIcon.setImageResource(iconId);

        return viewVoucher;
    }

    public void getAllRewardsAsync(final String userId, final boolean refresh) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                flowerDialog = new ACProgressFlower.Builder(RewardsInfo.this)
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
                    if (flowerDialog.isShowing()) {
                        flowerDialog.dismiss();
                    }
                    LogHelper.debug("[getAllRewardsAsync] = " + jsonObject.toString());
                    String response = jsonObject.getString("success");
                    if (response.equals("1")) {
                        Map<String, String> map = JSonHelper.parseAllRewards(jsonObject);
                        setupView(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (refresh) {

                }

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink userFunctions = new urlLink();
                JSONObject json = userFunctions.getAllRewards(userId, SavePreferences.getApplanguage(RewardsInfo.this));
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    @Override
    public void noNetwork() {
        alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
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
                .create();

        if (network == 0) {

            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

}