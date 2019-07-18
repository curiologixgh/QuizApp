package com.quizapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

// its common PrefManager class which will use to save local data
public class PrefManager {
    public SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("QuizAppPrefs", context.MODE_PRIVATE);
    }

    public int getMyIntPref(Context context, String prefName, int defVal) {
        return getPrefs(context).getInt(prefName, defVal);
    }

    public void setMyIntPref(Context context, String prefName, int value) {
        getPrefs(context).edit().putInt(prefName, value).commit();
    }

    public boolean getMyBooleanPref(Context context, String prefName) {
        return getPrefs(context).getBoolean(prefName, false);
    }

    public void setMyBooleanPref(Context context, String prefName, boolean value) {
        getPrefs(context).edit().putBoolean(prefName, value).commit();
    }
}