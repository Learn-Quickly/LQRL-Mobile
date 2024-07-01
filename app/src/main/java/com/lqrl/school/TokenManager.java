package com.lqrl.school;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREF_NAME = "my_app_prefs";
    private static final String KEY_JWT_TOKEN = "access_token";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_JWT_TOKEN, token);
        editor.apply();
    }

    public static String getToken(Context context) {
        return getSharedPreferences(context).getString(KEY_JWT_TOKEN, null);
    }

    public static void clearToken(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_JWT_TOKEN);
        editor.apply();
    }
}
