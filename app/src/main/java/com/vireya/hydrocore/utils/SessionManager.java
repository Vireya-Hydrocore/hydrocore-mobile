package com.vireya.hydrocore.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "hydrocore_session";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_API_KEY = "api_key";
    private static final String KEY_EMAIL = "email";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveSession(String token, String apiKey, String email) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_API_KEY, apiKey);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getApiKey() {
        return prefs.getString(KEY_API_KEY, null);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}