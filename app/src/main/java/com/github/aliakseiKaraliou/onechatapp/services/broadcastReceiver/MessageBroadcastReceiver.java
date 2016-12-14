package com.github.aliakseiKaraliou.onechatapp.services.broadcastReceiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.db.ORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.MessageModel;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.SimpleImageLoader;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.services.ReceivingService;
import com.github.aliakseiKaraliou.onechatapp.services.notifications.SimpleNotificationManager;
import com.github.aliakseiKaraliou.onechatapp.ui.activities.DialogActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessageBroadcastReceiver extends BroadcastReceiver {

    @Override
    public synchronized void onReceive(final Context context, Intent intent) {

        final ArrayList<Parcelable> parcelableArrayListExtra = intent.getParcelableArrayListExtra(Constants.Params.MESSAGE);
        final List<IMessage> messageList = new ArrayList<>();
        if (parcelableArrayListExtra != null) {
            for (Parcelable parcelable : parcelableArrayListExtra) {
                if (parcelable instanceof IMessage) {
                    messageList.add((IMessage) parcelable);
                }
            }
            if (VkInfo.isUserAuthorized()) {
                context.startService(new Intent(context, ReceivingService.class));
            }
            if (messageList.size() > 0) {

                Intent newMessageIntent = new Intent(Constants.Other.BROADCAST_NEW_MESSAGE_RECEIVER_NAME);
                newMessageIntent.putParcelableArrayListExtra(Constants.Params.MESSAGE, (ArrayList<? extends Parcelable>) messageList);
                context.sendBroadcast(newMessageIntent);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ORM messageORM = ((App) context.getApplicationContext()).getMessageORM();
                        messageORM.insertAll(Constants.Db.ALL_MESSAGES, MessageModel.convertTo(messageList));
                    }
                });

                final IMessage message = messageList.get(0);

                if (message != null) {
                    if (!message.isOut()) {
                        final String photoUrl = message.getReceiver().getPhoto100Url();
                        final AsyncOperation<String, Bitmap> bigIconLoading = new SimpleImageLoader().startLoading(photoUrl);
                        final SimpleNotificationManager notificationManager = ((App) context.getApplicationContext()).getNotificationManager();
                        Intent notificationIntent = new Intent(context, DialogActivity.class);
                        notificationIntent.putExtra(Constants.Other.PEER_ID, message.getReceiver().getId());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);
                        final String statusBarText;
                        if (message.getChat() == null) {
                            statusBarText = String.format(Locale.US, "%s: %s", message.getReceiver().getName(), message.getText());
                        } else {
                            statusBarText = String.format(Locale.US, "%s (%s): %s", message.getChat().getName(), message.getSender().getName(), message.getText());
                        }

                        final Bitmap result = bigIconLoading.getResult();
                        notificationManager.send(message.getReceiver().getName(), message.getText(), result, statusBarText, R.drawable.ic_mail, pendingIntent);
                    }
                }
            }
        }
    }
}
