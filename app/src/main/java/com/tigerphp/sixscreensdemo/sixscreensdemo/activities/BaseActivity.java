package com.tigerphp.sixscreensdemo.sixscreensdemo.activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tigerphp.sixscreensdemo.sixscreensdemo.utils.ResolutionSet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by luckycharm on 7/7/18.
 */

public class BaseActivity extends AppCompatActivity {

    boolean m_bInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void refreshUI() {
    }

    public void showToastMessage(String strMsg) {
        Toast.makeText(this, strMsg, Toast.LENGTH_LONG).show();
    }

    public void hideSoftKeyboard(/*View view*/) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken()/*view.getWindowToken()*/, 0);
    }

    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null)
        {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isConnected())
                return true;
        }

        return false;
    }

    public boolean isWiFiConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null)
        {
            NetworkInfo networkinfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if ((networkinfo != null) && (networkinfo.isAvailable() == true) && (networkinfo.getState() == NetworkInfo.State.CONNECTED))
                return true;
        }

        return false;
    }

    public String EncodeToUTF8(String str)
    {
        String tmp;

        try {
            tmp = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            tmp = str;
        }

        return tmp;
    }

    public void initActivity (final int i_nRootLayoutID) {
        final View rlayout = findViewById(i_nRootLayoutID);
        rlayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        rlayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        if (m_bInitialized == false)
                        {
                            m_bInitialized = true;

                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            int screenWidth = displayMetrics.widthPixels;
                            int screenHeight = displayMetrics.heightPixels;
/*
						Display display = getWindowManager().getDefaultDisplay();
						int screenWidth = display.getWidth();
						int screenHeight = display.getHeight();
*/
                            ResolutionSet._instance.setResolution(screenWidth, screenHeight);

                            Log.e("TAG", "---------BaseActivity initActivity---Without Default Param----");
                            ResolutionSet._instance.iterateChild(findViewById(i_nRootLayoutID));
                        }
                    }
                }
        );
    }
}
