package org.app.mbooster.activity_folder.Welcome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.app.mbooster.R;
import org.app.mbooster.listAdapter_folder.Slider.SliderTypes.BaseSliderView;

public class CustomBaseSlider extends BaseSliderView {
    public String page;

    public CustomBaseSlider(Context context, String page) {
        super(context);

        this.page = page;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView() {
//        View v = LayoutInflater.from(getContext()).inflate(com.daimajia.slider.library.R.layout.render_type_text,null);
//        ImageView target = (ImageView)v.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
//        target.setScaleType(ImageView.ScaleType.FIT_XY);
//        TextView description = (TextView)v.findViewById(com.daimajia.slider.library.R.id.description);
//        LinearLayout description_layout = (LinearLayout)v.findViewById(com.daimajia.slider.library.R.id.description_layout);
//        description.setVisibility(View.GONE);
//        description_layout.setVisibility(View.GONE);
//        description.setText(getDescription());
//        bindEventAndShow(v, target);

        View v = LayoutInflater.from(getContext()).inflate(R.layout.custombaseslider, null);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView desc = (TextView) v.findViewById(R.id.desc);
        ImageView icon = (ImageView) v.findViewById(R.id.icon);
        ImageView bg = (ImageView) v.findViewById(R.id.bg);


        if (page.equals("1")) {
            title.setText(R.string.certified);
            desc.setText(R.string.certified_fulltext);
            icon.setImageResource(R.drawable.ic_certified);
            bg.setImageResource(R.drawable.certified_bg);
        } else if (page.equals("2")) {
            title.setText(R.string.convenience);
            desc.setText(R.string.convenience_fulltext);
            icon.setImageResource(R.drawable.ic_convenience);
            bg.setImageResource(R.drawable.convenience_bg);
        } else if (page.equals("3")) {
            title.setText(R.string.worry_free);
            desc.setText(R.string.worry_free_fulltext);
            icon.setImageResource(R.drawable.ic_worry_free);
            bg.setImageResource(R.drawable.worry_free_bg);
        } else if (page.equals("4")) {
            title.setText(R.string.shop_safely);
            desc.setText(R.string.shop_safely_fulltext);
            icon.setImageResource(R.drawable.ic_secure);
            bg.setImageResource(R.drawable.secure_bg);
        }
        return v;
    }
}