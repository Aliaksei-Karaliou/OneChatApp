package com.example.korol.onechatapp.logic.vk;

import android.util.Pair;

import com.example.korol.onechatapp.logic.common.IMessageSender;
import com.example.korol.onechatapp.logic.common.ISender;

import java.util.concurrent.ExecutionException;

public class VkMessageSender implements IMessageSender {

    @Override
    public boolean send(ISender sender, String message) {
        try {
            final String messageSendRequest = new VkRequester("messages.send", new Pair<String, String>("peer_id", Long.toString(sender.getId())), new Pair<String, String>("message", message)).execute(null);
            final StringBuilder builder = new StringBuilder();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
