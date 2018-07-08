package com.tigerphp.sixscreensdemo.sixscreensdemo.activities;

import android.os.Bundle;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivity(R.id.rl_main);
    }
}
