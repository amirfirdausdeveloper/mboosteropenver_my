package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.shopping_mall.HomeFragment3;
import org.app.mbooster.shopping_mall.Subcategory;


import java.util.List;

public class GridRAdapter extends RecyclerView.Adapter {

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private EventAdapter.OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(EventAdapter.OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = NotificationAdapter.class.getSimpleName();
    private List<HomeFragment3.Category_item> list;
    private Context context;
    private AssetManager assetManager;

    public GridRAdapter(Context context, List<HomeFragment3.Category_item> list) {
        this.list = list;
        this.context = context;
        this.assetManager = context.getApplicationContext().getAssets();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Logger.d(TAG, "onCreateViewHolder, i: " + i);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, null);

        return new GridRAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {

        final GridRAdapter.ViewHolder holder = (GridRAdapter.ViewHolder) viewHolder;
        holder.position = i;
        final HomeFragment3.Category_item item = list.get(i);


        holder.tv.setText(item.getCategory_name());
//        holder.tv.setTextColor(Color.WHITE);

        final Typeface tvFont = Typeface.createFromAsset(assetManager, "fonts/gotham_book_bold.ttf");
        holder.tv.setTypeface(tvFont);

        int width = context.getResources().getDisplayMetrics().widthPixels / 2;
        ViewGroup.LayoutParams layoutParams = holder.img.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = (int) (((context.getResources().getDisplayMetrics().widthPixels - 20) / 1.5) / 1.33333);

        holder.img.setLayoutParams(layoutParams);

        LogHelper.debug("item.getCategory_image() = " + item.getCategory_image());

        ImageLoader.getInstance().displayImage(item.getCategory_image(), new ImageViewAware(holder.img, false));

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Subcategory.class);
                intent.putExtra("category_id", list.get(i).getCategory_id());
                intent.putExtra("category_name", list.get(i).getCategory_name());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View rootView;
        public ImageView img;
        public TextView tv;
        public int position;

        public ViewHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.category_name);
            img = (ImageView) itemView.findViewById(R.id.img);


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