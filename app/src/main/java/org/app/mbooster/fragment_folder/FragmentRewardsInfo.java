package org.app.mbooster.fragment_folder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.app.mbooster.Helper.JSonHelper;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.Holder.ConstantHolder;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MCPConvertActivity;
import org.app.mbooster.activity_folder.eVoucherListing;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;

import java.util.Map;

public class FragmentRewardsInfo extends Fragment {
    Context context;
    ProgressBar progressbar;
    private AlertDialog alertDialog;

    public static final String EMPTY_FIELD = "0";
    public static final String EV10 = "0";
    public static final String EV30 = "1";
    public static final String EV50 = "2";

    //View
    private LinearLayout pointContainer;
    private TextView mapoint;
    private ImageView menu_header;
    View disableView;

    boolean showMaOption = false;

    public static FragmentRewardsInfo instance;

    public static FragmentRewardsInfo getInstance(){
        if(instance == null){
            instance = new FragmentRewardsInfo();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, null);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapoint = (TextView) view.findViewById(R.id.mapoint);
        menu_header = (ImageView) view.findViewById(R.id.menu_header);
        pointContainer = view.findViewById(R.id.point_info_container);
        progressbar = view.findViewById(R.id.progressbar);
        disableView = view.findViewById(R.id.disable_view);
        getAllRewardsAsync(SavePreferences.getUserID(context), false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(pointContainer!=null && pointContainer.getChildCount()<=0) {
            getAllRewardsAsync(SavePreferences.getUserID(context), false);
        }
    }

    public void refreshPage(){
        getAllRewardsAsync(SavePreferences.getUserID(context), false);
    }

    private void setupView(Map<String, String> _map){

        if(pointContainer!=null){
            pointContainer.removeAllViews();
        }
        setupRewards(_map);
        setupVoucher(_map);
    }

    private void setupRewards(Map<String, String> _map){
        LayoutInflater inflater = LayoutInflater.from(context);
        if(showMaOption) {
            View getCustomMMSpotView = setuRewardsView(inflater
                    , getString(R.string.rewards_mmspot_title)
                    , (_map.containsKey("total_ma")) ? _map.get("total_ma") : EMPTY_FIELD
                    , getString(R.string.rewards_m_a)
                    , R.mipmap.icon_mmspot_green);
            pointContainer.addView(getCustomMMSpotView);
        }

        if(_map.containsKey("bizUser") && _map.get("bizUser").equalsIgnoreCase("1")) {
            View getCustomMCredit = setuRewardsView(inflater
                    , getString(R.string.rewards_mbooster_credit)
                    , (_map.containsKey("total_credit_wallet")) ? _map.get("total_credit_wallet") : EMPTY_FIELD
                    , getString(R.string.postfix_mp).toUpperCase()
                    , R.mipmap.icon_credit_point);
            getCustomMCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(context, MCPConvertActivity.class);
//                    getActivity().startActivityForResult(i, ConstantHolder.REQUEST_CONVERT_MCP);
                }
            });
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

    private void setupVoucher(Map<String, String> _map) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v10 = setupVoucherView(inflater
                , "10"
                , (_map.containsKey("total_ev10"))?_map.get("total_ev10"):EMPTY_FIELD
                , (_map.containsKey("total_ev10_amount"))?_map.get("total_ev10_amount"):EMPTY_FIELD
                , R.mipmap.icon_ev10);
        v10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickViewVoucher(EV10);
            }
        });
        pointContainer.addView(v10);

        View v30 = setupVoucherView(inflater
                , "30"
                , (_map.containsKey("total_ev30"))?_map.get("total_ev30"):EMPTY_FIELD
                , (_map.containsKey("total_ev30_amount"))?_map.get("total_ev30_amount"):EMPTY_FIELD
                , R.mipmap.icon_ev30);
        v30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickViewVoucher(EV30);
            }
        });
        pointContainer.addView(v30);

        View v50 = setupVoucherView(inflater
                , "50"
                , (_map.containsKey("total_ev50"))?_map.get("total_ev50"):EMPTY_FIELD
                , (_map.containsKey("total_ev50_amount"))?_map.get("total_ev50_amount"):EMPTY_FIELD
                , R.mipmap.icon_ev50);
        v50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickViewVoucher(EV50);
            }
        });
        pointContainer.addView(v50);

        LogHelper.debug("view count " + pointContainer.getChildCount());
    }


    private View setupVoucherView(LayoutInflater inflater, String name, String count, String price, int iconId){
        View viewVoucher= inflater.inflate(R.layout.item_evoucher_info, null);
        ImageView voucherIcon = viewVoucher.findViewById(R.id.voucher_icon);
        TextView voucherName = viewVoucher.findViewById(R.id.voucher_name);
        TextView voucherCount = viewVoucher.findViewById(R.id.voucher_count);
        TextView voucherPrice = viewVoucher.findViewById(R.id.voucher_price);

        StringBuilder countBuilder = new StringBuilder();
        countBuilder.append(count);
        countBuilder.append(getString(R.string.mbooster_evoucher_count_postfix));

        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(getString(R.string.mbooster_evoucher_prefix));
        nameBuilder.append(" " + name);

        voucherPrice.setText(context.getString(R.string.currency) + price);
        voucherName.setText(nameBuilder.toString());
        voucherCount.setText(countBuilder.toString());
        voucherIcon.setImageResource(iconId);

        return viewVoucher;
    }

    private void clickViewVoucher(String value){
        Intent intent = new Intent(context, eVoucherListing.class);
        Bundle extras = new Bundle();
        extras.putString("id", value);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void getAllRewardsAsync(final String userId, final boolean refresh) {
        class getinfo extends AsyncTask<String, String, JSONObject> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                disableView.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.VISIBLE);

            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    LogHelper.debug("[getAllRewardsAsync] = " + jsonObject.toString());
                    String response = jsonObject.getString("success");
//                    if (response.equals("1")) {
                    showMaOption = JSonHelper.getObjBoolean(jsonObject,"showMaOption", false);
                    Map<String, String> map = JSonHelper.parseAllRewards(jsonObject);
                    setupView(map);
//                    }
                } catch (Exception e) {
                    LogHelper.debug(e.getLocalizedMessage());
                }


                disableView.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);

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

}