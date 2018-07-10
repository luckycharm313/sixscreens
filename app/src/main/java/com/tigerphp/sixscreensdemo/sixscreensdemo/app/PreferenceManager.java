package com.tigerphp.sixscreensdemo.sixscreensdemo.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luckycharm on 7/8/18.
 */

public class PreferenceManager {
    private static SharedPreferences mSharedPreferences;
    private static final String KEY_USER_PREF = "KEY_USER_PREFERENCES";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USERDATA = "KEY_USERDATA";
    private static final String key_test = "test";

    private static final String KEY_TYPE = "KEY_TYPE";
    private static final String KEY_WORK_TIME = "KEY_WORK_TIME";
    /**
     * method to get token
     *
     * @return return value
     */
    public static String getToken(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_TOKEN, null);
    }

    /**
     * method to set token
     *
     * @return return true
     */
    public static boolean setToken(Context mContext, String token) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        return editor.commit();
    }


    public static String getTest(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key_test, null);
    }

    public static boolean setTest(Context mContext, String test) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key_test, test);
        return editor.commit();
    }

    public static String getBreakType(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_TYPE, null);
    }

    public static boolean setBreakType(Context mContext, String type) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_TYPE, type);
        return editor.commit();
    }

    public static long getWorkTime(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getLong(KEY_WORK_TIME, 0L);
    }

    public static boolean setWorkTime(Context mContext, long workTime) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(KEY_WORK_TIME, workTime);
        return editor.commit();
    }

    /**
     * method to get token
     *
     * @return return value
     */
    public static JSONObject getUserData(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        String temp = mSharedPreferences.getString(KEY_USERDATA, null);

        try {
            JSONObject jsonObj = new JSONObject(temp);
            return jsonObj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
         return null;

    }

    /**
     * method to set token
     *
     * @return return true
     */
    public static boolean setUserData(Context mContext, JSONObject data) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_USERDATA, data.toString());
        return editor.commit();
    }

    /** remove all sharedpreference data*/
    public static boolean removeAllPreferences(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }
}
