package com.tigerphp.sixscreensdemo.sixscreensdemo.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.adapters.HomeTabsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    HomeTabsAdapter mFragmentStatePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActivity(R.id.rl_main);
        initializerView();
    }

    private void initializerView() {

        mFragmentStatePagerAdapter = new HomeTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mFragmentStatePagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setCurrentItem(0);
        tabLayout.getTabAt(0).setCustomView(R.layout.custom_tab_dashboard);
        tabLayout.getTabAt(1).setCustomView(R.layout.custom_tab_schedule);
        tabLayout.getTabAt(2).setCustomView(R.layout.custom_tab_timesheets);
        tabLayout.getTabAt(3).setCustomView(R.layout.custom_tab_timeoff);
        tabLayout.getTabAt(4).setCustomView(R.layout.custom_tab_messages);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
