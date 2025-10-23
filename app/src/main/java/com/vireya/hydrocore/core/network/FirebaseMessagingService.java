//package com.vireya.hydrocore.core.network;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.vireya.hydrocore.R;
//import com.vireya.hydrocore.MainActivity;
//
//public class FirebaseMessagingService extends FirebaseMessagingService {
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        if (remoteMessage.getNotification() != null) {
//            String title = remoteMessage.getNotification().getTitle();
//            String body = remoteMessage.getNotification().getBody();
//            sendNotification(title, body);
//        }
//    }
//
//    private void sendNotification(String title, String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//
//        String channelId = "default_channel";
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_notification)
//                        .setContentTitle(title)
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(sound)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    channelId, "Notificações", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }
//}
