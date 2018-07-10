package com.tigerphp.sixscreensdemo.sixscreensdemo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;

import butterknife.ButterKnife;

/**
 * Created by luckycharm on 7/10/18.
 */

public class ScheduleFragment extends Fragment {
    View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_schedule, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }
}
