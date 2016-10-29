package com.example.korol.onechatapp.logic.vk.getMethods;

import android.util.Pair;

import com.example.korol.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.vk.JSON_Parsers.VkStartScreenParser;
import com.example.korol.onechatapp.logic.vk.VkRequester;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetStartScreen {
    public static List<IMessage> getStartScreen() throws AccessTokenException {
        //final VkRequester requester = new VkRequester("messages.getDialogs", new Pair<String, String>("count", "200"));
        final VkRequester requester = new VkRequester("messages.getDialogs", new Pair<String, String>("preview_length", "30"));
        try {
            String response = requester.execute(null);
            if (response.contains("User authorization failed: no access_token passed."))
                throw new AccessTokenException();
            else if (!response.equals("Error request"))
                return (new VkStartScreenParser(response)).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
