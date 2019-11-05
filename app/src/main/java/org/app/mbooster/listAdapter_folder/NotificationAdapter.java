package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.NotificationTitleItem;

import java.util.List;

/**
 * Created by royfei on 24/10/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private NotificationAdapter.OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = NotificationAdapter.class.getSimpleName();
    private List<NotificationTitleItem> list;
    private Context context;

    public NotificationAdapter(Context context, List<NotificationTitleItem> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Logger.d(TAG, "onCreateViewHolder, i: " + i);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item_notification, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        ViewHolder holder = (ViewHolder) viewHolder;
        holder.position = i;
        NotificationTitleItem item = list.get(i);
        holder.title.setText(item.title);
//        holder.imageView.setImageDrawable(context.getResources().getDrawable(item.icon));
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(item.icon, new ImageViewAware(holder.imageView, false));
        if (!item.notificationcount.equals("0")) {
            holder.notificationtv.setText(item.notificationcount);
            holder.notificationrl.setVisibility(View.VISIBLE);
        } else {
            holder.notificationtv.setText(item.notificationcount);
            holder.notificationrl.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View rootView;
        public ImageView imageView;
        public TextView title, notificationtv;
        public RelativeLayout notificationrl;
        public View unread;
        public int position;

        public ViewHolder(View itemView) {
            super(itemView);

            notificationtv = (TextView) itemView.findViewById(R.id.notificationtv);
            title = (TextView) itemView.findViewById(R.id.title);
            notificationrl = (RelativeLayout) itemView.findViewById(R.id.notificationrl);
            imageView = (ImageView) itemView.findViewById(R.id.icon);

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