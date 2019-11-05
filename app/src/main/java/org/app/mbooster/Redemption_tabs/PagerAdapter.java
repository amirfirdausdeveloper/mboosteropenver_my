package org.app.mbooster.Redemption_tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AllFragment tab1 = new AllFragment();
                return tab1;
            case 1:
                SilverFragment tab2 = new SilverFragment();
                return tab2;
            case 2:
                GoldFragment tab3 = new GoldFragment();
                return tab3;
            case 3:
                PlantinumFragment tab4 = new PlantinumFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}