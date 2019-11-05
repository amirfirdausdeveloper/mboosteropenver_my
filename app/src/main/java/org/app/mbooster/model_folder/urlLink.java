package org.app.mbooster.model_folder;

import android.content.Context;
import android.util.Log;

import org.app.mbooster.Helper.JSONParser;
import org.app.mbooster.Helper.JSONParser2;
import org.app.mbooster.Helper.LogHelper;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by User on 26/8/2016.
 */
public class urlLink {

    private JSONParser jsonParser;
    private JSONParser2 jsonParser2;

    private static int api_mode = 0;

    //********************************************************************************************************************************************************//
    //Url Link part
    //********************************************************************************************************************************************************//

    public static String androidDirectoryPath = getBaseURL();//"https://www.mbooster.my/appV2/";
    public static String nonePath = "https://www.mbooster.my/";

//    public static String loginURL = androidDirectoryPath + "login_mbasia.php";
    public static String loginURL = androidDirectoryPath + "login_all_user.php";
    public static String forgetPasswordURL = androidDirectoryPath + "forget_password.php";
    public static String URLgetUser = androidDirectoryPath + "get_user.php";
    public static String URLleftMenu = androidDirectoryPath + "left_menu.php";
    public static String changepasswordURL = androidDirectoryPath + "change_password.php";
    public static String user_registerURL = androidDirectoryPath + "user_register.php";
    public static String user_changeavatar = androidDirectoryPath + "user_changeavatar.php";
    public static String category = androidDirectoryPath + "shop/category.php";
    public static String subcategory = androidDirectoryPath + "shop/subcategory.php";
    public static String getproductlist = androidDirectoryPath + "shop/product_list.php";
    public static String getRedemptionProduct = androidDirectoryPath + "shop/redemption.php";
    public static String getSpinnerRedemption = androidDirectoryPath + "shop/redemption_category.php";
    public static String getproductDetails = androidDirectoryPath + "shop/product_detail.php";

    public static String getRedemptionCheckout = androidDirectoryPath + "shop/redemption_checkout.php";
    public static String getProductFullDesc = androidDirectoryPath + "shop/product_full_desc.php";
    public static String wishitem = androidDirectoryPath + "shop/wishlist_item.php";
    public static String addtobagURL = androidDirectoryPath + "shop/add_to_bag.php";
    public static String addtobagURLRedemption = androidDirectoryPath + "shop/redemption_cart.php";
    //public static String shopping_bag_listURL = androidDirectoryPath + "shop/shopping_bag_list3.php";
    //public static String shopping_bag_listURL = androidDirectoryPath + "shop/shopping_bag_list4.php";
    public static String shopping_bag_listURL = androidDirectoryPath + "shop/shopping_bag_list5.php";
    public static String shopping_bag_list_kwURL = androidDirectoryPath + "shop/shopping_bag_list_kwave.php";
    public static String product_add_qtyURL = androidDirectoryPath + "shop/product_add_qty.php";
    public static String product_deduct_qtyURL = androidDirectoryPath + "shop/product_deduct_qty.php";
    public static String dismissitemURL = androidDirectoryPath + "shop/remove_from_bag.php";
    //public static String getcheckoutinfoURL = androidDirectoryPath + "shop/check_out_info3.php";
    //public static String getcheckoutinfoURL = androidDirectoryPath + "shop/check_out_info4.php";
    public static String getcheckoutinfoURL = androidDirectoryPath + "shop/check_out_info5.php";
    public static String getcheckoutinfoKwaveURL = androidDirectoryPath + "shop/check_out_info_kwave.php";
    public static String check_outURL = androidDirectoryPath + "shop/check_out.php";
    public static String check_out2URL = androidDirectoryPath + "shop/check_out2.php";
    public static String validateCheckOutURL = androidDirectoryPath + "shop/validateCheckOut.php";
    public static String validateCheckOutKwaveURL = androidDirectoryPath + "shop/validateCheckOutKwave.php";

    public static String searchQueryURL = androidDirectoryPath + "shop/search_product.php";

    public static String product_update_shipping_methodURL = androidDirectoryPath + "shop/update_shipping_method.php";

    public static String checkAddrStatusURL = androidDirectoryPath + "check_address_status.php";
    public static String requestanitemURL = androidDirectoryPath + "shop/request_item.php";
    //public static String purchase_historyURL = androidDirectoryPath + "shop/purchase_history_new.php";
    public static String purchase_historyURL = androidDirectoryPath + "shop/purchase_history_5050.php";
    public static String purchasedetailURL = androidDirectoryPath + "shop/purchase_detail.php";
    public static String changeLanguageURL = androidDirectoryPath + "change_language.php";
    public static String getLanguageURL = androidDirectoryPath + "getLanguage.php";
    public static String requestListURL = androidDirectoryPath + "shop/request_item_list.php";
    public static String userInfoURL = androidDirectoryPath + "shop/check_out_info.php";
    public static String edit_userURL = androidDirectoryPath + "user_edit.php";
    public static String updateInfosURL = androidDirectoryPath + "update_infos.php";
    public static String appVersionURL = androidDirectoryPath + "appVersion.php";
    public static String appMaintenanceURL = androidDirectoryPath + "appsMaintenance.php";
    public static String shippingStateURL = androidDirectoryPath + "shop/shipping_state.php";
    public static String getCityURL = androidDirectoryPath + "shop/shipping_city.php";
    public static String getPostcodeURL = androidDirectoryPath + "shop/shipping_postcode.php";
    public static String getPostcodeListURL = androidDirectoryPath + "shop/shipping_postcode.php";

    public static String getHappeningTitlesURL = androidDirectoryPath + "shop/happening_titles.php";
    public static String getHappeningListURL = androidDirectoryPath + "shop/happening.php";
    public static String getHappeningDetailsURL = androidDirectoryPath + "shop/happening_details.php";

    public static String getEventDetailsURL = androidDirectoryPath + "shop/event_details.php";
    public static String getEventLoadURL = androidDirectoryPath + "shop/event_load.php";
    public static String eventaddtobagURL = androidDirectoryPath + "shop/event_add_to_bag.php";

    public static String firstLoginURL = androidDirectoryPath + "first_login.php";
    public static String firstLoginV2URL = androidDirectoryPath + "first_login_v2.php";
    public static String updateProfileURL = androidDirectoryPath + "update_profile.php";
    public static String getAddressURL = androidDirectoryPath + "get_address.php";

    public static String updateShippingInfoURL = androidDirectoryPath + "update_ship_info.php";
    public static String updateBillingInfoURL = androidDirectoryPath + "update_bill_info.php";

    public static String updateBillInfoURL = androidDirectoryPath + "update_bill_info.php";
    public static String updateShipInfoURL = androidDirectoryPath + "update_ship_info.php";
    public static String purchase_history_item_detailsURL = androidDirectoryPath + "shop/purchase_item_history.php";
    public static String updateSBShippingURL = androidDirectoryPath + "shop/updateSBShipping.php";

    public static String getVoucherListURL = androidDirectoryPath + "shop/voucher_list.php";
    public static String getVoucherListKwaveURL = androidDirectoryPath + "shop/voucher_list_kwave.php";
    public static String add_voucherURL = androidDirectoryPath + "shop/add_voucher.php";
    public static String add_voucherKwaveURL = androidDirectoryPath + "shop/add_voucher_kwave.php";
    public static String remove_voucherURL = androidDirectoryPath + "shop/remove_voucher.php";

    public static String setdevicetokenURL = androidDirectoryPath + "shop/setDeviceToken.php";

    public static String setShipBillDefaultURL = androidDirectoryPath + "setShipBillDefault.php";
    public static String SendEmailVerifyAddrURL = androidDirectoryPath + "SendEmailVerifyAddress.php";
    public static String updateAddAddressURL = androidDirectoryPath + "updateAddAddress.php";
    public static String getAddress_newURL = androidDirectoryPath + "getAddress.php";
    public static String verifyaddressURL = androidDirectoryPath + "verifyAddress.php";
    public static String deleteaddressURL = androidDirectoryPath + "deleteAddress.php";

    public static String getSBAddressURL = androidDirectoryPath + "getSBAddress.php";
    public static String selectSBaddressURL = androidDirectoryPath + "selectSBAddress.php";

    public static String PaymentProcessURL = androidDirectoryPath + "shop/PaymentProcessV2.php";
//    public static String PaymentProcessURL = androidDirectoryPath + "shop/PaymentProcessYC.php";

    public static String getUserLoginInfoURL = androidDirectoryPath + "getUserLoginDataV2.php";
    public static String checkAccountValidURL = androidDirectoryPath + "checkAccountValid.php";

    public static String userRegisterURL = androidDirectoryPath + "user_register.php";

    // public static String URL_FCM_CREATE = "https://dev.roboticdns3.com/mbooster/fcm_create.php";

    public static String URL_FCM_CREATE = "https://mbooster2u.com/mobile/login_new_android.php";

    public static String postConvertVoucher = androidDirectoryPath + "shop/convert_evoucher.php";
    public static String postVoucherSelection = androidDirectoryPath + "shop/voucher_entry_save.php";
    public static String getVoucherSelection = androidDirectoryPath + "shop/voucher_entry_detail.php";
    public static String getAllRewardsUrl = androidDirectoryPath + "shop/get_all_reward_points.php";
    public static String getAllEvoucherURL = androidDirectoryPath + "shop/get_all_evoucher.php";
    public static String updateEvoucherURL = androidDirectoryPath + "shop/add_evoucher.php";
    public static String getProductEVoucherListURL = androidDirectoryPath + "shop/product_evoucher_list.php";
    public static String product_remove_EvoucherURL = androidDirectoryPath + "shop/product_remove_evoucher.php";
    public static String userEvoucherListURL = androidDirectoryPath + "shop/user_evoucher_list.php";


    /**
     * Generic function
     */
    private static String getBaseURL(){
        if(api_mode == 0){ //Live
            return "https://www.mbooster.my/appV2/";
        }else if(api_mode == 1){ //Testing
            return  "https://www.mbooster.my/appV2/";
        }else{ //Testing
            return  "https://www.mbooster.my/appV2/";
        }
    }

    public static String getBasicUserData(Context c) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(SavePreferences.getUserID(c), "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(SavePreferences.getApplanguage(c), "UTF-8");
//            data += "&" + URLEncoder.encode("country_code", "UTF-8") + "=" + URLEncoder.encode(SavePreferences.getCountryCode(c), "UTF-8");

        } catch (UnsupportedEncodingException e) {

        }
        return data;
    }

    public static HashMap<String, String> getBasicUserParams(Context c) {
        HashMap<String, String> map = new HashMap<>();
        try {
            map.put("user_id", SavePreferences.getUserID(c));
            map.put("lang", SavePreferences.getApplanguage(c));
        } catch (Exception e) {

        }
        return map;
    }

    /**
     * Constructor
     */
    public urlLink() {
        jsonParser = new JSONParser();
        jsonParser2 = new JSONParser2();
    }


    //********************************************************************************************************************************************************//
    //data encoder function part
    //********************************************************************************************************************************************************//

    /*
    *
    * user login
    *
    * */
    public String submitLogin(String email, String password, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String resultback = data.toString();
        return resultback;
    }

    /*
    *
    * forget password
    *
    * */
    public JSONObject forgetPassword(String email, String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("lang", lang);

        return jsonParser2.makeHttpRequest(forgetPasswordURL, "POST", params);
    }


    /*
    *
    * get user data
    *
    * */
    public String submitgetUserData(Context c) {
        String data = getBasicUserData(c);
        return data;
//        try {
//            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String resultback = data.toString();
//        return resultback;
    }

    /*
    *
    * change password
    *
    * */
    public JSONObject changePassword(String user_id, String password, String newpassword ,String lang) {

        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", user_id);
        params.put("password", password);
        params.put("newpassword", newpassword);
        params.put("lang", lang);

        return jsonParser2.makeHttpRequest(changepasswordURL, "POST", params);
    }

    /*
   *
   * submit user register
   *
   * */
    public String submituserprofilepic(String user_avatar, String userid ,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("user_avatar", "UTF-8") + "=" + URLEncoder.encode(user_avatar, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(user_avatar, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String resultback = data.toString();
        return resultback;
    }


    /*
    *
    * submit user register
    *
    * */
    public JSONObject getcategory(String userid, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("user_id cat", userid);
        Log.i("lang cat", lang);
        JSONObject json = jsonParser.getJSONFromUrl(category, data);
        Log.i("category return", json.toString());
        return json;
    }

    /*
    *
    * submit user register
    *
    * */
    public JSONObject getsubcategory(String userid, String category_id, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");
            data += "&" + URLEncoder.encode("category_id", "UTF-8") + "=" + URLEncoder.encode(category_id, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(subcategory, data);
        // Log.i("json",json.toString());
        return json;
    }

    public JSONObject getProductList(String userid, String category_id, int index) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("category_id", "UTF-8") + "=" + URLEncoder.encode(category_id, "UTF-8");
            data += "&" + URLEncoder.encode("index", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(index), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getproductlist, data);
//         Log.i("product_list",json.toString());
        int maxLogSize = 1000;
        for(int i = 0; i <= json.toString().length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > json.toString().length() ? json.toString().length() : end;
            Log.i("Product Search", json.toString().substring(start, end));
        }
        return json;
    }

    public JSONObject getProductRedemption(String userid, String lang, String category_id ) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");
            data += "&" + URLEncoder.encode("category_id", "UTF-8") + "=" + URLEncoder.encode(category_id, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getRedemptionProduct, data);
//         Log.i("product_list",json.toString());
        int maxLogSize = 1000;
        for(int i = 0; i <= json.toString().length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > json.toString().length() ? json.toString().length() : end;
            Log.i("Product Search", json.toString().substring(start, end));
        }
        return json;
    }

    public JSONObject getSpinnerProduct(String userid,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getSpinnerRedemption, data);
        int maxLogSize = 1000;
        for(int i = 0; i <= json.toString().length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > json.toString().length() ? json.toString().length() : end;
            Log.i("Product Search", json.toString().substring(start, end));
        }
        return json;
    }

    public JSONObject getProductDetails(String userid, String product_id, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("product_id", "UTF-8") + "=" + URLEncoder.encode(product_id, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getproductDetails, data);
        // Log.i("json",json.toString());
        return json;
    }

    public JSONObject checkoutRedemption(String userid, String address_id, String product_id, String color, String category, String redeemcode,String action) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("address_id", "UTF-8") + "=" + URLEncoder.encode(address_id, "UTF-8");
            data += "&" + URLEncoder.encode("product_id", "UTF-8") + "=" + URLEncoder.encode(product_id, "UTF-8");
            data += "&" + URLEncoder.encode("color", "UTF-8") + "=" + URLEncoder.encode(color, "UTF-8");
            data += "&" + URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8");
            data += "&" + URLEncoder.encode("redeemcode", "UTF-8") + "=" + URLEncoder.encode(redeemcode, "UTF-8");
            data += "&" + URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getRedemptionCheckout, data);
        // Log.i("json",json.toString());
        return json;
    }

    public JSONObject wishitem(String userid, String product_id) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("product_id", "UTF-8") + "=" + URLEncoder.encode(product_id, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(wishitem, data);
//        Log.i("json",json.toString());
        return json;
    }

    public JSONObject addtobag(String userid, String product_id, String product_qty, String product_color, String redeem_by, String lang,String topupMcallsno) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");
            data += "&" + URLEncoder.encode("product_id", "UTF-8") + "=" + URLEncoder.encode(product_id, "UTF-8");
            data += "&" + URLEncoder.encode("product_qty", "UTF-8") + "=" + URLEncoder.encode(product_qty, "UTF-8");
            data += "&" + URLEncoder.encode("product_color", "UTF-8") + "=" + URLEncoder.encode(product_color, "UTF-8");
            data += "&" + URLEncoder.encode("redeem_by", "UTF-8") + "=" + URLEncoder.encode(redeem_by, "UTF-8");
            data += "&" + URLEncoder.encode("topupMcallsno", "UTF-8") + "=" + URLEncoder.encode(topupMcallsno, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(addtobagURL, data);
        Log.i("json", json.toString());
        return json;
    }

    public JSONObject addtobagRedeem(String userid, String product_id) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("product_id", "UTF-8") + "=" + URLEncoder.encode(product_id, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(addtobagURLRedemption, data);
        Log.i("json", json.toString());
        return json;
    }

    //eventaddtobag
    public JSONObject eventaddtobag(String userid, String product_id, String product_qty, String product_color) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("product_id", "UTF-8") + "=" + URLEncoder.encode(product_id, "UTF-8");
            data += "&" + URLEncoder.encode("product_qty", "UTF-8") + "=" + URLEncoder.encode(product_qty, "UTF-8");
            data += "&" + URLEncoder.encode("product_color", "UTF-8") + "=" + URLEncoder.encode(product_color, "UTF-8");
            Log.i("id", userid);
            Log.i("product_id", product_id);
            Log.i("product_qty", product_qty);
            Log.i("product_color", product_color);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(eventaddtobagURL, data);
        Log.i("json", json.toString());
        return json;
    }

    public JSONObject shopping_bag_list(Context c) {
        String data = getBasicUserData(c);// "";
//        try {
//            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        Log.i("shopping_bag_list url1", shopping_bag_listURL);
        JSONObject json = jsonParser.getJSONFromUrl(shopping_bag_listURL, data);
        Log.i("shopping_bag_list", json.toString());
        return json;
    }
    public JSONObject shopping_bag_list_kw(String userid) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(shopping_bag_list_kwURL, data);
        Log.i("shopping_bag_list_kw", json.toString());
        return json;
    }

    public JSONObject update_shipping_method(String user_id, String cart_id, String isSelfCollect, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("cart_id", "UTF-8") + "=" + URLEncoder.encode(cart_id, "UTF-8");
            data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");
            data += "&" + URLEncoder.encode("is_selfCollect", "UTF-8") + "=" + URLEncoder.encode(isSelfCollect, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("data333",data);
        JSONObject json = jsonParser.getJSONFromUrl(product_update_shipping_methodURL, data);
        Log.i("json333",json.toString());
        return json;
    }

    public JSONObject product_deduct_qty(String user_id, String cart_id,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("cart_id", "UTF-8") + "=" + URLEncoder.encode(cart_id, "UTF-8");
            data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(product_deduct_qtyURL, data);
//        Log.i("json",json.toString());
        return json;
    }

    public JSONObject product_add_qty(String user_id, String cart_id,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("cart_id", "UTF-8") + "=" + URLEncoder.encode(cart_id, "UTF-8");
            data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(product_add_qtyURL, data);
//        Log.i("json",json.toString());
        return json;
    }

    public JSONObject dismissitem(String user_id, String cart_id ,String lang) {
        String data = "";
        try {

            data = URLEncoder.encode("cart_id", "UTF-8") + "=" + URLEncoder.encode(cart_id, "UTF-8");
            data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(dismissitemURL, data);
        Log.i("json", json.toString());
        return json;
    }

    public JSONObject getcheckoutinfo(String user_id,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("device", "UTF-8") + "=" + URLEncoder.encode("Android", "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        JSONObject json = jsonParser.getJSONFromUrl(getcheckoutinfoURL, data);
        Log.i("getcheckoutinfo json", json.toString());
        return json;
    }

    public JSONObject getcheckoutinfoKwave(String user_id,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("device", "UTF-8") + "=" + URLEncoder.encode("Android", "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        JSONObject json = jsonParser.getJSONFromUrl(getcheckoutinfoKwaveURL, data);
        Log.i("json", json.toString());
        return json;
    }

    public JSONObject purchasedetail(String user_id, String pi_id) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("pi_id", "UTF-8") + "=" + URLEncoder.encode(pi_id, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(purchasedetailURL, data);
        Log.i("purchase details", json.toString());
        return json;
    }


    public JSONObject check_out(String user_id, String pw, String totalamount, String name, String email, String address1,
                                String address2, String city, String state, String postcode, String country, String contact) {

        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pw, "UTF-8");
            data += "&" + URLEncoder.encode("totalamountpoint", "UTF-8") + "=" + URLEncoder.encode(totalamount, "UTF-8");
            data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("address1", "UTF-8") + "=" + URLEncoder.encode(address1, "UTF-8");
            data += "&" + URLEncoder.encode("address2", "UTF-8") + "=" + URLEncoder.encode(address2, "UTF-8");
            data += "&" + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8");
            data += "&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(state, "UTF-8");
            data += "&" + URLEncoder.encode("postcode", "UTF-8") + "=" + URLEncoder.encode(postcode, "UTF-8");
            data += "&" + URLEncoder.encode("country", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8");
            data += "&" + URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(contact, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(check_outURL, data);
//        Log.i("json",json.toString());
        return json;
    }

    public JSONObject check_out2(String user_id, String pw, String totalamount, String name, String email, String address1,
                                 String address2, String city, String state, String postcode, String country, String contact, String temp_tid) {

        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pw, "UTF-8");
            data += "&" + URLEncoder.encode("totalamountpoint", "UTF-8") + "=" + URLEncoder.encode(totalamount, "UTF-8");
            data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("address1", "UTF-8") + "=" + URLEncoder.encode(address1, "UTF-8");
            data += "&" + URLEncoder.encode("address2", "UTF-8") + "=" + URLEncoder.encode(address2, "UTF-8");
            data += "&" + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8");
            data += "&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(state, "UTF-8");
            data += "&" + URLEncoder.encode("postcode", "UTF-8") + "=" + URLEncoder.encode(postcode, "UTF-8");
            data += "&" + URLEncoder.encode("country", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8");
            data += "&" + URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(contact, "UTF-8");
            data += "&" + URLEncoder.encode("temp_tid", "UTF-8") + "=" + URLEncoder.encode(temp_tid, "UTF-8");

            Log.i("temp_tid", temp_tid);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(check_out2URL, data);
//        Log.i("json",json.toString());
        return json;
    }

    public JSONObject searchQuery(String user_id, String keyword, int index) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(keyword, "UTF-8");
            data += "&" + URLEncoder.encode("index", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(index), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(searchQueryURL, data);
        //Log.i("product search",json.toString());

        int maxLogSize = 1000;
        for(int i = 0; i <= json.toString().length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > json.toString().length() ? json.toString().length() : end;
            Log.v("Product Search", json.toString().substring(start, end));
        }
        return json;
    }

    public JSONObject requestanitem(String user_id, String item_name, String item_desc, String item_point, String base64,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("item_name", "UTF-8") + "=" + URLEncoder.encode(item_name, "UTF-8");
            data += "&" + URLEncoder.encode("item_desc", "UTF-8") + "=" + URLEncoder.encode(item_desc, "UTF-8");
            data += "&" + URLEncoder.encode("item_point", "UTF-8") + "=" + URLEncoder.encode(item_point, "UTF-8");
            data += "&" + URLEncoder.encode("base64Photo", "UTF-8") + "=" + URLEncoder.encode(base64, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(requestanitemURL, data);
//        Log.i("json",json.toString());
        return json;
    }

    //checkAddrStatusURL
    public JSONObject checkAddrStatusURL(String user_id,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(checkAddrStatusURL, data);
//        Log.i("json",json.toString());
        return json;
    }


//    public JSONObject purchase_history(String userid,int page){
//        String data = "";
//        try {
//            data = URLEncoder.encode("user_id","UTF-8") + "=" + URLEncoder.encode(userid,"UTF-8");
//            data +="&"+ URLEncoder.encode("index","UTF-8") + "=" + URLEncoder.encode(Integer.toString(page),"UTF-8");
//
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        JSONObject json = jsonParser.getJSONFromUrl(purchase_historyURL, data);
////        Log.i("purchase history",json.toString());
//        return json;
//    }


    //PurchaseHistory
    public JSONObject purchase_history(String user_id, String index) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("index", index);


        JSONObject json = jsonParser2.makeHttpRequest(purchase_historyURL, "GET", params);
//        Log.i("appVersion",json.toString());
        return json;
    }

    //getLanguageURL
    public JSONObject getLanguage(String userid) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            //data +="&"+ URLEncoder.encode("index","UTF-8") + "=" + URLEncoder.encode(Integer.toString(page),"UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getLanguageURL, data);
//        Log.i("json",json.toString());
        return json;
    }

    //changeLanguageURL
    public JSONObject changeLanguage(String userid, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("language", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(changeLanguageURL, data);
//        Log.i("json",json.toString());
        return json;
    }


    //requestanitemHistory
    public JSONObject requestanitemHistory(String userid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", userid);


        JSONObject json = jsonParser2.makeHttpRequest(requestListURL, "GET", params);
//        Log.i("reqAnItemH",json.toString());
        return json;
    }

    //check user info //userInfoURL
    public JSONObject userInfo(String userid) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            //data +="&"+ URLEncoder.encode("index","UTF-8") + "=" + URLEncoder.encode(Integer.toString(page),"UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(userInfoURL, data);
//        Log.i("userInfo",json.toString());
        return json;
    }

    //updateUserInfo
    public JSONObject updateUserInfo(String userid, String nameText, String phone, String email, String addr1, String addr2,
                                     String city, String postcode, String state, String country,String lang) {

        //final String contact,final String email,final String addr1,final String addr2,
//                final String city,final String postcode,final String state,final String country
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("emailText", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("mobileText", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("addressText", "UTF-8") + "=" + URLEncoder.encode(addr1, "UTF-8");
            data += "&" + URLEncoder.encode("address2Text", "UTF-8") + "=" + URLEncoder.encode(addr2, "UTF-8");
            data += "&" + URLEncoder.encode("postcodeText", "UTF-8") + "=" + URLEncoder.encode(postcode, "UTF-8");
            data += "&" + URLEncoder.encode("cityText", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8");
            data += "&" + URLEncoder.encode("stateText", "UTF-8") + "=" + URLEncoder.encode(state, "UTF-8");
            data += "&" + URLEncoder.encode("countryText", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(edit_userURL, data);
//        Log.i("userInfo",json.toString());
        return json;
    }

    public JSONObject updateInfos(String userid, String nameText, String phone, String email, String addr1, String addr2,
                                  String city, String postcode, String state, String country) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("nameText", "UTF-8") + "=" + URLEncoder.encode(nameText, "UTF-8");
            data += "&" + URLEncoder.encode("emailText", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("mobileText", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("addressText", "UTF-8") + "=" + URLEncoder.encode(addr1, "UTF-8");
            data += "&" + URLEncoder.encode("address2Text", "UTF-8") + "=" + URLEncoder.encode(addr2, "UTF-8");
            data += "&" + URLEncoder.encode("postcodeText", "UTF-8") + "=" + URLEncoder.encode(postcode, "UTF-8");
            data += "&" + URLEncoder.encode("cityText", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8");
            data += "&" + URLEncoder.encode("stateText", "UTF-8") + "=" + URLEncoder.encode(state, "UTF-8");
            data += "&" + URLEncoder.encode("countryText", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(updateInfosURL, data);
//        Log.i("userInfo",json.toString());
        return json;
    }

    //check user info //userInfoURL
    public JSONObject leftMenu(String userid, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(URLleftMenu, data);
        Log.i("leftmenu", json.toString());
        return json;
    }

    //Post Voucher Selection
    public JSONObject postVoucherSelection(HashMap<String , String> params){
        JSONObject json = jsonParser2.makeHttpRequest(postVoucherSelection, "POST", params);
        return json;
    }

    //Get User Reward
    public JSONObject getVoucherSelection(String user_id, String cart_id, String product_id) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("cart_id", "UTF-8") + "=" + URLEncoder.encode(cart_id, "UTF-8");
            data += "&" + URLEncoder.encode("product_id", "UTF-8") + "=" + URLEncoder.encode(product_id, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getVoucherSelection, data);
        Log.i("leftmenu", json.toString());
        return json;
    }

    //Get User Reward
    public JSONObject getAllRewards(String userid, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getAllRewardsUrl, data);
        Log.i("leftmenu", json.toString());
        return json;
    }



    //requestanitemHistory
    public JSONObject appVersion() {

        HashMap<String, String> params = new HashMap<>();
        params.put("type", "0");  // ios == 1


        JSONObject json = jsonParser2.makeHttpRequest(appVersionURL, "GET", params);
//        Log.i("appVersion",json.toString());
        return json;
    }

    public JSONObject appMaintenance(String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("lang",lang);
        params.put("type", "1");


        JSONObject json = jsonParser2.makeHttpRequest(appMaintenanceURL, "GET", params);
//        Log.i("appVersion",json.toString());
        return json;
    }


    //shipping_state
    public JSONObject shipping_state() {

        HashMap<String, String> params = new HashMap<>();
        //params.put("type", "0");  // ios == 1


        JSONObject json = jsonParser2.makeHttpRequest(shippingStateURL, "GET", params);
        Log.i("appVersion", json.toString());
        return json;
    }


    //getCity
    public JSONObject getCity() {

        HashMap<String, String> params = new HashMap<>();
        //params.put("type", "0");  // ios == 1


        JSONObject json = jsonParser2.makeHttpRequest(getCityURL, "GET", params);
//        Log.i("appVersion",json.toString());
        return json;
    }

    //getPostcode
    public JSONObject getPostcode(String scid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("scid", scid);  // ios == 1


        JSONObject json = jsonParser2.makeHttpRequest(getPostcodeURL, "GET", params);
        Log.i("postcode", json.toString());
        return json;
    }

    public JSONObject first_login(String userid, String nameText, String emailText, String mobileText,
                                  String billAddressText, String billAddress2Text, String billStateText, String billCityText, String billPostcodeText, String billCountryText,
                                  String shipNameText, String shipContactText, String shipAddressText, String shipAddress2Text, String shipStateText, String shipCityText, String shipPostcodeText, String shipCountryText) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("nameText", "UTF-8") + "=" + URLEncoder.encode(nameText, "UTF-8");
            data += "&" + URLEncoder.encode("emailText", "UTF-8") + "=" + URLEncoder.encode(emailText, "UTF-8");
            data += "&" + URLEncoder.encode("mobileText", "UTF-8") + "=" + URLEncoder.encode(mobileText, "UTF-8");

            data += "&" + URLEncoder.encode("billAddressText", "UTF-8") + "=" + URLEncoder.encode(billAddressText, "UTF-8");
            data += "&" + URLEncoder.encode("billAddress2Text", "UTF-8") + "=" + URLEncoder.encode(billAddress2Text, "UTF-8");
            data += "&" + URLEncoder.encode("billStateText", "UTF-8") + "=" + URLEncoder.encode(billStateText, "UTF-8");
            data += "&" + URLEncoder.encode("billCityText", "UTF-8") + "=" + URLEncoder.encode(billCityText, "UTF-8");
            data += "&" + URLEncoder.encode("billPostcodeText", "UTF-8") + "=" + URLEncoder.encode(billPostcodeText, "UTF-8");
            data += "&" + URLEncoder.encode("billCountryText", "UTF-8") + "=" + URLEncoder.encode(billCountryText, "UTF-8");

            data += "&" + URLEncoder.encode("shipNameText", "UTF-8") + "=" + URLEncoder.encode(shipNameText, "UTF-8");
            data += "&" + URLEncoder.encode("shipContactText", "UTF-8") + "=" + URLEncoder.encode(shipContactText, "UTF-8");
            data += "&" + URLEncoder.encode("shipAddressText", "UTF-8") + "=" + URLEncoder.encode(shipAddressText, "UTF-8");
            data += "&" + URLEncoder.encode("shipAddress2Text", "UTF-8") + "=" + URLEncoder.encode(shipAddress2Text, "UTF-8");
            data += "&" + URLEncoder.encode("shipStateText", "UTF-8") + "=" + URLEncoder.encode(shipStateText, "UTF-8");
            data += "&" + URLEncoder.encode("shipCityText", "UTF-8") + "=" + URLEncoder.encode(shipCityText, "UTF-8");
            data += "&" + URLEncoder.encode("shipPostcodeText", "UTF-8") + "=" + URLEncoder.encode(shipPostcodeText, "UTF-8");
            data += "&" + URLEncoder.encode("shipCountryText", "UTF-8") + "=" + URLEncoder.encode(shipCountryText, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(firstLoginURL, data);
//        Log.i("userInfo",json.toString());
        return json;
    }

    //first login v2
    public JSONObject first_login_v2(String userid, String nameText, String emailText, String mobileText,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("nameText", "UTF-8") + "=" + URLEncoder.encode(nameText, "UTF-8");
            data += "&" + URLEncoder.encode("emailText", "UTF-8") + "=" + URLEncoder.encode(emailText, "UTF-8");
            data += "&" + URLEncoder.encode("mobileText", "UTF-8") + "=" + URLEncoder.encode(mobileText, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(firstLoginV2URL, data);
//        Log.i("userInfo",json.toString());
        return json;
    }

    //updateProfile
    public JSONObject updateProfile(String userid, String name, String email, String phnumber,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("nameText", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("emailText", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("mobileText", "UTF-8") + "=" + URLEncoder.encode(phnumber, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(updateProfileURL, data);
//        Log.i("userInfo",json.toString());
        return json;
    }

    //getAddress
    public JSONObject getAddress(String user_id) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);  // ios == 1


        JSONObject json = jsonParser2.makeHttpRequest(getAddressURL, "GET", params);
        //Log.i("postcode", json.toString());
        return json;
    }

    //Convert Credit
    public JSONObject postConvertCredit(HashMap<String, String> params) {

        LogHelper.debug("[postConvertCredit] params  = " + params.toString());
        JSONObject json = jsonParser2.makeHttpRequest(postConvertVoucher, "POST", params);

        //Log.i("postcode", json.toString());
        return json;
    }

    //updateShippingInfoURL
    public JSONObject updateShippingInfo(String userid, String nameText, String mobileText, String addressText, String address2Text, String postcodeText, String cityText,
                                         String stateText, String countryText, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("nameText", "UTF-8") + "=" + URLEncoder.encode(nameText, "UTF-8");
            data += "&" + URLEncoder.encode("mobileText", "UTF-8") + "=" + URLEncoder.encode(mobileText, "UTF-8");
            data += "&" + URLEncoder.encode("addressText", "UTF-8") + "=" + URLEncoder.encode(addressText, "UTF-8");
            data += "&" + URLEncoder.encode("address2Text", "UTF-8") + "=" + URLEncoder.encode(address2Text, "UTF-8");
            data += "&" + URLEncoder.encode("postcodeText", "UTF-8") + "=" + URLEncoder.encode(postcodeText, "UTF-8");
            data += "&" + URLEncoder.encode("cityText", "UTF-8") + "=" + URLEncoder.encode(cityText, "UTF-8");
            data += "&" + URLEncoder.encode("stateText", "UTF-8") + "=" + URLEncoder.encode(stateText, "UTF-8");
            data += "&" + URLEncoder.encode("countryText", "UTF-8") + "=" + URLEncoder.encode(countryText, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(updateShippingInfoURL, data);
//        Log.i("userInfo",json.toString());
        return json;
    }

    //updateShippingInfoURL
    public JSONObject updateBillingInfo(String userid, String addressText, String address2Text, String postcodeText, String cityText, String stateText, String countryText) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("addressText", "UTF-8") + "=" + URLEncoder.encode(addressText, "UTF-8");
            data += "&" + URLEncoder.encode("address2Text", "UTF-8") + "=" + URLEncoder.encode(address2Text, "UTF-8");
            data += "&" + URLEncoder.encode("postcodeText", "UTF-8") + "=" + URLEncoder.encode(postcodeText, "UTF-8");
            data += "&" + URLEncoder.encode("cityText", "UTF-8") + "=" + URLEncoder.encode(cityText, "UTF-8");
            data += "&" + URLEncoder.encode("stateText", "UTF-8") + "=" + URLEncoder.encode(stateText, "UTF-8");
            data += "&" + URLEncoder.encode("countryText", "UTF-8") + "=" + URLEncoder.encode(countryText, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(updateBillingInfoURL, data);
//        Log.i("userInfo",json.toString());
        return json;
    }


    //updatebillinfo
//    public JSONObject updateBillInfo(String userid,String countyText,String postcodeText,String addressText, String countryText){
//        String data = "";
//        try {
//            data = URLEncoder.encode("user_id","UTF-8") + "=" + URLEncoder.encode(userid,"UTF-8");
//            data +="&"+ URLEncoder.encode("countyText","UTF-8") + "=" + URLEncoder.encode(countyText,"UTF-8");
//            data +="&"+ URLEncoder.encode("postcodeText","UTF-8") + "=" + URLEncoder.encode(postcodeText,"UTF-8");
//            data +="&"+ URLEncoder.encode("addressText","UTF-8") + "=" + URLEncoder.encode(addressText,"UTF-8");
//            data +="&"+ URLEncoder.encode("countryText","UTF-8") + "=" + URLEncoder.encode(countryText,"UTF-8");
//
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        JSONObject json = jsonParser.getJSONFromUrl(updateBillInfoURL, data);
////        Log.i("userInfo",json.toString());
//        return json;
//    }
//
//    //updateshipinfo
//    public JSONObject updateShipInfo(String userid,String nameText,String mobileText,String countyText, String postcodeText,String addressText,String countryText){
//        String data = "";
//        try {
//            data = URLEncoder.encode("user_id","UTF-8") + "=" + URLEncoder.encode(userid,"UTF-8");
//            data +="&"+ URLEncoder.encode("nameText","UTF-8") + "=" + URLEncoder.encode(nameText,"UTF-8");
//            data +="&"+ URLEncoder.encode("mobileText","UTF-8") + "=" + URLEncoder.encode(mobileText,"UTF-8");
//            data +="&"+ URLEncoder.encode("countyText","UTF-8") + "=" + URLEncoder.encode(countyText,"UTF-8");
//            data +="&"+ URLEncoder.encode("postcodeText","UTF-8") + "=" + URLEncoder.encode(postcodeText,"UTF-8");
//            data +="&"+ URLEncoder.encode("addressText","UTF-8") + "=" + URLEncoder.encode(addressText,"UTF-8");
//            data +="&"+ URLEncoder.encode("countryText","UTF-8") + "=" + URLEncoder.encode(countryText,"UTF-8");
//
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        JSONObject json = jsonParser.getJSONFromUrl(updateShipInfoURL, data);
////        Log.i("userInfo",json.toString());
//        return json;
//    }

    //purchase_history_item_details
    public JSONObject purchase_history_item_details(String userid, String piid, String cart_id, String app_language) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("pi_id", "UTF-8") + "=" + URLEncoder.encode(piid, "UTF-8");
            data += "&" + URLEncoder.encode("cart_id", "UTF-8") + "=" + URLEncoder.encode(cart_id, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(app_language, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(purchase_history_item_detailsURL, data);
        Log.i("purchase item", json.toString());
        return json;
    }

    //updateSBShipping
    public JSONObject updateSBShipping(String userid, String nameText, String mobileText, String emailText, String cityText, String stateText, String postcodeText, String addressText, String address2Text, String countryText) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("nameText", "UTF-8") + "=" + URLEncoder.encode(nameText, "UTF-8");
            data += "&" + URLEncoder.encode("mobileText", "UTF-8") + "=" + URLEncoder.encode(mobileText, "UTF-8");
            data += "&" + URLEncoder.encode("emailText", "UTF-8") + "=" + URLEncoder.encode(emailText, "UTF-8");
            data += "&" + URLEncoder.encode("cityText", "UTF-8") + "=" + URLEncoder.encode(cityText, "UTF-8");
            data += "&" + URLEncoder.encode("stateText", "UTF-8") + "=" + URLEncoder.encode(stateText, "UTF-8");
            data += "&" + URLEncoder.encode("postcodeText", "UTF-8") + "=" + URLEncoder.encode(postcodeText, "UTF-8");
            data += "&" + URLEncoder.encode("addressText", "UTF-8") + "=" + URLEncoder.encode(addressText, "UTF-8");
            data += "&" + URLEncoder.encode("address2Text", "UTF-8") + "=" + URLEncoder.encode(address2Text, "UTF-8");
            data += "&" + URLEncoder.encode("countryText", "UTF-8") + "=" + URLEncoder.encode(countryText, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(updateSBShippingURL, data);
//        Log.i("userInfo",json.toString());
        return json;
    }

    //temp_postcode
    public JSONObject getPostcodeList() {

        HashMap<String, String> params = new HashMap<>();
//        params.put("user_id", user_id);  // ios == 1


        JSONObject json = jsonParser2.makeHttpRequest(getPostcodeListURL, "GET", params);
        Log.i("get address", json.toString());
        return json;
    }

    //voucherList
    public JSONObject getVoucherList(String user_id) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);  // ios == 1


        JSONObject json = jsonParser2.makeHttpRequest(getVoucherListURL, "GET", params);
        Log.i("get address", json.toString());
        return json;
    }

    //voucherListKwave
    public JSONObject getVoucherListKwave(String user_id) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);  // ios == 1


        JSONObject json = jsonParser2.makeHttpRequest(getVoucherListKwaveURL, "GET", params);
        Log.i("get address", json.toString());
        return json;
    }

    //add_voucher
    public JSONObject add_voucher(String user_id, String voucher,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("voucher", "UTF-8") + "=" + URLEncoder.encode(voucher, "UTF-8");
            data +="&"+ URLEncoder.encode("lang","UTF-8") + "=" + URLEncoder.encode(lang,"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(add_voucherURL, data);
        Log.i("purchase item", json.toString());
        return json;
    }
    //add_voucherKwavw
    public JSONObject add_voucherKwave(String user_id, String voucher,String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("voucher", "UTF-8") + "=" + URLEncoder.encode(voucher, "UTF-8");
            data +="&"+ URLEncoder.encode("lang","UTF-8") + "=" + URLEncoder.encode(lang,"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(add_voucherKwaveURL, data);
        Log.i("purchase item", json.toString());
        return json;
    }

    //remove_voucher
    public JSONObject remove_voucher(String user_id, String voucher_id) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("voucher_id", "UTF-8") + "=" + URLEncoder.encode(voucher_id, "UTF-8");
//            data +="&"+ URLEncoder.encode("cart_id","UTF-8") + "=" + URLEncoder.encode(cart_id,"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(remove_voucherURL, data);
        Log.i("purchase item", json.toString());
        return json;
    }

    public JSONObject product_full_desc(String user_id, String product_id) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("product_id", "UTF-8") + "=" + URLEncoder.encode(product_id, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getProductFullDesc, data);
        // Log.i("json",json.toString());
        return json;
    }

    //happening
    public JSONObject happening(String user_id, String id) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getHappeningListURL, data);
        Log.i("json", json.toString());
        return json;
    }

    //happening_details
    public JSONObject happening_details(String user_id, String id) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

            Log.i("id", id);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getHappeningDetailsURL + "?id=" + id, data);
        Log.i("happening details", json.toString());
        return json;
    }

    //eventDetails
    public JSONObject eventDetails(String user_id, String id, String category_id, String max, String min) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("event_id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("category_id", "UTF-8") + "=" + URLEncoder.encode(category_id, "UTF-8");
            data += "&" + URLEncoder.encode("max_price", "UTF-8") + "=" + URLEncoder.encode(max, "UTF-8");
            data += "&" + URLEncoder.encode("min_price", "UTF-8") + "=" + URLEncoder.encode(min, "UTF-8");

            Log.i("user_id", user_id);
            Log.i("event_id", id);
            Log.i("category_id", category_id);
            Log.i("max_price", max);
            Log.i("min_price", min);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getEventDetailsURL + "?id=" + id, data);
        Log.i("eventdetails", json.toString());
        return json;
    }

    //happeningtitlelist
    public JSONObject happeningtitlelist(String user_id, String lang) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(lang, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getHappeningTitlesURL, data);
        Log.i("json", json.toString());
        return json;
    }

    //event_load
    public JSONObject event_load(String user_id, String id) {
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("event_id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

            Log.i("id", id);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(getEventLoadURL, data);
        Log.i("eventload", json.toString());
        return json;
    }

    //setdevicetoken
    public JSONObject setdevicetoken(String user_id, String token, String uuid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("token", token);
        params.put("uuid", uuid);

        JSONObject json = jsonParser2.makeHttpRequest(setdevicetokenURL, "POST", params);
        Log.i("get address", json.toString());
        return json;
    }

    //setShipBillDefault
    public JSONObject setShipBillDefault(String user_id, String address_id, String shipbill,String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("address_id", address_id);
        params.put("shipbill", shipbill);
        params.put("lang", lang);

        JSONObject json = jsonParser2.makeHttpRequest(setShipBillDefaultURL, "POST", params);

        return json;
    }

    //SendEmailVerifyAddr
    public JSONObject SendEmailVerifyAddr(String user_id, String address_id,String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("address_id", address_id);
        params.put("lang", lang);

        JSONObject json = jsonParser2.makeHttpRequest(SendEmailVerifyAddrURL, "POST", params);

        return json;
    }

    //updateAddressURL
    public JSONObject updateAddAddress(String userid, String address_id, String nameText, String mobileText, String emailText, String icnoText, String cityText,
                                       String stateText, String postcodeText, String addressText, String countryText,String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", userid);
        params.put("address_id", address_id);
        params.put("nameText", nameText);
        params.put("mobileText", mobileText);
        params.put("emailText", emailText);
        params.put("icnoText", icnoText);
        params.put("cityText", cityText);
        params.put("stateText", stateText);
        params.put("postcodeText", postcodeText);
        params.put("addressText", addressText);
        params.put("address2Text", "");
        params.put("countryText", countryText);
        params.put("lang", lang);


        JSONObject json = jsonParser2.makeHttpRequest(updateAddAddressURL, "POST", params);

        return json;
    }

    //getAddress
    public JSONObject getAddressNew(String user_id, String aid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("address_id", aid);


        JSONObject json = jsonParser2.makeHttpRequest(getAddress_newURL, "GET", params);
        //Log.i("postcode", json.toString());
        return json;
    }

    // verifyaddress
    public JSONObject verifyaddress(String user_id, String address_id, String verify_code,String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("address_id", address_id);
        params.put("verify_code", verify_code);
        params.put("lang", lang);


        JSONObject json = jsonParser2.makeHttpRequest(verifyaddressURL, "POST", params);
        //Log.i("postcode", json.toString());
        return json;
    }

    //deleteaddressURL
    public JSONObject deleteAddress(String user_id, String aid ,String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("address_id", aid);
        params.put("lang", lang);


        JSONObject json = jsonParser2.makeHttpRequest(deleteaddressURL, "POST", params);
        //Log.i("postcode", json.toString());
        return json;
    }

    //selectSBAddress
    public JSONObject selectSBAddress(String user_id, String aid,String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("address_id", aid);
        params.put("lang", lang);

        JSONObject json = jsonParser2.makeHttpRequest(selectSBaddressURL, "POST", params);
        //Log.i("postcode", json.toString());
        return json;
    }

    //getSBAddress
    public JSONObject getSBAddress(String user_id) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);


        JSONObject json = jsonParser2.makeHttpRequest(getSBAddressURL, "POST", params);
        //Log.i("postcode", json.toString());
        return json;
    }

    //validateCheckOut
    public JSONObject validateCheckOut(String user_id,String lang) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("lang", lang);


        JSONObject json = jsonParser2.makeHttpRequest(validateCheckOutURL, "POST", params);
        Log.i("JSON", json.toString());
        return json;
    }

    //validateCheckOut
    public JSONObject validateCheckOutKwave(String user_id,String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("lang", lang);


        JSONObject json = jsonParser2.makeHttpRequest(validateCheckOutKwaveURL, "POST", params);
        //Log.i("postcode", json.toString());
        return json;
    }

    //PaymentProcessURL
    public JSONObject PaymentProcess(String user_id, String gatewayTID, String temp_tid, String payment_date) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("gatewayTID", gatewayTID);
        params.put("temp_tid", temp_tid);
        params.put("payment_date", payment_date);
        params.put("device", "Android");

        JSONObject json = jsonParser2.makeHttpRequest(PaymentProcessURL, "POST", params);
        //Log.i("postcode", json.toString());
        return json;
    }

    /*
     * login with email
     * */
    public JSONObject loginWithEmailOrMMspot(String username, String password) {

        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        return jsonParser2.makeHttpRequest(loginURL, "POST", params);
    }


    //GetUserLoginInfo
    public JSONObject getUserLoginInfo(String msisdn, String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("msisdn", msisdn);
        params.put("lang", lang);

        Log.i("msisdn", msisdn);
        Log.i("lang", lang);
        return jsonParser2.makeHttpRequest(getUserLoginInfoURL, "POST", params);
    }

    //checkAccountValidURL
    public JSONObject checkAccountValid(String user_id) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);

        return jsonParser2.makeHttpRequest(checkAccountValidURL, "POST", params);
    }

    //userRegister
    public JSONObject userRegister(String name, String email, String password, String birthday, String gender,String lang) {

        HashMap<String, String> params = new HashMap<>();
        params.put("fullname", name);
        params.put("email", email);
        params.put("password", password);
        params.put("birthday", birthday);
        params.put("gender", gender);
        params.put("lang", lang);
        return jsonParser2.makeHttpRequest(userRegisterURL, "POST", params);
    }

    //getEVoucherList
    public JSONObject getAllEvoucher(String user_id, String cart_id) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("cart_id", cart_id);

        Log.i("getAllEvoucherparams", cart_id);

        JSONObject json = jsonParser2.makeHttpRequest(getAllEvoucherURL, "POST", params);

        return json;
    }

    //updateEvoucherURL
    public JSONObject updateEvoucher(String userid, String voucher_code, String cart_id, String product_id) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", userid);
        params.put("voucher_code", voucher_code);
        params.put("cart_id", cart_id);
        params.put("product_id", product_id);


        JSONObject json = jsonParser2.makeHttpRequest(updateEvoucherURL, "POST", params);

        return json;
    }

    //productEvoucherList
    public JSONObject getProductEVoucherList(String user_id, String cart_id) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("cart_id", cart_id);

        JSONObject json = jsonParser2.makeHttpRequest(getProductEVoucherListURL, "POST", params);
        return json;
    }

    //product_remove_evoucher
    public JSONObject product_remove_Evoucher(String user_id, String voucher_id, String lang) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("voucher_id", voucher_id);
        params.put("lang", lang);

        JSONObject json = jsonParser2.makeHttpRequest(product_remove_EvoucherURL, "POST", params);

        return json;
    }

    //userEvoucherList
    public JSONObject userEvoucherList(Context c, String fromWhere) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SavePreferences.getUserID(c));
        params.put("lang", SavePreferences.getApplanguage(c));
        if(fromWhere!=null){
            params.put("fromWhere", fromWhere);
        }

        JSONObject json = jsonParser2.makeHttpRequest(userEvoucherListURL, "POST", params);

        return json;
    }


}
