package com.github.aliakseiKaraliou.onechatapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollServer;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkGetLongPollServerParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkLongPollParser;

import java.io.IOException;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        final AsyncOperation<VkLongPollServer, String> asyncOperation = new AsyncOperation<VkLongPollServer, String>() {
            @Override
            protected String doInBackground(VkLongPollServer vkLongPollServer) {
                //final String request = new VkRequester().doLongPollRequest(longPollServer);
                final String request = "{\"ts\":1866813405,\"updates\":[[4,272018,1,127804881,1480870706,\" ... \",\"нпап\",{}],[80,1,0],[7,127804881,272017]]}";
                new VkLongPollParser().parse(request);
                return null;
            }
        };
        asyncOperation.startLoading(longPollServer);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
