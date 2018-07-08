package com.tigerphp.sixscreensdemo.sixscreensdemo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.adapters.HomeTabsAdapter;
import com.tigerphp.sixscreensdemo.sixscreensdemo.api.APIInterface;
import com.tigerphp.sixscreensdemo.sixscreensdemo.api.ApiManager;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppHelper;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.EndPoints;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.PreferenceManager;
import com.tigerphp.sixscreensdemo.sixscreensdemo.models.Pusher;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants.PERMISSION_REQUEST_CODE;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

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
    }

    private void Permissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }
    /** clock in button in layout_clocked_out **/
    public void onClockIn(View v){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
        else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, AppConstants.UPLOAD_PICTURE_REQUEST_CODE);
        }
    }

    public void onChangeJob(View _v){

    }

    /** clock in button in layout_clocked_in **/

    public void onClockOut(View v){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
        else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, AppConstants.UPLOAD_PICTURE_REQUEST_CODE_);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstants.UPLOAD_PICTURE_REQUEST_CODE:

                    //Uri _uri = data.getData();
                    try {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = AppHelper.getImageUri(getApplicationContext(), imageBitmap);

                        String FilePath = AppHelper.getFilePath(MainActivity.this, tempUri);
                        final String jobName = "Paint Cabinets";

                        try {
                            InputStream targetStream = new FileInputStream(FilePath);
                            RequestParams params    = new RequestParams();
                            params.put("username", userData.getString("username"));
                            params.put("jobName", jobName);
                            params.put("photo", targetStream);

                            ApiManager.callApi(EndPoints.clockInEndpoint, params, new APIInterface() {
                                @Override
                                public void onSuccess(JSONObject response) throws JSONException {
                                    Boolean status      = response.getBoolean("success");

                                    if(status){
                                        JSONObject user_data = response.getJSONObject("data");
                                        String photoURL = user_data.getString("photoURL");

                                        EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEW_USER_PHOTO, photoURL, jobName, true));
                                    }
                                    else{
                                        String message = response.getString("message");
                                        AppHelper.CustomToast(MainActivity.this, message);
                                    }
                                }

                                @Override
                                public void onFailure(JSONObject response) throws JSONException {
                                    AppHelper.CustomToast(MainActivity.this, "Network Error!");
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        Log.e("tag", "err"+e);
                        e.printStackTrace();
                    }

                    break;
                case AppConstants.UPLOAD_PICTURE_REQUEST_CODE_:

                    try {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = AppHelper.getImageUri(getApplicationContext(), imageBitmap);

                        String FilePath = AppHelper.getFilePath(MainActivity.this, tempUri);

                        final String jobName = "Paint Cabinets";

                        try {
                            InputStream __targetStream = new FileInputStream(FilePath);
                            RequestParams __params    = new RequestParams();
                            __params.put("username", userData.getString("username"));
                            __params.put("jobName", jobName);
                            __params.put("photo", __targetStream);

                            ApiManager.callApi(EndPoints.clockOutEndpoint, __params, new APIInterface() {
                                @Override
                                public void onSuccess(JSONObject response) throws JSONException {
                                    Boolean status      = response.getBoolean("success");

                                    if(status){
                                        JSONObject user_data = response.getJSONObject("data");
                                        String photoURL = user_data.getString("photoURL");

                                        EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEW_USER_PHOTO, photoURL, jobName, false));
                                    }
                                    else{
                                        String message = response.getString("message");
                                        AppHelper.CustomToast(MainActivity.this, message);
                                    }
                                }

                                @Override
                                public void onFailure(JSONObject response) throws JSONException {
                                    AppHelper.CustomToast(MainActivity.this, "Network Error!");
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        Log.e("tag", "err"+e);
                        e.printStackTrace();
                    }

                    break;
                default:

            }
        }
    }
}
