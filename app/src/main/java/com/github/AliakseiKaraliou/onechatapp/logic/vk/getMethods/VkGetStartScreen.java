package com.github.AliakseiKaraliou.onechatapp.logic.vk.getMethods;

import android.content.Context;
import android.util.Pair;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.utils.exceptions.AccessTokenException;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.json.VkStartScreenJsonParser;

import java.util.List;

public class VkGetStartScreen {

    public static List<IMessage> getStartScreen(Context context) throws AccessTokenException {
        return getStartScreen(context, 0);
    }

    public static List<IMessage> getStartScreen(Context context, int offset) throws AccessTokenException {
        //final VkRequester requester = new VkRequester("messages.getDialogs", new Pair<String, String>("count", "200"));
        final VkRequester requester = new VkRequester("messages.getDialogs", new Pair<String, String>("preview_length", "30"), new Pair<String, String>("offset", Integer.toString(offset)));
        String response = requester.execute(null);
        if (response.contains("error"))
            throw new AccessTokenException();
        else if (!response.equals("Error request")){
            final List<IMessage> execute = new VkStartScreenJsonParser(context).execute(response);
                return execute;
            }
        return null;
    }
}
