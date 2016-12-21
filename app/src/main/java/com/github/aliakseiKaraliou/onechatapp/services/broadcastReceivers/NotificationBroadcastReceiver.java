package com.github.aliakseiKaraliou.onechatapp.services.broadcastReceivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;

import java.util.List;
import java.util.Locale;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private byte NEW_MESSAGE_ID = 4;

    @Override
    public void onReceive(Context context, Intent intent) {
        final List<IEvent> eventList = intent.getParcelableArrayListExtra(Constants.Other.EVENT_LIST);

        IMessage message = null;
        for (IEvent event : eventList) {
            if (event instanceof IMessage && !((IMessage) event).isOut()) {
                message = (IMessage) event;
            }
        }

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

        //Message
        if (message != null) {

            final String title;
            if (message.getChat() == null) {
                title = message.getSender().getName();
            } else {
                title = String.format(Locale.US, "%s (%s)", message.getSender().getName(), message.getChat().getName());
            }
            final String ticker = String.format(Locale.US, "%s: %s", title, message.getText());

            notificationBuilder.setSmallIcon(R.drawable.ic_mail)
                    .setContentTitle(title)
                    .setContentText(message.getText())
                    .setTicker(ticker)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message.getText()));

            notificationManager.notify(NEW_MESSAGE_ID, notificationBuilder.build());
        }
    }
}
