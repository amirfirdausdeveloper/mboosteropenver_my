package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.SavePreferences;

import java.util.ArrayList;

/**
 * Created by User on 6/9/2016.
 */
public class TabsPagerAdapterSubCa extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    Context context;

    int width;

    public TabsPagerAdapterSubCa(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragments(Fragment fragments, String titles)
    {
        this.fragments.add(fragments);
        this.tabTitles.add(titles);

    }

    public void addFragments(Fragment fragments, String titles,String id)
    {
        this.fragments.add(fragments);
        this.tabTitles.add(titles);
        this.id.add(id);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles.get(position);
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(context).inflate(R.layout.customtablayout_subca, null);
        TextView tv = (TextView) tab.findViewById(R.id.text);
        LinearLayout ly = (LinearLayout) tab.findViewById(R.id.ly);

        ly.setMinimumWidth((int)(width/7.8));

        tv.setText(tabTitles.get(position));


//        if(this.id.get(position).equals("33") && SavePreferences.getUserID(context).equals("20002")){
//            ly.setBackgroundColor(Color.parseColor("#ff5252"));
//            //ly.setBackground(context.getResources().getDrawable(R.drawable.border));
//           tv.setTextColor(Color.parseColor("#ffffff"));
//        }

        return tab;
    }




}
