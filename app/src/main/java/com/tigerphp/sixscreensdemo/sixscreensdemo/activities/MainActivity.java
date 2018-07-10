package com.tigerphp.sixscreensdemo.sixscreensdemo.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.adapters.HomeTabsAdapter;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.ImageLoader;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.PreferenceManager;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants.PERMISSION_REQUEST_CODE;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.nav_view)
    NavigationView nav_view;
    @BindView(R.id.rl_main)
    DrawerLayout rl_main;

    HomeTabsAdapter mFragmentStatePagerAdapter;
    JSONObject userData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActivity(R.id.rl_main);
        initializerView();
        Permissions();
    }

    private void initializerView() {
        userData = PreferenceManager.getUserData(this);

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

        try {
            String username = userData.getString("firstName") + " "+ userData.getString("lastName");
            String url = userData.getString("photoURL");


            View menuNav = nav_view.getHeaderView(0);
            nav_view.setNavigationItemSelectedListener(this);

            TextView tv = (TextView) menuNav.findViewById(R.id.nav_header_textView);
            CircleImageView img_avatar_menu = (CircleImageView) menuNav.findViewById(R.id.img_avatar_menu);

            tv.setText(username);
            ImageLoader.loadCircleImage(this, url, img_avatar_menu, R.drawable.avatar);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void Permissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }

    public void onClickMenu(View v){
        rl_main.openDrawer(GravityCompat.START);
    }

    public void closeMenu(View v){
        rl_main.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_item_setting:
                break;
            case R.id.nav_item_chat:
                break;
            case R.id.nav_item_feedback:
                break;
            case R.id.nav_item_signout:
                Logout();
                break;

        }
        rl_main.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Logout(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are you sure you want to logout?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceManager.setToken(MainActivity.this, null);
                Intent mIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(mIntent);
            }
        });
        alert.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.setCancelable(false);
        alert.show();
    }
}
