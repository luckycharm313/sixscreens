package com.tigerphp.sixscreensdemo.sixscreensdemo.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.EndPoints;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.PreferenceManager;

import java.security.NoSuchAlgorithmException;

/**
 * Created by luckycharm on 7/8/18.
 */

public class APIService {
    public APIService() throws NoSuchAlgorithmException {

    }

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //client.addHeader("token", PreferenceManager.getToken(BuzzeeApplication.getInstance()));
//        client.setTimeout(5*60*1000);
//        client.setConnectTimeout(5*60*1000);
//        client.setResponseTimeout(5*60*1000);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //client.addHeader("token", PreferenceManager.getToken(BuzzeeApplication.getInstance()));
//        client.setTimeout(5*60*1000);
//        client.setConnectTimeout(5*60*1000);
//        client.setResponseTimeout(5*60*1000);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return EndPoints.baseURL + relativeUrl;
    }
}
