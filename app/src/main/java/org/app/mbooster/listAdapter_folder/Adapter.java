package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.ProductModel;


import java.util.List;

/**
 * Created by royfei on 03/10/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ProductModel> mProductListItems;
    private boolean mHorizontal;
    private boolean mPager;
    private Context context;

    public Adapter(Context context, boolean horizontal, boolean pager, List<ProductModel> productListItems) {
        mHorizontal = horizontal;
        mProductListItems = productListItems;
        mPager = pager;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mPager) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_pager, parent, false));
        } else {
            return mHorizontal ? new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter, parent, false)) :
                    new ViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.adapter_vertical, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductModel productListItem = mProductListItems.get(position);
//        holder.imageView.setImageResource(productListItem.getDrawable());
//        String imageUri = "drawable://" + productListItem.getDrawable(); // from drawables (only images, non-9patch)

//        ImageLoader.getInstance().displayImage(imageUri, new ImageViewAware(holder.imageView, false));
//        holder.nameTextView.setText(productListItem.getName());
//        holder.ratingTextView.setText(String.valueOf(productListItem.getPrice()));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mProductListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView nameTextView;
        public TextView ratingTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            ratingTextView = (TextView) itemView.findViewById(R.id.ratingTextView);
        }

        @Override
        public void onClick(View v) {
//            Log.d("ProductListItem", mProductListItems.get(getAdapterPosition()).getName());
//            String id = mProductListItems.get(getAdapterPosition()).getId();
//
//            Intent intent = new Intent(context,ProductModel.class);
//            intent.putExtra("product_id",id);
//            context.startActivity(intent);

        }
    }

}
