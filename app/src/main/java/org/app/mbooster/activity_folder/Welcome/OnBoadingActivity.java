package org.app.mbooster.activity_folder.Welcome;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.listAdapter_folder.Slider.*;
import org.app.mbooster.listAdapter_folder.Slider.Indicators.*;
import org.app.mbooster.listAdapter_folder.Slider.SliderTypes.BaseSliderView;
import org.app.mbooster.listAdapter_folder.Slider.Tricks.*;
import org.app.mbooster.model_folder.SavePreferences;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OnBoadingActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    private PagerIndicator custom_indicator;
    private TextView skip;
    private Button letgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boading);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Gotham Rounded/GothamRnd-Book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        custom_indicator = (PagerIndicator) findViewById(R.id.custom_indicator);
        skip = (TextView) findViewById(R.id.skip);
        letgo = (Button) findViewById(R.id.letgo);
        letgo.setVisibility(View.GONE);

        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();

        file_maps.put("1", R.drawable.onboarding_01);
        file_maps.put("2", R.drawable.onboarding_02);
        file_maps.put("3", R.drawable.onboarding_03);
        file_maps.put("4", R.drawable.onboarding_04);

        SavePreferences.setFirstTimeApp(OnBoadingActivity.this, "1");

        for (int i = 0; i < file_maps.size(); i++) {
            CustomBaseSlider textSliderView = new CustomBaseSlider(this, String.valueOf(i + 1));
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(String.valueOf(i + 1)))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
//                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "");

            mDemoSlider.addSlider(textSliderView);
        }

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoadingActivity.this, MainActivity.class));
            }
        });
        letgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoadingActivity.this, MainActivity.class));
            }
        });


//        for (String name : file_maps.keySet()) {
//            CustomBaseSlider textSliderView = new CustomBaseSlider(this);
//            // initialize a SliderLayout
//            textSliderView
//                    .image(file_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit);
////                    .setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra", "");
//
//            mDemoSlider.addSlider(textSliderView);
//        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setCustomIndicator(custom_indicator);
        mDemoSlider.setCustomAnimation(null);
        mDemoSlider.stopAutoCycle();
        mDemoSlider.setCurrentPosition(0);
        mDemoSlider.addOnPageChangeListener(this);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
        if (position == 3) {
            custom_indicator.setVisibility(View.GONE);
            skip.setVisibility(View.GONE);
            letgo.setVisibility(View.VISIBLE);
        } else {
            custom_indicator.setVisibility(View.VISIBLE);
            skip.setVisibility(View.VISIBLE);
            letgo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


}
