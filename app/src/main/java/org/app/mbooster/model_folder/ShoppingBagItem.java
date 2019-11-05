package org.app.mbooster.model_folder;

public class ShoppingBagItem  {
    String cart_id, product_id, product_name, product_qty, product_img, amount_point, total_amount_point, supplier_name, shippingcost, voucher_status, is_self_collect, is_allowed_self_collect_item, totalma, totalmyr, totalvouchervalue, redeem_type;
    String isSelfCollect;

    public ShoppingBagItem() {

    }

    public ShoppingBagItem(String cart_id, String product_id, String product_name, String product_qty, String product_img, String amount_point, String total_amount_point, String supplier_name, String shippingcost, String voucher_status, String is_self_collect, String is_allowed_self_collect_item, String totalma, String totalmyr, String totalvouchervalue, String redeem_type) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_qty = product_qty;
        this.product_img = product_img;
        this.amount_point = amount_point;
        this.total_amount_point = total_amount_point;
        this.supplier_name = supplier_name;
        this.shippingcost = shippingcost;
        this.voucher_status = voucher_status;
        this.is_self_collect = is_self_collect;
        this.is_allowed_self_collect_item = is_allowed_self_collect_item;
        this.totalma = totalma;
        this.totalmyr = totalmyr;
        this.totalvouchervalue = totalvouchervalue;
        this.redeem_type = redeem_type;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getAmount_point() {
        return amount_point;
    }

    public void setAmount_point(String amount_point) {
        this.amount_point = amount_point;
    }

    public String getTotal_amount_point() {
        return total_amount_point;
    }

    public void setTotal_amount_point(String total_amount_point) {
        this.total_amount_point = total_amount_point;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getShippingcost() {
        return shippingcost;
    }

    public void setShippingcost(String shippingcost) {
        this.shippingcost = shippingcost;
    }

    public String getVoucher_status() {
        return voucher_status;
    }

    public void setVoucher_status(String voucher_status) {
        this.voucher_status = voucher_status;
    }

    public String getIs_self_collect() {
        return is_self_collect;
    }

    public void setIs_self_collect(String is_self_collect) {
        this.is_self_collect = is_self_collect;
    }

    public String getIs_allowed_self_collect_item() {
        return is_allowed_self_collect_item;
    }

    public void setIs_allowed_self_collect_item(String is_allowed_self_collect_item) {
        this.is_allowed_self_collect_item = is_allowed_self_collect_item;
    }

    public String getTotalma() {
        return totalma;
    }

    public void setTotalma(String totalma) {
        this.totalma = totalma;
    }

    public String getTotalmyr() {
        return totalmyr;
    }

    public void setTotalmyr(String totalmyr) {
        this.totalmyr = totalmyr;
    }

    public String getTotalvouchervalue() {
        return totalvouchervalue;
    }

    public void setTotalvouchervalue(String totalvouchervalue) {
        this.totalvouchervalue = totalvouchervalue;
    }

    public String getRedeem_type() {
        return redeem_type;
    }

    public void setRedeem_type(String redeem_type) {
        this.redeem_type = redeem_type;
    }

    public String getIsSelfCollect() {
        return isSelfCollect;
    }

    public void setIsSelfCollect(String isSelfCollect) {
        this.isSelfCollect = isSelfCollect;
    }
}
