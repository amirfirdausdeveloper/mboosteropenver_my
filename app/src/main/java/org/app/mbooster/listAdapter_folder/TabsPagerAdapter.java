package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.app.mbooster.R;

import java.util.ArrayList;

/**
 * Created by User on 6/9/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();
    ArrayList<String> notificationcount = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    Context context;

    int[] icons = {
            R.mipmap.home_icon,
            R.mipmap.icon_order_history,
//            R.drawable.shoppingbag,
            R.mipmap.rewards_icon,
            R.mipmap.icon_happening,
            R.mipmap.request_icon
    };
    String[] titles = {
            "Home", "Purchase History", "Shopping Bag", "MMspot", "Account"
    };
    String[] titlesCN = {
            "主页", "订购记录", "购物包", "M汇通", "账号"
    };
    int width;

    public TabsPagerAdapter(FragmentManager fm, Context context, int width) {
        super(fm);
        this.context = context;
        this.width = width;

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
//        return super.getItemPosition(object);
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragments(Fragment fragments, String titles, String notificationcount) {
        this.fragments.add(fragments);
        this.tabTitles.add(titles);
        this.notificationcount.add(notificationcount);
    }

    public void setNotificationcount(ArrayList<String> notificationcount) {
        this.notificationcount = notificationcount;
    }

//    public void UpdateNotification(int position, String notification) {
//        notificationcount.set(position, notification);
//        notifyDataSetChanged();
//    }

//    public void addFragments(Fragment fragments, String titles,String id)
//    {
//        this.fragments.add(fragments);
//        this.tabTitles.add(titles);
//        this.id.add(id);
//    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles.get(position);
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(context).inflate(R.layout.customtablayout, null);
        ImageView img = (ImageView) tab.findViewById(R.id.icon);
        TextView tv = (TextView) tab.findViewById(R.id.text);
        LinearLayout ly = (LinearLayout) tab.findViewById(R.id.ly);
        RelativeLayout notificationrl = (RelativeLayout) tab.findViewById(R.id.notificationrl);
        TextView notificationtv = (TextView) tab.findViewById(R.id.notificationtv);

        ly.setMinimumWidth((int) (width / 7.8));

        tv.setText(tabTitles.get(position));
        img.setImageResource(icons[position]);

        if (notificationcount.get(position).equals("0")) {
            notificationrl.setVisibility(View.GONE);
        }
        if (position == 0) {
            int tabIconColor = ContextCompat.getColor(context, R.color.colorbutton);
            img.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            tv.setTextColor(Color.parseColor("#EBA63F"));
        }
//        if(position == 3){
//            ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
//            layoutParams.height = layoutParams.height ;
//            layoutParams.width = layoutParams.width *2;
//            img.setLayoutParams(layoutParams);
//        }
        return tab;
    }
}
