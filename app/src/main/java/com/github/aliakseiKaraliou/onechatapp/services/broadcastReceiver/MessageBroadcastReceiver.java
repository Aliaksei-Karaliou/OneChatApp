package com.github.aliakseiKaraliou.onechatapp.services.broadcastReceiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.db.ORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.MessageModel;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.services.ReceivingService;
import com.github.aliakseiKaraliou.onechatapp.services.notifications.SimpleNotificationManager;
import com.github.aliakseiKaraliou.onechatapp.ui.activities.DialogActivity;

import java.util.Locale;

public class MessageBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        final IMessage message = intent.getParcelableExtra(Constants.Params.MESSAGE);
        if (message != null) {
            if (!message.isOut()) {
                final SimpleNotificationManager notificationManager = ((App) context.getApplicationContext()).getNotificationManager();
                Intent notificationIntent = new Intent(context, DialogActivity.class);
                notificationIntent.putExtra(Constants.Other.PEER_ID, message.getReceiver().getId());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);
                String statusBarText = String.format(Locale.US, "(%s) %s", message.getReceiver().getName(), message.getText());
                notificationManager.send(message.getReceiver().getName(), message.getText(), R.drawable.ic_vk_social_network_logo, statusBarText, pendingIntent);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final ORM messageORM = ((App) context.getApplicationContext()).getMessageORM();
                    messageORM.insert(Constants.Db.ALL_MESSAGES, MessageModel.getInstance());
                }
            });
        }
        if (VkInfo.isUserAuthorized()) {
            context.startService(new Intent(context, ReceivingService.class));
        }
    }
}
