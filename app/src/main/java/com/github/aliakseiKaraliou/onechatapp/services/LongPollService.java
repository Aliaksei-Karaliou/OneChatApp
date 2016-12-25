package com.github.aliakseiKaraliou.onechatapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollServer;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollUpdate;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkGetLongPollServerParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers.VkLongPollParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LongPollService extends IntentService {

    private VkLongPollServer longPollServer;

    public LongPollService() {
        super(Constants.Other.EVENT_SERVICE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String request = new VkRequester().doRequest(Constants.Method.MESSAGES_GETLONGPOLLSERVER);
                    longPollServer = new VkGetLongPollServerParser().parse(request);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        //waits for longPollServer initialize
        while (longPollServer == null) ;


        while (true) {
            final String longPollRequest = new VkRequester().doLongPollRequest(longPollServer);
            final VkLongPollUpdate parse = new VkLongPollParser().parse(getApplicationContext(), longPollRequest);
            final List<IEvent> events = parse.getEvents();
            if (events.size() > 0) {
                final Intent broadcastIntent = new Intent(Constants.Other.BROADCAST_EVENT_RECEIVER_NAME);
                broadcastIntent.putParcelableArrayListExtra(Constants.Other.EVENT_LIST, (ArrayList<? extends Parcelable>) events);
                sendBroadcast(broadcastIntent);
            }
            final long newTs = parse.getTs();
            longPollServer.setTs(newTs);
        }
    }


}
