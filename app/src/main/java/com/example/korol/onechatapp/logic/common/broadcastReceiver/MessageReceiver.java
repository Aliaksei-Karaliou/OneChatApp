package com.example.korol.onechatapp.logic.common.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Intent messageReceiver = new Intent(context, IncomingMessageEvent.class);
        context.startService(messageReceiver);
    }
}
