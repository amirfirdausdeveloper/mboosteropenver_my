package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.model_folder.PHModels;
import org.app.mbooster.shopping_mall.Payment.PaymentWebView;
import org.app.mbooster.shopping_mall.PurchaseHistoryItemDetails;

import java.util.ArrayList;

import static android.view.View.GONE;
import static org.app.mbooster.Helper.Helper.buttonEffect;

/**
 * Created by royfei on 19/06/2017.
 */

public class PHExpandableListViewAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<PHModels.PurchaseOrder> purchaseOrders_list;
    LayoutInflater layoutInflater;
    AssetManager assetManager;
    Typeface tvFont;

    public PHExpandableListViewAdapter(Context context, ArrayList<PHModels.PurchaseOrder> purchaseOrders_list) {
        layoutInflater = LayoutInflater.from(context);
        this.purchaseOrders_list = purchaseOrders_list;
        this.context = context;

        assetManager = context.getApplicationContext().getAssets();
        tvFont = Typeface.createFromAsset(assetManager, "fonts/gotham_book_bold.ttf");

    }

    @Override
    public int getGroupCount() {
        return purchaseOrders_list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return purchaseOrders_list.get(i).vendors.size() + 1;
    }

    @Override
    public Object getGroup(int i) {
        return purchaseOrders_list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return purchaseOrders_list.get(i).vendors.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View v = layoutInflater.inflate(R.layout.ph_group_layout, null);

        PHModels.PurchaseOrder item = purchaseOrders_list.get(i);

        TextView invoiceid = (TextView) v.findViewById(R.id.piid);
        TextView date = (TextView) v.findViewById(R.id.date);
        invoiceid.setText("#" + item.invoiceId);
        invoiceid.setTypeface(tvFont);
        date.setText(item.date);
        return v;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View v, ViewGroup viewGroup) {

        LogHelper.debug("[getChildView] i " +i + ", poi " + purchaseOrders_list.get(i));

        if (i1 < purchaseOrders_list.get(i).vendors.size()) {
            v = layoutInflater.inflate(R.layout.ph_child_layout, null);

            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
            for (int ii = 0; ii < purchaseOrders_list.get(i).vendors.get(i1).items.size(); ii++) {
                View vv = layoutInflater.inflate(R.layout.ph_listview_purchase_item, null);

                RelativeLayout relativelayout = (RelativeLayout) vv.findViewById(R.id.relativelayout);
                linearLayout.addView(vv);
                if (i1 % 2 != 0) {
                    relativelayout.setBackgroundColor(Color.parseColor("#fafafa"));
                }
                TextView name = (TextView) vv.findViewById(R.id.name);
                Button status = (Button) vv.findViewById(R.id.status);
                ImageView img = (ImageView) vv.findViewById(R.id.img);

                buttonEffect(status);

                final int finalIi = ii;
                status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PurchaseHistoryItemDetails.class);
                        intent.putExtra("piid", purchaseOrders_list.get(i).vendors.get(i1).piid);
                        intent.putExtra("cart_id", purchaseOrders_list.get(i).vendors.get(i1).items.get(finalIi).itemCartId);
                        context.startActivity(intent);
                    }
                });
                String statusstr = purchaseOrders_list.get(i).vendors.get(i1).items.get(ii).itemStatus;
                if (statusstr.equals("0") || statusstr.equals("1")) {
                    status.setText(context.getString(R.string.order_processing));
                } else if (statusstr.equals("2")) {
                    status.setText(context.getString(R.string.order_shipped));
                    status.setBackgroundResource(R.drawable.buttonrectangle3);
                } else if (statusstr.equals("3")) {
                    status.setText(context.getString(R.string.order_completed));
                    status.setBackgroundResource(R.drawable.buttonrectangle4);
                } else if (statusstr.equals("4")) {
                    status.setText(context.getString(R.string.order_refunds));
                } else if (statusstr.equals("5")) {
                    status.setText(context.getString(R.string.order_cancel2));
                    status.setBackgroundResource(R.drawable.buttonrectangle2);
                }else if (statusstr.equals("999")) {
                    status.setText("Incomplete Payment");
                    status.setBackgroundResource(R.drawable.buttonrectangle2);
                }

//                Log.i("status", statusstr);
//                Log.i("status", "aa" + context.getString(R.string.order_cancel2));

                ImageAware imageAware = new ImageViewAware(img);
                ImageLoader.getInstance().displayImage(purchaseOrders_list.get(i).vendors.get(i1).items.get(ii).itemImg, imageAware);

                name.setText(purchaseOrders_list.get(i).vendors.get(i1).items.get(ii).itemName);

                if (ii == purchaseOrders_list.get(i).vendors.get(i1).items.size() - 1) {
                    View view1 = layoutInflater.inflate(R.layout.ph_vendor_item, null);
                    RelativeLayout relativelayout1 = (RelativeLayout) view1.findViewById(R.id.relativelayout1);

                    if (i1 % 2 != 0) {
                        relativelayout1.setBackgroundColor(Color.parseColor("#fafafa"));
                    }
                    linearLayout.addView(view1);
                    TextView name1 = (TextView) view1.findViewById(R.id.name);
                    TextView totalprice = (TextView) view1.findViewById(R.id.totalprice);

                    name1.setText(purchaseOrders_list.get(i).vendors.get(i1).vendorName);
                    name1.setVisibility(GONE);

                    totalprice.setText(context.getString(R.string.total) + ": " + context.getString(R.string.currency) + purchaseOrders_list.get(i).vendors.get(i1).vendorTotalPrice);
                }
            }
        } else {
            v = layoutInflater.inflate(R.layout.ph_total_pi_footer, null);

            TextView voucher_total = (TextView) v.findViewById(R.id.voucher_total);
            TextView paid_total = (TextView) v.findViewById(R.id.paid_total);
            TextView total_outstanding_amount = (TextView) v.findViewById(R.id.total_outstanding_amount);
            TextView ma_total = v.findViewById(R.id.ma_total);
            Button paynowBtn = (Button) v.findViewById(R.id.paynowBtn);
            TextView total_voucher_value = (TextView) v.findViewById(R.id.total_voucher_value);

            if (purchaseOrders_list.get(i).paymtIncompl.equals("1")) {
                paid_total.setText(context.getString(R.string.currency) + purchaseOrders_list.get(i).total_paid_mat);
                total_outstanding_amount.setText(context.getString(R.string.total_outstanding_amount) + ": " + context.getString(R.string.currency) + purchaseOrders_list.get(i).total_outstanding_amount);
                total_voucher_value.setText(context.getString(R.string.total_voucher_value) + context.getString(R.string.currency) + purchaseOrders_list.get(i).total_voucher_value);
                ma_total.setText(context.getString(R.string.total_ma_value)+ context.getString(R.string.currency)+purchaseOrders_list.get(i).total_paid_mat);
                paynowBtn.setText("Pay Now");
                paynowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PaymentWebView.class);
                        intent.putExtra("url", purchaseOrders_list.get(i).payment_link);
                        context.startActivity(intent);
                    }
                });
            }else{
                paid_total.setText(context.getString(R.string.currency) + purchaseOrders_list.get(i).total_paid);
                total_outstanding_amount.setVisibility(GONE);
                total_voucher_value.setVisibility(GONE);
                ma_total.setVisibility(View.GONE);
                paynowBtn.setVisibility(GONE);
            }

            if (purchaseOrders_list.get(i).voucher.equals("0.00")) {
                if(purchaseOrders_list.get(i).voucher_qualify.equals("1")) {
                    voucher_total.setText(context.getString(R.string.mbooster_voucher_entitlement) );
                }else{
                    voucher_total.setVisibility(GONE);
                    voucher_total.setText(context.getString(R.string.total) +context.getString(R.string.currency) + purchaseOrders_list.get(i).total_amount + "\n"
                            + context.getString(R.string.total_voucher_value) +context.getString(R.string.currency) + purchaseOrders_list.get(i).voucher);
                }
            }else{
                if(purchaseOrders_list.get(i).voucher_qualify.equals("1")) {
                    voucher_total.setText(context.getString(R.string.mbooster_voucher_entitlement)+ "\n"
                            +context.getString(R.string.total) +context.getString(R.string.currency) + purchaseOrders_list.get(i).total_amount + "\n"
                            + context.getString(R.string.total_voucher_value) +context.getString(R.string.currency) + purchaseOrders_list.get(i).voucher);
                }else{
                    voucher_total.setText(context.getString(R.string.total) +context.getString(R.string.currency) + purchaseOrders_list.get(i).total_amount + "\n"
                            + context.getString(R.string.total_voucher_value) +context.getString(R.string.currency) + purchaseOrders_list.get(i).voucher);
                }
            }

            //voucher_total.setText(context.getString(R.string.currency) + purchaseOrders_list.get(i).voucher);
            //paid_total.setText(context.getString(R.string.currency) + purchaseOrders_list.get(i).total_paid);
        }
        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


}
