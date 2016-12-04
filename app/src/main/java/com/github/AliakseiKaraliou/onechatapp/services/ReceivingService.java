package com.github.aliakseiKaraliou.onechatapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollServer;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollUpdate;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkGetLongPollServerParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkLongPollParser;
import com.github.aliakseiKaraliou.onechatapp.services.notifications.SimpleNotificationManager;

import java.io.IOException;
import java.util.List;

public class ReceivingService extends Service {

    private VkLongPollServer longPollServer;

    public ReceivingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AsyncOperation<Void, VkLongPollServer> asyncOperation = new AsyncOperation<Void, VkLongPollServer>() {
            @Override
            protected VkLongPollServer doInBackground(Void aVoid) {
                try {
                    final String request = new VkRequester().doRequest("messages.getLongPollServer");
                    return new VkGetLongPollServerParser().parse(request);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        asyncOperation.startLoading(null);
        longPollServer = asyncOperation.getResult();
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        infinityLoop();
        return START_STICKY;
    }

    private void infinityLoop() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String request = new VkRequester().doLongPollRequest(longPollServer);
                    final VkLongPollUpdate update = new VkLongPollParser().parse(request);
                    final List<IEvent> events = update.getEvents();
                    IMessage message = null;
                    for (IEvent event : events) {
                        if (event instanceof IMessage) {
                            message = (IMessage) event;
                        }
                    }
                    if (message != null) {
                        final SimpleNotificationManager notificationManager = ((App) getApplicationContext()).getNotificationManager();
                        notificationManager.send(message.getReciever().getName(), message.getText(), R.drawable.ic_mail, 1);
                    }
                    longPollServer.setTs(update.getTs());
                }
            }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
