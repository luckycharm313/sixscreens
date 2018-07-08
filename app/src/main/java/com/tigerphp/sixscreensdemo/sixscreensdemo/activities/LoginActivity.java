package com.tigerphp.sixscreensdemo.sixscreensdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initActivity(R.id.rl_login);
    }

    public void onLoginClick(View _v) {
        Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(mIntent);
    }
}
