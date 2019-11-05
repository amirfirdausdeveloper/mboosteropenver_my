package org.app.mbooster.Holder;

public class ConstantHolder {

    /**
     * Evoucher enum 10, 30, 50
     */
    public static enum VOUCHER_TYPE{
        EV10,
        EV30,
        EV50
    }

    /**
     * For Activity Request Code
     */
    public static final int REQUEST_CONVERT_MCP = 1003;

    public static final int HD_RESOLUTION_W = 1080;

    /**
     * For Product Details Select payment method
     */
    public static final int RADIO_PAY_CASH = 0;
    public static final int RADIO_PAY_EV = 1;
    public static final int RADIO_PAY_MMSPOT = 2;

    /**
     * For page display
     */
    public static final int DISPLAY_PAGE_REWARDPOINT = 1;
    public static final int DISPLAY_PAGE_MA = 2;

    public static final String CHANGE_LANGUAGE_TAG = "IDONTUNDERSTAND";


    public static final String IS_MAINTAINANCE = "1";

    /**
     * Double Decimal Pattern
     */
    public static final String PATTERN_THOUSAND = "#,###";
}
