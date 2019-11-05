package org.app.mbooster.model_folder;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by User on 26/8/2016.
 */
public class SavePreferences {

    private static String SharedPreferenceFileName = "UserLoginDetailMY";
    private static String UserEmail = "uemail";
    private static String UserPassword = "upwd";
    private static String UserProfilePicture = "upicture";
    private static String UserName = "uname";
    private static String UserID = "uid";
    private static String UserRole = "urole";
    private static String AppLanguage = "language";
    private static String CountryCode = "countryCode";
    private static String SelectTab = "SELECTTAB";
    private static String UUID = "uuid";
    private static String JSONCountdown = "jsoncountdown";
    private static String MMSPOTLOGINTOKEN = "mmspot_login_token";
    private static String UserType = "user_type"; // 1 = Public User, 0 = MMspot User
    private static String MMSPOTNAME = "MMSPOTNAME";
    private static String MMSPOTEMAIL = "MMSPOTEMAIL";
    private static String MMSPOTCONTACT = "MMSPOTCONTACT";
    private static String FirstTimeApp = "FirstTimeApp";
    private static String BizUser = "bizUser";
    private static String MPConvert = "MPConvert";

    private static String Current_m_flexi = "";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SharedPreferenceFileName, context.MODE_PRIVATE);
    }

    public static String getUserRole(Context context) {
        return getSharedPreferences(context).getString(UserRole, "default");
    }

    public static void setUserRole(Context context, String userRole) {
        getSharedPreferences(context).edit().putString(UserRole, userRole).commit();
    }

    public static void setApplanguage(Context context, String language) {
        getSharedPreferences(context).edit().putString(AppLanguage, language).commit();
    }

    public static String getApplanguage(Context context) {
        return getSharedPreferences(context).getString(AppLanguage, "ENG");
    }

    public static String getUserEmail(Context context) {
        return getSharedPreferences(context).getString(UserEmail, "default");
    }

    public static void setUserEmail(Context context, String userEmail) {
        getSharedPreferences(context).edit().putString(UserEmail, userEmail).commit();
    }

    public static String getUserPassword(Context context) {
        return getSharedPreferences(context).getString(UserPassword, "default");
    }

    public static void setUserPassword(Context context, String userPassword) {
        getSharedPreferences(context).edit().putString(UserPassword, userPassword).commit();
    }

    public static String getUserProfilePicture(Context context) {
        return getSharedPreferences(context).getString(UserProfilePicture, "default");
    }

    public static void setUserProfilePicture(Context context, String userProfilePicture) {
        getSharedPreferences(context).edit().putString(UserProfilePicture, userProfilePicture).commit();
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(UserName, "default");
    }

    public static void setUserName(Context context, String userName) {
        getSharedPreferences(context).edit().putString(UserName, userName).commit();
    }

    public static String getUserID(Context context) {
        return getSharedPreferences(context).getString(UserID, "0");
    }

    public static void setUserID(Context context, String userID) {
        getSharedPreferences(context).edit().putString(UserID, userID).commit();
    }

    public static void clear(Context context) {
        getSharedPreferences(context).edit().clear().commit();
    }

    public static void setCurrent_m_flexi(Context context, String current_m_flexi) {
        getSharedPreferences(context).edit().putString(Current_m_flexi, current_m_flexi).commit();
    }

    public static String getCurrent_m_flexi(Context context) {
        return getSharedPreferences(context).getString(Current_m_flexi, "default");
    }

    public static String getMainActivitySelectTab(Context context) {
        return getSharedPreferences(context).getString(SelectTab, "0");
    }


    public static String getBizUser(Context context) {
        return getSharedPreferences(context).getString(BizUser, "0");
    }

    public static void setMainActivitySelectTab(Context context, String selectTab) {
        getSharedPreferences(context).edit().putString(SelectTab, selectTab).commit();
    }

    public static String getJSONCoundown(Context context) {
        return getSharedPreferences(context).getString(JSONCountdown, "0");
    }

    public static void setJSONCoundown(Context context, String selectTab) {
        getSharedPreferences(context).edit().putString(JSONCountdown, selectTab).commit();
    }

    public static String getMMspotLoginToken(Context MMspotLoginToken) {
        return getSharedPreferences(MMspotLoginToken).getString(MMSPOTLOGINTOKEN, "");
    }

    public static void setMMspotLoginToken(Context context, String MMspotLoginToken) {
        getSharedPreferences(context).edit().putString(MMSPOTLOGINTOKEN, MMspotLoginToken).commit();
    }

    public static String getUUID(Context context) {
        return getSharedPreferences(context).getString(UUID, "default");
    }

    public static void setUUID(Context context, String uuid) {
        getSharedPreferences(context).edit().putString(UUID, uuid).commit();
    }

    public static String getFirstTimeApp(Context context) {
        return getSharedPreferences(context).getString(FirstTimeApp, "0");
    }

    public static void setFirstTimeApp(Context context, String firstTimeApp) {
        getSharedPreferences(context).edit().putString(FirstTimeApp, firstTimeApp).commit();
    }

    public static String getUserType(Context context) {
        return getSharedPreferences(context).getString(UserType, "1");
    }

    public static String getMMSPOTCONTACT(Context context) {
        return getSharedPreferences(context).getString(MMSPOTCONTACT, "0");
    }

    public static String getMPConvert(Context context) {
        return getSharedPreferences(context).getString(MPConvert, "0");
    }

    /**
     * @param context
     * @param userType //  0 = MMspot User, 1 = Public User
     */
    public static void setUserType(Context context, String userType) {
        getSharedPreferences(context).edit().putString(UserType, userType).commit();
    }

    public static String getMMSPOTNAME(Context context) {
        return getSharedPreferences(context).getString(MMSPOTNAME, "0");
    }

    public static void setMMSPOTNAME(Context context, String userType) {
        getSharedPreferences(context).edit().putString(MMSPOTNAME, userType).commit();
    }

    public static String getMMSPOTEMAIL(Context context) {
        return getSharedPreferences(context).getString(MMSPOTEMAIL, "0");
    }

    public static void setMMSPOTEMAIL(Context context, String userType) {
        getSharedPreferences(context).edit().putString(MMSPOTEMAIL, userType).commit();
    }


    public static void setMMSPOTCONTACT(Context context, String userType) {
        getSharedPreferences(context).edit().putString(MMSPOTCONTACT, userType).commit();
    }

    public static void setBizUser(Context context, String value) {
        getSharedPreferences(context).edit().putString(BizUser, value).commit();
    }

    public static void setMPConvert(Context context, String value) {
        getSharedPreferences(context).edit().putString(MPConvert, value).commit();
    }

    public static boolean isEmpty(Context context) {
        if (getUserEmail(context).equals("default"))
            return true;
        else
            return false;

    }

    public static String getCountryCode(Context context) {
        return getSharedPreferences(context).getString(CountryCode, "MY");
    }

    public static void setCountryCode(Context context, String code) {
        getSharedPreferences(context).edit().putString(CountryCode, code).commit();
    }
}