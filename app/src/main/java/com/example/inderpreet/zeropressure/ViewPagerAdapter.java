package com.example.inderpreet.zeropressure;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by INDERPREET on 17-06-2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter{
    int mNumOfTabs;


    public ViewPagerAdapter(FragmentManager fm,int NumOfTabs) {
        super(fm);
        this.mNumOfTabs=NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AboutFragment tab1 = new AboutFragment();
                return tab1;
            case 1:
                SymptomFragment tab2 = new SymptomFragment();
                return tab2;
            case 2:
                StagesFragment tab3 = new StagesFragment();
                return tab3;
            case 3:
                CareFragment tab4 = new CareFragment();
                return tab4;

            default:
                return null;
        }    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
