package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import org.app.mbooster.model_folder.ProductModel;

import java.util.List;
import java.util.Locale;

public class ProductListingAdapter extends BaseAdapter {

    private Context context;
    private List<ProductModel> productItems;
    boolean bizUser;
    boolean mtiUser;

    private boolean showEvOption = false;
    private boolean showMaOption = false;

    DisplayImageOptions options;

    public ProductListingAdapter(Context context
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
            convertView = View.inflate(context, R.layout.product_list_item_v2, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.img_soldout = convertView.findViewById(R.id.img_sold_out);
            holder.iamgeview_new_item = (ImageView) convertView.findViewById(R.id.iamgeview_new_item);
            holder.label_voucher = (ImageView) convertView.findViewById(R.id.label_voucher);
            holder.evoucher_rl = convertView.findViewById(R.id.procut_list_evoucher_container);
            holder.ma_voucher_rl = convertView.findViewById(R.id.procut_list_ma_container);
            holder.pv_point_rl = convertView.findViewById(R.id.procut_list_pv_container);
            holder.mppv_ll = convertView.findViewById(R.id.mp_pv_container);

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.originalprice = (TextView) convertView.findViewById(R.id.originaprice);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.maAmount = convertView.findViewById(R.id.ma_amount_tv);
            holder.voucherAmount = convertView.findViewById(R.id.voucher_amount_tv);
            holder.pvAmount = convertView.findViewById(R.id.pv_amount_tv);
            holder.mPvPAmount = convertView.findViewById(R.id.mp_pv_text);
            holder.promom1 = convertView.findViewById(R.id.promo_1);
            holder.promom2 = convertView.findViewById(R.id.promo_2);
            holder.promom3 = convertView.findViewById(R.id.promo_3);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ProductModel model = productItems.get(position);
        holder.name.setText(Html.fromHtml(model.getProductname()));
        holder.price.setText(context.getResources().getString(R.string.currency) + model.getProductpts());

        Locale current_locale = context.getResources().getConfiguration().locale;
        if (current_locale.toString().toLowerCase().contains("en")) {
            holder.label_voucher.setImageResource(R.drawable.label_mbooster_voucher);
        } else if (current_locale.toString().toLowerCase().contains("zh")) {
            holder.iamgeview_new_item.setImageResource(R.drawable.label_new_cn);
            holder.label_voucher.setImageResource(R.drawable.label_mbooster_voucher_cn);
        }

        final ProgressBar progressbar = (ProgressBar) convertView.findViewById(R.id.progressbar);

        int width = context.getResources().getDisplayMetrics().widthPixels / 2 - 50;
        android.view.ViewGroup.LayoutParams layoutParams = holder.img.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = (int) (((context.getResources().getDisplayMetrics().widthPixels) / 2) / 1.3);
        holder.img.setLayoutParams(layoutParams);
        holder.img_soldout.setLayoutParams(layoutParams);

        int width2 = context.getResources().getDisplayMetrics().widthPixels / 2;
        android.view.ViewGroup.LayoutParams layoutParams2 = holder.label_voucher.getLayoutParams();
        layoutParams2.width = width2 / 3;
        layoutParams2.height = width2 / 3;
        holder.label_voucher.setLayoutParams(layoutParams2);

        //EVoucher Layout
//        int evoucherWidth = (context.getResources().getDisplayMetrics().widthPixels / 2) - context.getResources().getDimensionPixelOffset(R.dimen.item_product_voucher_calculate_value);
//        android.view.ViewGroup.LayoutParams layoutParamsVoucher = evoucher_rl.getLayoutParams();
//        layoutParamsVoucher.width = evoucherWidth;
//        evoucher_rl.setLayoutParams(layoutParamsVoucher);
//        if (model.getVoucher_status().equals("1") && model.getMaxVoucherValue() > 0) {
        if(showEvOption) {
            holder.evoucher_rl.setVisibility(View.VISIBLE);
            holder.voucherAmount.setText(Helper.doubleDecimalToString(model.getMaxVoucherValue(), ConstantHolder.PATTERN_THOUSAND) + context.getString(R.string.postfix_ev));
        }else{
            holder.evoucher_rl.setVisibility(View.GONE);
        }
//        } else {
//            evoucher_rl.setVisibility(View.GONE);
//        }

        if (model.getMaxMAValue() > 0 && showMaOption) {
            holder.ma_voucher_rl.setVisibility(View.VISIBLE);
            holder.maAmount.setText(Helper.doubleDecimalToString(model.getMaxMAValue(), ConstantHolder.PATTERN_THOUSAND) + context.getString(R.string.mairtime));
        } else {
            holder.ma_voucher_rl.setVisibility(View.GONE);
        }

        if(model.getQuantity()>0 || model.getQuantity()==-1) {
            holder.img_soldout.setVisibility(View.GONE);
        }else{
            holder.img_soldout.setVisibility(View.VISIBLE);
        }

        if(bizUser || mtiUser) {
            StringBuilder builder = new StringBuilder();
            holder.mppv_ll.setVisibility(View.VISIBLE);
            if (bizUser || mtiUser) {
                builder.append(model.getMpValue() + context.getString(R.string.postfix_mp).toUpperCase());
            }
            if (mtiUser) {
                builder.append("/" + model.getPvValue() + context.getString(R.string.postfix_pv).toUpperCase());
            }
            holder.mPvPAmount.setText(builder.toString());
        }else{
            holder.mppv_ll.setVisibility(View.GONE);
        }

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

        if (model.getAmountcost().equals("0.00") || model.getAmountcost().equals("0")) {
            holder.originalprice.setVisibility(View.VISIBLE);
            holder.originalprice.setText("");
        } else {
            holder.originalprice.setVisibility(View.VISIBLE);
            holder.originalprice.setText(context.getResources().getString(R.string.currency) + model.getAmountcost());
        }

        holder.originalprice.setPaintFlags(holder.originalprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.label_voucher.setVisibility(View.GONE);
//        if (model.getVoucher_status().equals("1")) {
//            label_voucher.setVisibility(View.VISIBLE);
//        }

        if(model.getDiscount_perc()!=null && !model.getDiscount_perc().equals("0")) {
            StringBuilder dcBuilder = new StringBuilder();
            if(!model.getDiscount_perc().contains("-")) {
                dcBuilder.append("-");
            }
            dcBuilder.append(model.getDiscount_perc());
            dcBuilder.append("%");
            holder.promom1.setText(dcBuilder.toString());
            holder.promom1.setVisibility(View.VISIBLE);
        }else{
            holder.promom1.setVisibility(View.GONE);
        }


        final Typeface tvFont = Typeface.createFromAsset(context.getAssets(), "fonts/gotham_book.ttf");
        final Typeface tvFont2 = Typeface.createFromAsset(context.getAssets(), "fonts/gotham_book_bold.ttf");

        holder.mPvPAmount.setTypeface(tvFont2);
        holder.price.setTypeface(tvFont2);
        holder.originalprice.setTypeface(tvFont2);
        holder.name.setTypeface(tvFont);

        return convertView;
    }

    class ViewHolder{

        ImageView img, img_soldout, iamgeview_new_item, label_voucher;
        RelativeLayout evoucher_rl, ma_voucher_rl,pv_point_rl;
        LinearLayout mppv_ll;
        TextView originalprice, price, maAmount, voucherAmount, pvAmount, mPvPAmount;
        TextView name, promom1, promom2;
        ImageView promom3;
    }
}
