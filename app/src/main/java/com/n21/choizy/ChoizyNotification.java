package com.n21.choizy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ChoizyNotification extends FirebaseMessagingService {



    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        System.out.println(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
       // super.onMessageReceived(remoteMessage);
        String body = remoteMessage.getNotification().getBody();
        String title = remoteMessage.getNotification().getTitle();
        String C_ID = "Choizy notification";
        NotificationChannel channel = new NotificationChannel(C_ID,"Choizy", NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        Notification.Builder notification = new Notification.Builder(this,C_ID).setContentTitle(title).setContentText(body).setSmallIcon(R.mipmap.ic_launcher);

        NotificationManagerCompat.from(this).notify(2,notification.build());

    }
}
