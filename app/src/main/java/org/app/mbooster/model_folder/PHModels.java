package org.app.mbooster.model_folder;

import java.util.ArrayList;

/**
 * Created by royfei on 19/06/2017.
 */

public class PHModels {

    public static class PurchaseOrder {
        public String invoiceId, date,total_paid,voucher,total_amount,voucher_qualify,transactioniid,total_paid_mat,total_outstanding_amount,paymtIncompl,payment_link, total_voucher_value;
        public ArrayList<Vendor> vendors;

        public PurchaseOrder(String invoiceId, String date,String total_paid,String voucher,String total_amount,String voucher_qualify, String transactioniid, String total_paid_mat, String total_outstanding_amount, String paymtIncompl, String payment_link, String total_voucher_value, ArrayList<Vendor> vendors) {
            this.invoiceId = invoiceId;
            this.date = date;
            this.vendors = vendors;
            this.total_paid = total_paid;
            this.total_amount = total_amount;
            this.voucher = voucher;
            this.voucher_qualify = voucher_qualify;
            this.transactioniid = transactioniid;
            this.total_paid_mat = total_paid_mat;
            this.total_outstanding_amount = total_outstanding_amount;
            this.paymtIncompl = paymtIncompl;
            this.payment_link = payment_link;
            this.total_voucher_value = total_voucher_value;
        }
    }

    public static class Vendor {
        public String piid, vendorName, vendorTotalPrice;
        public ArrayList<PurchaseItem> items;

        public Vendor(String piid, String vendorName, String vendorTotalPrice, ArrayList<PurchaseItem> items) {
            this.piid = piid;
            this.vendorName = vendorName;
            this.vendorTotalPrice = vendorTotalPrice;
            this.items = items;
        }
    }

    public static class PurchaseItem {

        public String itemProductId, itemCartId, itemName, itemStatus, itemImg;

        public PurchaseItem(String itemProductId, String itemCartId, String itemName, String itemStatus, String itemImg) {
            this.itemCartId = itemCartId;
            this.itemName = itemName;
            this.itemProductId = itemProductId;
            this.itemStatus = itemStatus;
            this.itemImg = itemImg;
        }
    }
}
