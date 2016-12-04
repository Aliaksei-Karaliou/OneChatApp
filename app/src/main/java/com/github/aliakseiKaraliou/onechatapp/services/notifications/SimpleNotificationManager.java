package com.github.aliakseiKaraliou.onechatapp.services.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class SimpleNotificationManager {

    private NotificationManager notificationManager;
    private Context context;

    public SimpleNotificationManager(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void send(String title, String text, int smallIconId, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(smallIconId);
        final Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }
}
