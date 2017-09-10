package com.alphabetastudios.codeule.Settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abheisenberg on 28/8/17.
 */

public class UserPreferences {

    private SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    private static final String ALARM_EARLY = "alarm_early";
    private static final String FIRST_LAUNCH = "first_launch";

    /*
        A getter and setter for every setting list item.
     */

    private static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    public static boolean getAlarmEarly(Context context){
        return getSharedPrefs(context).getBoolean(ALARM_EARLY, false);
    }

    public static void setAlarmEarly(Context context, boolean isSet){
        editor = getSharedPrefs(context).edit();
        editor.putBoolean(ALARM_EARLY, isSet);
        editor.apply();
    }

    public static boolean getIsFirstLaunch(Context context){
        return getSharedPrefs(context).getBoolean(FIRST_LAUNCH, true);
    }

    public static void setIsFirstLaunch(Context context, boolean firstLaunch){
        editor = getSharedPrefs(context).edit();
        editor.putBoolean(FIRST_LAUNCH, firstLaunch);
        editor.apply();
    }
}
