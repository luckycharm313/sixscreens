package com.tigerphp.sixscreensdemo.sixscreensdemo.api;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by luckycharm on 7/8/18.
 */

public class ApiManager {

    public static void callApi (String url, RequestParams params, final APIInterface callback){

        APIService.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject e) {
                try {
                    Log.e("tag", "err 2 :"+e);
                    callback.onFailure(e);
                } catch (JSONException _e) {
                    _e.printStackTrace();
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
