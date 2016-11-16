package com.github.AliakseiKaraliou.onechatapp.logic.vk;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.github.AliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.AliakseiKaraliou.onechatapp.logic.common.MessageSender;

public class VkMessageSender extends MessageSender {

    @Override
    public boolean send(@NonNull ISender sender, @NonNull String message) {
        Pair<String, String> peerId = new Pair<>("peer_id", Long.toString(sender.getId()));
        Pair<String, String> messagePair = new Pair<>("message", message);
        final String messageSendRequest = new VkRequester("messages.send", peerId, messagePair).execute(null);
        return true;
    }

}
