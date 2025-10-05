package com.vireya.hydrocore.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_NOME = "nome_usuario";
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void salvarUsuario(String nome) {
        editor.putString(KEY_NOME, nome);
        editor.apply();
    }

    public String getUsuarioNome() {
        return prefs.getString(KEY_NOME, "Usuário");
    }

    public void limparSessao() {
        editor.clear();
        editor.apply();
    }
}
