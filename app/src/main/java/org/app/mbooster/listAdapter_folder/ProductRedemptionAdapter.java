package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.Holder.ConstantHolder;
import org.app.mbooster.R;
import org.app.mbooster.Redemption_tabs.RedeemProductActivity;
import org.app.mbooster.model_folder.ProductModel;
import org.app.mbooster.shopping_mall.Product.Product2;

import java.util.List;
import java.util.Locale;

public class ProductRedemptionAdapter extends BaseAdapter {

    private Context context;
    private List<ProductModel> productItems;
    boolean bizUser;
    boolean mtiUser;

    private boolean showEvOption = false;
    private boolean showMaOption = false;

    DisplayImageOptions options;

    public ProductRedemptionAdapter(Context context
            , List<ProductModel> productItems
            , boolean _bizUser
            , boolean _mtiUser) {
        this.context = context;
        this.productItems = productItems;
        this.bizUser = _bizUser;
        this.mtiUser = _mtiUser;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_placeholder) // resource or drawable
                .showImageForEmptyUri(R.mipmap.icon_placeholder) // resource or drawable
                .showImageOnFail(R.mipmap.icon_placeholder) // resource or drawable
                .build();
    }

    public void setShowEvOption(boolean showEvOption) {
        this.showEvOption = showEvOption;
    }

    public void setShowMaOption(boolean showMaOption) {
        this.showMaOption = showMaOption;
    }

    public void setBizUser(boolean _bizUser){
        this.bizUser = _bizUser;
    }

    public void setMtiUserUser(boolean _mtiUser){
        this.mtiUser = _mtiUser;
    }

    @Override
    public int getCount() {
        return productItems.size();
    }

    @Override
    public ProductModel getItem(int position) {
        return this.productItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            convertView = View.inflate(context, R.layout.product_list_item_redemption, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.button_redeem = convertView.findViewById(R.id.button_redeem);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ProductModel model = productItems.get(position);
        holder.name.setText(Html.fromHtml(model.getProductname()));


        final ProgressBar progressbar = (ProgressBar) convertView.findViewById(R.id.progressbar);


        ImageLoader.getInstance().displayImage(model.getProductimg(), new ImageViewAware(holder.img, false), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressbar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressbar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressbar.setVisibility(View.INVISIBLE);

            }
        });



        final Typeface tvFont = Typeface.createFromAsset(context.getAssets(), "fonts/gotham_book.ttf");

        holder.name.setTypeface(tvFont);
        holder.button_redeem.setTypeface(tvFont);

        holder.button_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RedeemProductActivity.class);
                i.putExtra("productid", model.getProductid());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        return convertView;
    }

    class ViewHolder{

        ImageView img, img_soldout, iamgeview_new_item, label_voucher;
        RelativeLayout evoucher_rl, ma_voucher_rl,pv_point_rl;
        LinearLayout mppv_ll;
        TextView originalprice, price, maAmount, voucherAmount, pvAmount, mPvPAmount;
        TextView name, promom1, promom2;
        ImageView promom3;
        Button button_redeem;
    }
}
