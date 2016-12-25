package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;

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

public class VkDialogsListStartParser implements IParser<String, Set<Long>> {
    @Nullable
    @Override
    public Set<Long> parse(String jsonString) {
        try {
            Set<Long> set = new HashSet<>();
            JSONArray items = new JSONObject(jsonString).getJSONObject(Constants.Json.RESPONSE).getJSONArray(Constants.Json.ITEMS);
            JSONObject currentObject;
            final VkIdConverter vkIdConverter = new VkIdConverter();
            for (int i = 0; i < items.length(); i++) {
                currentObject = items.getJSONObject(i).getJSONObject(Constants.Json.MESSAGE);
                if (currentObject.has(Constants.Json.CHAT_ID)) {
                    final Long peerChat = vkIdConverter.chatToPeer(currentObject.getLong(Constants.Json.CHAT_ID));
                    final IReceiver chatReceiver = VkReceiverStorage.get(peerChat);
                    if (chatReceiver == null) {
                        set.add(peerChat);
                    }
                }
                final long peerUser = currentObject.getLong(Constants.Json.USER_ID);
                final IReceiver userReceiver = VkReceiverStorage.get(peerUser);
                if (userReceiver == null)
                    set.add(peerUser);
                if (currentObject.has(Constants.Json.ACTION_MID)) {
                    final long peerAction = currentObject.getLong(Constants.Json.ACTION_MID);
                    set.add(peerAction);
                }
            }
            return set;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}