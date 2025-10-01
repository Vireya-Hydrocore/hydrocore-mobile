package com.vireya.hydrocore;

import android.app.Application;

import com.vireya.hydrocore.core.network.NotificacaoHelper;

public class HydrocoreApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Cria o canal de notificação logo na inicialização
        NotificacaoHelper.criarCanalNotificacao(this);
    }
}
