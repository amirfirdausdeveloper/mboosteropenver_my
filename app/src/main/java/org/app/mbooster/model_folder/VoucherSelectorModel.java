package org.app.mbooster.model_folder;

public class VoucherSelectorModel {

    String useEV10, useEV30, useEV50;
    String availableEV10, availableEV30, availableEV50;
    String voucherLimitType = "0";
    String maxVoucherValueDisplay;
    String maxVoucherQuantity = "0";

    int maxVoucherValue;

    public String getUseEV10() {
        return useEV10;
    }

    public void setUseEV10(String useEV10) {
        this.useEV10 = useEV10;
    }

    public String getUseEV30() {
        return useEV30;
    }

    public void setUseEV30(String useEV30) {
        this.useEV30 = useEV30;
    }

    public String getUseEV50() {
        return useEV50;
    }

    public void setUseEV50(String useEV50) {
        this.useEV50 = useEV50;
    }

    public String getAvailableEV10() {
        return availableEV10;
    }

    public void setAvailableEV10(String availableEV10) {
        this.availableEV10 = availableEV10;
    }

    public String getAvailableEV30() {
        return availableEV30;
    }

    public void setAvailableEV30(String availableEV30) {
        this.availableEV30 = availableEV30;
    }

    public String getAvailableEV50() {
        return availableEV50;
    }

    public void setAvailableEV50(String availableEV50) {
        this.availableEV50 = availableEV50;
    }

    public String getVoucherLimitType() {
        return voucherLimitType;
    }

    public void setVoucherLimitType(String voucherLimitType) {
        this.voucherLimitType = voucherLimitType;
    }

    public String getMaxVoucherValueDisplay() {
        return maxVoucherValueDisplay;
    }

    public void setMaxVoucherValueDisplay(String maxVoucherValueDisplay) {
        this.maxVoucherValueDisplay = maxVoucherValueDisplay;
    }

    public String getMaxVoucherQuantity() {
        return maxVoucherQuantity;
    }

    public void setMaxVoucherQuantity(String maxVoucherQuantity) {
        this.maxVoucherQuantity = maxVoucherQuantity;
    }

    public int getMaxVoucherValue() {
        return maxVoucherValue;
    }

    public void setMaxVoucherValue(int maxVoucherValue) {
        this.maxVoucherValue = maxVoucherValue;
    }

}
