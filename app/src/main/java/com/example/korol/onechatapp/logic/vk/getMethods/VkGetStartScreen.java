package com.example.korol.onechatapp.logic.vk.getMethods;

import android.util.Pair;

import com.example.korol.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.vk.json.VkStartScreenJsonParser;
import com.example.korol.onechatapp.logic.vk.VkRequester;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VkGetStartScreen {

    private static final String NO_ACCESS_TOKEN_PASSED = "User authorization failed: no access_token passed.";
    private static final String ACCESS_TOKEN_WAS_GIVEN_TO_ANOTHER_IP_ADDRESS = "User authorization failed: access_token was given to another ip address.";
    private static final String ACCESS_TOKEN_HAS_EXPIRED = "User authorization failed: access_token has expired.";

    public static List<IMessage> getStartScreen() throws AccessTokenException {
        //final VkRequester requester = new VkRequester("messages.getDialogs", new Pair<String, String>("count", "200"));
        final VkRequester requester = new VkRequester("messages.getDialogs", new Pair<String, String>("preview_length", "30"));
        try {
            String response = requester.execute(null);
            if (response.contains(NO_ACCESS_TOKEN_PASSED) || response.contains(ACCESS_TOKEN_WAS_GIVEN_TO_ANOTHER_IP_ADDRESS) || response.contains(ACCESS_TOKEN_HAS_EXPIRED))
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
