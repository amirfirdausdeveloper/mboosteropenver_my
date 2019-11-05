package org.app.mbooster.activity_folder.Welcome;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.model_folder.SavePreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChooseLanguage extends AppCompatActivity {


    RadioGroup radio_group;
    RadioButton english, chinese;
    Button ok_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Gotham Rounded/GothamRnd-Book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        radio_group = (RadioGroup) findViewById(R.id.radio_group);

        english = (RadioButton) findViewById(R.id.english);
        chinese = (RadioButton) findViewById(R.id.chinese);

        LinearLayout.LayoutParams rb_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Drawable drawable = getResources().getDrawable(R.drawable.radio_btn_custom);
        drawable.setBounds(0, 0, 75, 72);
        Drawable drawable2 = getResources().getDrawable(R.drawable.radio_btn_custom);
        drawable2.setBounds(0, 0, 75, 72);

        english.setCompoundDrawables(null, null, drawable, null);
        chinese.setCompoundDrawables(null, null, drawable2, null);

        english.setChecked(true);


        ok_btn = (Button) findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (english.isChecked()) {
                    Helper.setAppLocale(ChooseLanguage.this, "en", "");
                    SavePreferences.setApplanguage(ChooseLanguage.this, "ENG");
                    Toast.makeText(ChooseLanguage.this, "English is selected", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(ChooseLanguage.this, OnBoadingActivity.class));
                } else if (chinese.isChecked()) {
                    Helper.setAppLocale(ChooseLanguage.this, "zh", "CN");
                    SavePreferences.setApplanguage(ChooseLanguage.this, "SCN");
                    Toast.makeText(ChooseLanguage.this, "已选择中文", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(ChooseLanguage.this, OnBoadingActivity.class));
                } else {
                    Toast.makeText(ChooseLanguage.this, "No language is selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
