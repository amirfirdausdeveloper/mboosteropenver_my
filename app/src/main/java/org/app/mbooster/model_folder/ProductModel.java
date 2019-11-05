package org.app.mbooster.model_folder;

/**
 * Created by royfei on 24/10/2017.
 */

public class ProductModel {
    String productid;
    String productname;
    String productpts;
    String productairtime;
    String productby;
    String productimg;
    String amountcost; //originalprice
    String voucher_status;
    String product_label;
    String discount_perc;
    String eventId;

    double pvValue = 0;
    int maxVoucherValue = 0;
    int quantity = 0;
    double maxMAValue = 0;
    double mpValue = 0;

    public String product_color;

    String bundle_voucher;

    public ProductModel(){

    }

    public ProductModel(String productid, String productname, String productpts, String productairtime, String productby, String productimg, String amountcost, String voucher_status, String product_label, String discount_perc) {
        this.productby = productby;
        this.productid = productid;
        this.productairtime = productairtime;
        this.productname = productname;
        this.productpts = productpts;
        this.productimg = productimg;
        this.amountcost = amountcost;
        this.voucher_status = voucher_status;
        this.product_label = product_label;
        this.discount_perc = discount_perc;
    }

    public ProductModel(String productid, String productname, String productpts, String productairtime, String productby, String productimg, String amountcost, String voucher_status, String product_label, String discount_perc, String product_color) {
        this.productby = productby;
        this.productid = productid;
        this.productairtime = productairtime;
        this.productname = productname;
        this.productpts = productpts;
        this.productimg = productimg;
        this.amountcost = amountcost; //originalprice
        this.voucher_status = voucher_status;
        this.product_label = product_label;
        this.discount_perc = discount_perc;
        this.product_color = product_color;
    }


    public String getProductairtime() {
        return productairtime;
    }

    public String getProductid() {
        return productid;
    }

    public String getProductby() {
        return productby;
    }

    public String getProductimg() {
        return productimg;
    }

    public String getProductname() {
        return productname;
    }

    public String getProductpts() {
        return productpts;
    }

    public void setProductairtime(String productairtime) {
        this.productairtime = productairtime;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public void setProductby(String productby) {
        this.productby = productby;
    }

    public void setProductimg(String productimg) {
        this.productimg = productimg;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public void setProductpts(String productpts) {
        this.productpts = productpts;
    }

    public String getAmountcost() {
        return amountcost;
    }

    public String getVoucher_status() {
        return voucher_status;
    }

    public String getProduct_label() {
        return product_label;
    }

    public String getDiscount_perc() {
        return discount_perc;
    }

    public void setVoucher_status(String voucher_status) {
        this.voucher_status = voucher_status;
    }

    public void setAmountcost(String amountcost) {
        this.amountcost = amountcost;
    }

    public void setProduct_label(String product_label) {
        this.product_label = product_label;
    }

    public void setDiscount_perc(String discount_perc) {
        this.discount_perc = discount_perc;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getBundle_voucher() {
        return bundle_voucher;
    }

    public void setBundle_voucher(String bundle_voucher) {
        this.bundle_voucher = bundle_voucher;
    }

    public int getMaxVoucherValue() {
        return maxVoucherValue;
    }

    public void setMaxVoucherValue(int maxVoucherValue) {
        this.maxVoucherValue = maxVoucherValue;
    }

    public double getMaxMAValue() {
        return maxMAValue;
    }

    public void setMaxMAValue(double maxMAValue) {
        this.maxMAValue = maxMAValue;
    }

    public double getPvValue() {
        return pvValue;
    }

    public void setPvValue(double pvValue) {
        this.pvValue = pvValue;
    }

    public double getMpValue() {
        return mpValue;
    }

    public void setMpValue(double mpValue) {
        this.mpValue = mpValue;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
