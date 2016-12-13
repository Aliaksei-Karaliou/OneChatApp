package com.github.aliakseiKaraliou.onechatapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollServer;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollUpdate;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkGetLongPollServerParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkLongPollParser;

import java.io.IOException;
import java.util.List;

public class ReceivingService extends Service {

    private VkLongPollServer longPollServer;
    private IMessage message = null;

    public ReceivingService() {
    }

    @Override
    public void onCreate() {
        Log.i("SERVICE", "created");
        super.onCreate();
        final AsyncOperation<Void, VkLongPollServer> asyncOperation = new AsyncOperation<Void, VkLongPollServer>() {
            @Override
            protected VkLongPollServer doInBackground(Void aVoid) {
                try {
                    final String request = new VkRequester().doRequest(Constants.Method.MESSAGES_GETLONGPOLLSERVER);
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
        Log.i("SERVICE", "started");
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String request = new VkRequester().doLongPollRequest(longPollServer);
                final VkLongPollUpdate update = new VkLongPollParser().parse(getApplicationContext(), request);
                final List<IEvent> events = update.getEvents();
                for (IEvent event : events) {
                    if (event instanceof IMessage) {
                        message = (IMessage) event;
                    }
                }
                longPollServer.setTs(update.getTs());
                stopSelf();
            }
        });
        thread.start();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent(Constants.Other.BROADCAST_MESSAGE_RECEIVER_NAME);
        if (message != null) {
            broadcastIntent.putExtra(Constants.Params.MESSAGE, message);
        }
        sendBroadcast(broadcastIntent);
        Log.i("SERVICE", "stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
