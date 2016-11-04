package com.example.korol.onechatapp.logic.vk.getMethods;

import android.util.Pair;

import com.example.korol.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.vk.json.VkStartScreenJsonParser;
import com.example.korol.onechatapp.logic.vk.VkRequester;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VkGetStartScreen {

    public static List<IMessage> getStartScreen() throws AccessTokenException {
        return getStartScreen(0);
    }

    public static List<IMessage> getStartScreen(int offset) throws AccessTokenException {
        //final VkRequester requester = new VkRequester("messages.getDialogs", new Pair<String, String>("count", "200"));
        final VkRequester requester = new VkRequester("messages.getDialogs", new Pair<String, String>("preview_length", "30"), new Pair<String, String>("offset", "0"));
        try {
            String response = requester.execute(null);
            if (response.contains("error"))
                throw new AccessTokenException();
            else if (!response.equals("Error request")){
                final List<IMessage> execute = new VkStartScreenJsonParser().execute(response);
                return execute;
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
