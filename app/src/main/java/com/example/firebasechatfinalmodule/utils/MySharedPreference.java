package com.example.firebasechatfinalmodule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 21/2/18.
 */

public class MySharedPreference {

    public Activity activity;
    public SharedPreferences sharedPreferences;
    final public static String USER_ID="USER_ID";
    final public static String USER_Name="USER_Name";
    final public static String USER_PROFILE_IMAGE="user_image";


public MySharedPreference()
{

}
    public MySharedPreference(Activity activity) {
        this.activity = activity;
        createSharedPreferences();
    }

    public void createSharedPreferences() {
        sharedPreferences = activity.getSharedPreferences("com.jewelleryscale.user.tradingsoftware", Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();

    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public boolean getboolean(String key){
           return sharedPreferences.getBoolean(key,false);
    }

    public void putboolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();

    }

    public void removeString(String key) {
        sharedPreferences.edit().remove(key).commit();
    }

    public void removeAll() {
        sharedPreferences.edit().clear().commit();
    }



    public void setBoolean(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).commit();

    }
    public boolean isBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }
}
