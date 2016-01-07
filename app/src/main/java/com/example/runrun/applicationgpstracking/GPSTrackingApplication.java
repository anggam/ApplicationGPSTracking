package com.example.runrun.applicationgpstracking;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by RUNRUN on 1/4/2016.
 */
public class GPSTrackingApplication extends Application {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static GPSTrackingApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        pref = getSharedPreferences("app_pref", MODE_PRIVATE);
        editor = pref.edit();

        instance = this;
    }

    public static GPSTrackingApplication getInstance() {
        return instance;
    }

    public void saveMyUserId(int id) {
        editor.putInt("my_user_id", id);
        editor.apply();
    }

    public boolean isLoggedIn() {
        int userId = pref.getInt("my_user_id", 0);
        return userId != 0;
    }
}
