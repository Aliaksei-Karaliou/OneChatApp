package com.github.aliakseiKaraliou.onechatapp.logic.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import java.io.IOException;

public class LongPoll extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        final AsyncOperation<Void, String> asyncOperation = new AsyncOperation<Void, String>() {
            @Override
            protected String doInBackground(Void aVoid) {
                try {
                    final String request = new VkRequester().doRequest(VkConstants.Method.MESSAGES_GETLONGPOLLSERVER);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
