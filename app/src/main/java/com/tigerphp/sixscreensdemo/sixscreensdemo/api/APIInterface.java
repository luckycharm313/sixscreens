package com.tigerphp.sixscreensdemo.sixscreensdemo.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luckycharm on 7/8/18.
 */

public interface APIInterface {
    void onSuccess(JSONObject param) throws JSONException;
    void onFailure(JSONObject param) throws JSONException;
}
