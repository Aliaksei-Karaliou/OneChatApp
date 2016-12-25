package com.github.aliakseiKaraliou.onechatapp.broadcastReceivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.SimpleImageLoader;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.events.VkAddNewMessageEvent;
import com.github.aliakseiKaraliou.onechatapp.ui.activities.DialogActivity;

import java.util.List;
import java.util.Locale;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private static final String TITLE_TEMPLATE = "%s (%s)";
    private static final byte NEW_MESSAGE_ID = 4;
    private static final String TICKER_TEMPLATE = "%s: %s";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final List<IEvent> eventList = intent.getParcelableArrayListExtra(Constants.Other.EVENT_LIST);

        IMessage message = null;
        for (final IEvent event : eventList) {
            if (event instanceof VkAddNewMessageEvent) {
                final IMessage newMessage = ((VkAddNewMessageEvent) event).getMessage();
                if (!newMessage.isOut()) {
                    message = newMessage;
                }
            }
        }

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

        //Message
        if (message != null) {

            final SimpleImageLoader simpleImageLoader = new SimpleImageLoader();

            simpleImageLoader.startLoading(message.getSender().getPhoto100Url());
            final String title;
            if (message.getChat() == null) {
                title = message.getSender().getName();
            } else {
                title = String.format(Locale.US, TITLE_TEMPLATE, message.getSender().getName(), message.getChat().getName());
            }
            final String ticker = String.format(Locale.US, TICKER_TEMPLATE, title, message.getText());

            final Intent openActivityIntent = new Intent(context, DialogActivity.class);
            openActivityIntent.putExtra(Constants.Other.PEER_ID, message.getReceiver().getId());
            final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openActivityIntent, 0);

            final Bitmap receiverPhoto = simpleImageLoader.getResult();

            notificationBuilder
                    .setLargeIcon(receiverPhoto)
                    .setSmallIcon(R.drawable.ic_mail)
                    .setContentTitle(title)
                    .setContentText(message.getText())
                    .setTicker(ticker)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message.getText()))
                    .setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT <= 20){
                notificationBuilder.setSmallIcon(R.drawable.mail);
            }
            notificationManager.notify(NEW_MESSAGE_ID, notificationBuilder.build());
        }
    }
}
