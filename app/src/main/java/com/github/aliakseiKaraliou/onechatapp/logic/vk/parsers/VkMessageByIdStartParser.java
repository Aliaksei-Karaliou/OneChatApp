package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class VkMessageByIdStartParser implements IParser<String, Set<Long>> {


    @Nullable
    @Override
    public Set<Long> parse(String s) {
        try {
            final VkIdConverter vkIdConverter = new VkIdConverter();
            final Set<Long> idSet = new HashSet<>();
            final JSONArray items = new JSONObject(s).getJSONObject(Constants.Json.RESPONSE).getJSONArray(Constants.Json.ITEMS);
            for (int i = 0; i < items.length(); i++) {
                final JSONObject currentObject = items.getJSONObject(i);
                final long userId = currentObject.getLong(Constants.Json.USER_ID);
                final IReceiver receiver = VkReceiverStorage.get(userId);
                if (receiver == null) {
                    idSet.add(userId);
                }
                if (currentObject.has(Constants.Json.CHAT_ID)) {
                    final long chatId = currentObject.getLong(Constants.Json.CHAT_ID);
                    final IChat chat = ((IChat) VkReceiverStorage.get(chatId));
                    if (chat == null) {
                        idSet.add(vkIdConverter.chatToPeer(chatId));
                    }
                }
            }
            return idSet;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
