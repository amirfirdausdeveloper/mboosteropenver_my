package org.app.mbooster.model_folder;

public class VoucherModel {
    String voucher_id, voucher_code, voucher_amount, statusDesc, usedDate, expiryDate;


    public void setVoucher_id(String voucher_id) {
        this.voucher_id = voucher_id;
    }

    public void setVoucher_code(String voucher_code) {
        this.voucher_code = voucher_code;
    }

    public void setVoucher_amount(String voucher_amount) {
        this.voucher_amount = voucher_amount;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setUsedDate(String usedDate) {
        this.usedDate = usedDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getVoucher_id() {
        return voucher_id;
    }

    public String getVoucher_code() {
        return voucher_code;
    }

    public String getVoucher_amount() {
        return voucher_amount;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public String getUsedDate() {
        return usedDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

}
