package com.github.AliakseiKaraliou.onechatapp.logic.vk.getMethods;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.json.VkAllMessagesJsonParser;

import java.util.List;

public class VkGetAllMessages {
    public static List<IMessage> getStartScren() {
        new VkAllMessagesJsonParser().execute(new VkRequester("messages.get").execute(null));
        return null;
    }
}
