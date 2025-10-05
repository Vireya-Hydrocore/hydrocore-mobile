//package com.vireya.hydrocore.core.network;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.vireya.hydrocore.MainActivity;
//import com.vireya.hydrocore.R;
//
//public class FirebaseMessagingService extends FirebaseMessagingService {
//
//    private static final String CHANNEL_ID = "FCM_CHANNEL";
//
//    @Override
//    public void onNewToken(String token) {
//        super.onNewToken(token);
//        // 🔹 Aqui você pode enviar o token para seu backend
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//        String title = remoteMessage.getNotification() != null ?
//                remoteMessage.getNotification().getTitle() : "Novo Aviso";
//        String body = remoteMessage.getNotification() != null ?
//                remoteMessage.getNotification().getBody() : "Você recebeu um aviso.";
//
//        showNotification(title, body);
//    }
//
//    private void showNotification(String title, String message) {
//        // Criar canal para Android 8+
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Notificações Hydrocore",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            if (manager != null) {
//                manager.createNotificationChannel(channel);
//            }
//        }
//
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//        );
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
//
//        NotificationManagerCompat.from(this).notify(1, builder.build());
//    }
//}
