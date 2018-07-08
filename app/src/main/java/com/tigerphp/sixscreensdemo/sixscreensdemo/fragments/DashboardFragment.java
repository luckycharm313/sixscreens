package com.tigerphp.sixscreensdemo.sixscreensdemo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;

/**
 * Created by luckycharm on 7/8/18.
 */

public class DashboardFragment extends Fragment {

    View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.tab_dashboard, container, false);
        return mView;

    }
}
