package com.tigerphp.sixscreensdemo.sixscreensdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.api.APIInterface;
import com.tigerphp.sixscreensdemo.sixscreensdemo.api.ApiManager;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppHelper;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.EndPoints;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.email)
    EditText txt_email;
    @BindView(R.id.password)
    EditText txt_password;

    private String str_email;
    private String str_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initActivity(R.id.rl_login);
        ButterKnife.bind(this);

        String token = PreferenceManager.getToken(this);
        if(token != null){
            Intent mIntent = new Intent(this, MainActivity.class);
            this.startActivity(mIntent);
            finish();
        }

    }

    public void onLoginClick(View _v) {
        str_email = txt_email.getText().toString();
        str_password = txt_password.getText().toString();

        Boolean validateResult = validateInformation();
        if(validateResult)
            login_action();
        else
            AppHelper.CustomToast(this, "Empty Data");
    }

    private Boolean validateInformation() {
        Boolean validate_result_flag = true;
        if(!isEmptyOfEditBox(txt_email)) {
            validate_result_flag = false;
        }

        if(!isEmptyOfEditBox(txt_password)) {
            validate_result_flag = false;
        }
        return validate_result_flag;
    }

    private boolean isEmptyOfEditBox(EditText _e) {
        return _e.getText().length() >0 ? true : false;
    }

    public void login_action(){

        RequestParams params    = new RequestParams();
        params.put("username", str_email);
        params.put("password", str_password);

        ApiManager.callApi(EndPoints.loginEndpoint, params, new APIInterface() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                Boolean status      = response.getBoolean("success");
                if(status){
                    JSONObject user_data = response.getJSONObject("data");
                    PreferenceManager.setToken(LoginActivity.this, "token");
                    PreferenceManager.setUserData(LoginActivity.this, user_data);

                    Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(mIntent);
                }
                else{
                    String message = response.getString("message");
                    AppHelper.CustomToast(LoginActivity.this, message);
                }
            }

            @Override
            public void onFailure(JSONObject response) throws JSONException {
                AppHelper.CustomToast(LoginActivity.this, "Network Error!");
            }
        });
    }

}
