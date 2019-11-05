package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.mbooster.R;

public class VoucherConversionAdapter extends BaseAdapter {

    private Context context;
    private int[] list;

    public VoucherConversionAdapter(Context context, int[] productItems) {
        this.context = context;
        this.list = productItems;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(context, android.R.layout.simple_list_item_1, null);
        TextView tv1 = convertView.findViewById(android.R.id.text1);

        int value = list[position];
        tv1.setText(String.valueOf(value)+context.getString(R.string.mbooster_evoucher_count_postfix));
//        final Typeface tvFont = Typeface.createFromAsset(context.getAssets(), "fonts/gotham_book.ttf");
//        final Typeface tvFont2 = Typeface.createFromAsset(context.getAssets(), "fonts/gotham_book_bold.ttf");
//        price.setTypeface(tvFont);
//        name.setTypeface(tvFont);
//        originalprice.setTypeface(tvFont);

        return convertView;
    }

}