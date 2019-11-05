package org.app.mbooster.model_folder;

/**
 * Created by Mobkini on 6/2/2018.
 */

public class Address {
    public String id, name, contact, email, icno, addr1, addr2, postcode, city, state, country,shipping,billing, status;

    public Address(String id, String name, String contact, String email, String icno, String addr1, String addr2, String postcode, String city, String state, String country,String shipping, String billing, String status) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.icno = icno;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.postcode = postcode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.shipping = shipping;
        this.billing = billing;
        this.status = status;
    }
}
