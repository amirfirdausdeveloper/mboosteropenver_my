package org.app.mbooster.model_folder;

/**
 * Created by royfei on 14/12/2017.
 */

public class Paymentmethod {

    public String id, title, img, payment_link, status;

    public Paymentmethod(String id, String title, String img, String payment_link, String status) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.payment_link = payment_link;
        this.status = status;
    }
}
