package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.mbooster.R;

import java.util.List;

public class ProductOptionAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;

    public ProductOptionAdapter(Context context, List<String> _list) {
        this.context = context;
        this.list = _list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(context, android.R.layout.simple_list_item_1, null);
        TextView tv1 = convertView.findViewById(android.R.id.text1);

        String value = list.get(position);
        tv1.setText(value);
//        final Typeface tvFont = Typeface.createFromAsset(context.getAssets(), "fonts/gotham_book.ttf");
//        final Typeface tvFont2 = Typeface.createFromAsset(context.getAssets(), "fonts/gotham_book_bold.ttf");
//        price.setTypeface(tvFont);
//        name.setTypeface(tvFont);
//        originalprice.setTypeface(tvFont);

        return convertView;
    }

}