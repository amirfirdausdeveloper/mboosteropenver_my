package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.ProductModel;

import java.util.List;

/**
 * Created by royfei on 07/11/2017.
 */

public class EventGridAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
    private static final String TAG = NotificationAdapter.class.getSimpleName();
    private List<ProductModel> list;
    private Context context;


    public EventGridAdapter(Context context, List<ProductModel> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Logger.d(TAG, "onCreateViewHolder, i: " + i);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item2, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.position = i;
        final ProductModel item = list.get(i);

        ImageAware imageAware = new ImageViewAware(holder.img);
        ImageLoader.getInstance().displayImage(item.getProductimg(), imageAware);

        holder.pts.setText(context.getString(R.string.currency) + item.getProductpts());
       // holder.pts_discount.setText(context.getString(R.string.currency) + item.getProductpts());
       // holder.pts_discount.setPaintFlags(holder.pts_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        String s = context.getString(R.string.currency) + item.getProductpts();

        holder.pts_discount.setText(s, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) holder.pts_discount.getText();
        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.name.setText(item.getProduct_label());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View rootView;
        public ImageView img;
        public TextView name, pts, pts_discount;
        public int position;

        public ViewHolder(View itemView) {
            super(itemView);

            pts = (TextView) itemView.findViewById(R.id.pts);
            pts_discount = (TextView) itemView.findViewById(R.id.pts_discount);
            img = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.name);

            rootView = itemView.findViewById(R.id.rootview);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }
}