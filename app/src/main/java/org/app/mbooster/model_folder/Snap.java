package org.app.mbooster.model_folder;

import java.util.List;

/**
 * Created by royfei on 03/10/2017.
 */

public class Snap {

    private int mGravity;
    private String mText;
    private List<ProductModel> mProductListItems;

    public Snap(int gravity, String text, List<ProductModel> productListItems) {
        mGravity = gravity;
        mText = text;
        mProductListItems = productListItems;
    }

    public String getText(){
        return mText;
    }

    public int getGravity(){
        return mGravity;
    }

    public List<ProductModel> getApps(){
        return mProductListItems;
    }

}
