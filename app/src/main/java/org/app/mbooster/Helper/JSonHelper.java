package org.app.mbooster.Helper;

import org.app.mbooster.model_folder.ProductModel;
import org.app.mbooster.model_folder.ShoppingBagItem;
import org.app.mbooster.model_folder.VoucherSelectorModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSonHelper {

    public static final String MAINTAINANCE_ANDROID = "maintenance";

    public static boolean getObjBoolean(JSONObject json, String tag, boolean defaultValue) {
        boolean result = defaultValue;
        if (json == null || json.equals("")) {
            return result;
        }
        try {

            if (json.has(tag)) {
                result = json.getBoolean(tag);
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return result;
    }

    public static String getObjString(JSONObject json, String tag) {
        return getObjString(json, tag, "");
    }

    public static String getObjString(JSONObject json, String tag, String defaulText) {
        String result = "";
        if (json == null || json.equals("")) {
            return result;
        }
        try {

            if (json.has(tag)) {
                result = json.getString(tag);
                if (result == null || result.length() < 0) {
                    return "";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result = defaulText;
        }
        return result;
    }

    public static int getObjInt(JSONObject json, String tag) {
        int result = 0;
        if (json == null) {
            return result;
        }
        try {

            result = json.getInt(tag);
            if (result == 0) {
                return 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getObjInt(JSONObject json, String tag, int defaultValue) {
        int result = 0;
        if (json == null) {
            return result;
        }
        try {

            result = json.getInt(tag);
            if (result == 0) {
                return 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result = defaultValue;
        }
        return result;
    }

    public static double getObjDouble(JSONObject json, String tag) {
        double result = 0;
        if (json == null) {
            return result;
        }
        try {

            result = json.getDouble(tag);
            if (result == 0) {
                return 0;
            }
        } catch (JSONException e) {
            LogHelper.error(e.getLocalizedMessage());
        }
        return result;
    }

    public static List<ShoppingBagItem> parseShoppingBagItem(JSONArray jsonArray, String isSelfCollect){
        List<ShoppingBagItem> list = new ArrayList<>();
        try{
            LogHelper.debug("[parseProductList] = " + jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ShoppingBagItem mS = new ShoppingBagItem();
                mS.setCart_id(getObjString(json, "cart_id"));
                mS.setProduct_id(getObjString(json, "product_id"));
                mS.setProduct_name(getObjString(json, "product_name"));
                mS.setProduct_qty(getObjString(json, "product_qty"));
                mS.setProduct_img(getObjString(json, "product_img"));
                mS.setAmount_point(getObjString(json, "amount_point"));

                mS.setTotal_amount_point(getObjString(json, "total_product_point"));
                mS.setSupplier_name(getObjString(json, "supplier_name"));
                mS.setShippingcost(getObjString(json, "shipping_cost"));
                mS.setVoucher_status(getObjString(json, "voucher_status"));

                mS.setIsSelfCollect(isSelfCollect);

                mS.setIs_allowed_self_collect_item(getObjString(json, "is_allowed_self_collect_item"));
                mS.setIs_self_collect(getObjString(json, "isSelfCollect"));
                mS.setTotalma(getObjString(json, "totSplitAmt"));
                mS.setTotalmyr(getObjString(json, "totCashAmt"));
                mS.setTotalvouchervalue(getObjString(json, "totVcrAmt"));
                mS.setRedeem_type(getObjString(json, "redeem_type"));
                list.add(mS);

            }
        }catch (Exception e){
            LogHelper.error(e.getLocalizedMessage());
        }
        return list;
    }

    public static List<ProductModel> parseProductList(JSONArray jsonArray){
        List<ProductModel> list = new ArrayList<>();
        try{
            LogHelper.debug("[parseProductList] = " + jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ProductModel mProduct = new ProductModel();
                mProduct.setProductid(getObjString(json,"product_id"));
                mProduct.setProductname(getObjString(json,"product_name"));
                mProduct.setProductpts(getObjString(json,"amount_point"));
                mProduct.setProductairtime(getObjString(json,"amount_airtime"));
                mProduct.setProductimg(getObjString(json,"product_img"));
                mProduct.setAmountcost(getObjString(json,"amount_cost"));
                mProduct.setVoucher_status(getObjString(json,"evoucher_status"));
                mProduct.setProduct_label(getObjString(json,"product_label"));
                mProduct.setDiscount_perc(getObjString(json,"discount_perc"));
                mProduct.setBundle_voucher(getObjString(json,"bundle_voucher"));
                mProduct.setQuantity(getObjInt(json,"product_qty", -1));

                String maValue = getObjString(json,"splitpay_ma_value");
                String maxVoucher = getObjString(json,"max_voucher_value");
                String pvValue = getObjString(json,"pv_value");
                String mpValue = getObjString(json,"mp_value");
                DecimalFormat format = new DecimalFormat("#,###.##");

                mProduct.setMaxMAValue(format.parse(maValue).doubleValue());
                mProduct.setMaxVoucherValue(format.parse(maxVoucher).intValue());
                mProduct.setPvValue(format.parse(pvValue).doubleValue());
                mProduct.setMpValue(format.parse(mpValue).doubleValue());

                mProduct.setProduct_color(getObjString(json, "product_color"));

                list.add(mProduct);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static List<ProductModel> parseRedemptionList(JSONArray jsonArray){
        List<ProductModel> list = new ArrayList<>();
        try{
            LogHelper.debug("[parseProductList] = " + jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ProductModel mProduct = new ProductModel();
                mProduct.setProductid(getObjString(json,"product_id"));
                mProduct.setProductname(getObjString(json,"product_name"));
                mProduct.setProductpts(getObjString(json,"amount_point"));
                mProduct.setProductairtime(getObjString(json,"amount_airtime"));
                mProduct.setProductimg(getObjString(json,"product_img"));
                mProduct.setAmountcost(getObjString(json,"amount_cost"));
                mProduct.setProduct_label(getObjString(json,"product_label"));
                mProduct.setQuantity(getObjInt(json,"product_qty", -1));

                DecimalFormat format = new DecimalFormat("#,###.##");


                mProduct.setProduct_color(getObjString(json, "product_color"));

                list.add(mProduct);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static String parseCreditPointsOnly(JSONObject json){
        String value = "0";
        try{
            value = getObjString(json, "total_credit_wallet");
        }catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static Map<String, String> parseAllRewards(JSONObject json){
        Map<String, String> map = new HashMap<>();
        try{
            map.put("total_ma", getObjString(json, "total_ma", "0"));
            map.put("total_credit_wallet", getObjString(json, "total_credit_wallet", "0"));
            map.put("total_ev_value", getObjString(json, "total_ev_value", "0"));
            map.put("total_ev10", getObjString(json, "total_ev10", "0"));
            map.put("total_ev30", getObjString(json, "total_ev30", "0"));
            map.put("total_ev50", getObjString(json, "total_ev50", "0"));
            map.put("package_series", getObjString(json, "package_series"));
            map.put("bizUser", getObjString(json, "bizUser"));
            map.put("total_ev10_amount", getObjString(json, "total_ev10_amount", "0"));
            map.put("total_ev30_amount", getObjString(json, "total_ev30_amount", "0"));
            map.put("total_ev50_amount", getObjString(json, "total_ev50_amount", "0"));

        }catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static VoucherSelectorModel parseUsableVoucher(JSONObject json){
        VoucherSelectorModel mVSM = new VoucherSelectorModel();
        try{
            mVSM.setUseEV10(getObjString(json, "use_qty_ev10"));
            mVSM.setUseEV30(getObjString(json, "use_qty_ev30"));
            mVSM.setUseEV50(getObjString(json, "use_qty_ev50"));
            mVSM.setAvailableEV10(getObjString(json, "available_qty_ev10"));
            mVSM.setAvailableEV30(getObjString(json, "available_qty_ev30"));
            mVSM.setAvailableEV50(getObjString(json, "available_qty_ev50"));
            mVSM.setVoucherLimitType(getObjString(json, "voucher_limit_type"));
            mVSM.setMaxVoucherQuantity(getObjString(json, "max_voucher_qty"));
            mVSM.setMaxVoucherValueDisplay(getObjString(json, "max_voucher_value"));
            mVSM.setMaxVoucherValue(getObjInt(json, "max_voucher_value_int"));

        }catch (Exception e) {
            e.printStackTrace();
        }

        return mVSM;
    }

//    public static Map<String, String> parseUsableVoucher(JSONObject json){
//        Map<String, String> map = new HashMap<>();
//        try{
//            map.put("use_qty_ev10", getObjString(json, "use_qty_ev10"));
//            map.put("use_qty_ev30", getObjString(json, "use_qty_ev30"));
//            map.put("use_qty_ev50", getObjString(json, "use_qty_ev50"));
//            map.put("available_qty_ev10", getObjString(json, "available_qty_ev10"));
//            map.put("available_qty_ev30", getObjString(json, "available_qty_ev30"));
//            map.put("available_qty_ev50", getObjString(json, "available_qty_ev50"));
//            map.put("voucher_limit_type", getObjString(json, "voucher_limit_type"));
//            map.put("max_voucher_qty", getObjString(json, "max_voucher_qty"));
//            map.put("max_voucher_value", getObjString(json, "max_voucher_value"));
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return map;
//    }

}
