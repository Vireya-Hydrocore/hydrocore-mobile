package com.vireya.hydrocore.core.network;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.vireya.hydrocore.MainActivity;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.agenda.model.Aviso;

public class NotificacaoHelper {

    public static final String CHANNEL_ID = "AVISOS_CHANNEL";

    // Cria o canal (para Android 8+)
    public static void criarCanalNotificacao(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Avisos Hydrocore";
            String description = "Canal para avisos recebidos do sistema";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Mostra a notificação
    public static void mostrarNotificacao(Context context, Aviso aviso) {
        // 🔹 Checa se notificações estão ativas
        boolean notificacaoAtiva = context
                .getSharedPreferences("configuracoes", Context.MODE_PRIVATE)
                .getBoolean("notificacaoAtivo", true);

        if (!notificacaoAtiva) {
            // Se estiver desligado, não mostra nada
            return;
        }

        String channelId = "avisos_channel";
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Avisos",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // 🔹 Intent para abrir MainActivity e já sinalizar que veio da notificação
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("abrirAviso", true);
        intent.putExtra("idAviso", aviso.getIdAvisos());

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                aviso.getIdAvisos(), // requestCode único
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle("Novo Aviso")
                .setContentText(aviso.getDescricao())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(aviso.getIdAvisos(), builder.build());
    }
}
