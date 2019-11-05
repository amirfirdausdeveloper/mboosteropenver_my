package org.app.mbooster.model_folder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 25/8/2016.
 */
public class ExpandableClassModel {

    public static HashMap<String, List<String>> getData(int i){

        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        if (i == 0){
            //no t72 user
            ArrayList<String> MyAcc = new ArrayList<String>();
//        MyAcc.add("Overview");
//        MyAcc.add("Profile");
            //MyAcc.add("Change Password");
           // MyAcc.add("Logout");

            ArrayList<String> EChannel = new ArrayList<String>();
           // EChannel.add("eLoad Transaction");
            EChannel.add("Appsome History");
            EChannel.add("speakOut History");
            EChannel.add("Withdraw History");
            EChannel.add("eLoad Transaction");


            ArrayList<String> eLoad = new ArrayList<String>();
            eLoad.add("eLoad");
            eLoad.add("eLoad cart");
//            eLoad.add("eLoad Transaction");


            ArrayList<String> MFlexi = new ArrayList<String>();
            MFlexi.add("Boost M-Flexi");
            MFlexi.add("Withdraw");
            MFlexi.add("Boost M-Flexi History");
            MFlexi.add("Withdraw History");

            ArrayList<String> WithdrawHistory = new ArrayList<String>();
            ArrayList<String> WithdrawMflexi = new ArrayList<String>();
            ArrayList<String> BoostMflexi = new ArrayList<String>();
            ArrayList<String> BoostMflexihistory = new ArrayList<String>();
            ArrayList<String> Logout = new ArrayList<String>();
            ArrayList<String> eLoad_Transaction = new ArrayList<String>();
            ArrayList<String> ShoppingMall = new ArrayList<String>();


            expandableListDetail.put("Request An Item",MyAcc);
            expandableListDetail.put("Withdraw",WithdrawMflexi);
            expandableListDetail.put("Incentive History",eLoad_Transaction);
            expandableListDetail.put("Purchase History",ShoppingMall);
//            expandableListDetail.put("Logout",Logout);
        }

        else
        {


            List<String> MyAcc = new ArrayList<String>();
//        MyAcc.add("Overview");
//        MyAcc.add("Profile");
          //  MyAcc.add("Change Password");
          // MyAcc.add("Logout");

            List<String> EChannel = new ArrayList<String>();
            EChannel.add("Appsome History");
            EChannel.add("speakOut History");
            EChannel.add("T72 History");
            EChannel.add("Withdraw History");
            EChannel.add("eLoad Transaction");

            List<String> eLoad = new ArrayList<String>();
            eLoad.add("eLoad");
            eLoad.add("eLoad cart");

            List<String> MFlexi = new ArrayList<String>();
            MFlexi.add("Boost M-Flexi");
            MFlexi.add("Withdraw");
            MFlexi.add("Boost M-Flexi History");
            MFlexi.add("Withdraw History");

            ArrayList<String> WithdrawHistory = new ArrayList<String>();
            ArrayList<String> WithdrawMflexi = new ArrayList<String>();
            ArrayList<String> BoostMflexi = new ArrayList<String>();
            ArrayList<String> BoostMflexihistory = new ArrayList<String>();
            ArrayList<String> eLoad_Transaction = new ArrayList<String>();
            ArrayList<String> Logout = new ArrayList<String>();

            ArrayList<String> ShoppingMall = new ArrayList<String>();
           // ShoppingMall.add("Category");

            expandableListDetail.put("Request An Item",MyAcc);
            expandableListDetail.put("Withdraw",WithdrawMflexi);
            expandableListDetail.put("Incentive History",eLoad_Transaction);
            expandableListDetail.put("Purchase History",ShoppingMall);

        }

        return expandableListDetail;

    }


}
