package com.vireya.hydrocore.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "hydrocore_session";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_API_KEY = "api_key";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ETA = "ETA";
    private static final String KEY_ID_FUNCIONARIO = "idFuncionario";

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

    // 🔹 Salvar ID do funcionário
    public void setIdFuncionario(int idFuncionario) {
        editor.putInt(KEY_ID_FUNCIONARIO, idFuncionario);
        editor.apply();
    }

    // 🔹 Obter ID do funcionário
    public int getIdFuncionario() {
        return prefs.getInt(KEY_ID_FUNCIONARIO, -1);
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

    // 🔹 Salvar e recuperar ETA
    public void setEta(String eta) {
        editor.putString(KEY_ETA, eta);
        editor.apply();
    }

    public String getEta() {
        return prefs.getString(KEY_ETA, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
