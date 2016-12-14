package com.github.aliakseiKaraliou.onechatapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollServer;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollUpdate;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkLongPollParser;

import java.util.ArrayList;
import java.util.List;

public class ReceivingService extends Service {

    private VkLongPollServer longPollServer;
    private List<IEvent> events=new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                longPollServer = ((App) getApplicationContext()).getLongPollServer();
                final String request = new VkRequester().doLongPollRequest(longPollServer);
                final VkLongPollUpdate update = new VkLongPollParser().parse(getApplicationContext(), request);
                events = update.getEvents();
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
        Intent broadcastIntent = new Intent(Constants.Other.BROADCAST_EVENT_RECEIVER_NAME);
        broadcastIntent.putParcelableArrayListExtra(Constants.Params.MESSAGE, (ArrayList<? extends Parcelable>) events);
        events = new ArrayList<>();
        sendBroadcast(broadcastIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
