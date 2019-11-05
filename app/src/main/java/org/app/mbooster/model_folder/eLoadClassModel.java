package org.app.mbooster.model_folder;

/**
 * Created by User on 30/8/2016.
 */
public class eLoadClassModel {

    public String epid;
    public String ecart_id;
    public String pname;
    public String rmamount;
    public String pointamount;
    public String pimg1;
    public String qty;
    public String pimg2;
    public String grant;

    public eLoadClassModel(){
    }

    public eLoadClassModel(String epid, String pname, String rmamount, String pointamount, String pimg1, String pimg2) {
        this.epid = epid;
        this.pname = pname;
        this.rmamount = rmamount;
        this.pointamount = pointamount;
        this.pimg1 = pimg1;
        this.pimg2 = pimg2;
    }

    public eLoadClassModel(String ecart_id, String pname, String rmamount, String pointamount, String pimg1, String qty, String grant) {
        this.ecart_id = ecart_id;
        this.pname = pname;
        this.rmamount = rmamount;
        this.pointamount = pointamount;
        this.pimg1 = pimg1;
        this.qty = qty;
        this.grant = grant;
    }

    public String getEcart_id() {
        return ecart_id;
    }

    public void setEcart_id(String ecart_id) {
        this.ecart_id = ecart_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getGrant() {
        return grant;
    }

    public void setGrant(String grant) {
        this.grant = grant;
    }

    public String getEpid() {
        return epid;
    }

    public void setEpid(String epid) {
        this.epid = epid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getRmamount() {
        return rmamount;
    }

    public void setRmamount(String rmamount) {
        this.rmamount = rmamount;
    }

    public String getPointamount() {
        return pointamount;
    }

    public void setPointamount(String pointamount) {
        this.pointamount = pointamount;
    }

    public String getPimg1() {
        return pimg1;
    }

    public void setPimg1(String pimg1) {
        this.pimg1 = pimg1;
    }

    public String getPimg2() {
        return pimg2;
    }

    public void setPimg2(String pimg2) {
        this.pimg2 = pimg2;
    }
}
