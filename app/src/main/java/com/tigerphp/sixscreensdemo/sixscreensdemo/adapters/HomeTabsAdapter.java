package com.tigerphp.sixscreensdemo.sixscreensdemo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tigerphp.sixscreensdemo.sixscreensdemo.fragments.DashboardFragment;
import com.tigerphp.sixscreensdemo.sixscreensdemo.fragments.ScheduleFragment;

/**
 * Created by luckycharm on 7/8/18.
 */

public class HomeTabsAdapter extends FragmentStatePagerAdapter {

    public HomeTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DashboardFragment();
            case 1:
                return new ScheduleFragment();
            case 2:
                return new ScheduleFragment();
            case 3:
                return new ScheduleFragment();
            case 4:
                return new ScheduleFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

}
