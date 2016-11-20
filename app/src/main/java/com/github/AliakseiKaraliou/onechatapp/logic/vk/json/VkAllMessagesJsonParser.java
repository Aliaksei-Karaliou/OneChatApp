package com.github.AliakseiKaraliou.onechatapp.logic.vk.json;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.entities.VkMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.storages.VkIdToChatStorage;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.storages.VkIdToGroupStorage;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.storages.VkIdToUserStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VkAllMessagesJsonParser extends AsyncOperation<String, List<IMessage>> {

    @Override
    protected List<IMessage> doInBackground(String json) {
        try {
            final JSONArray items = new JSONObject(json).getJSONObject("response").getJSONArray("items");
            final Set<Long> userIdSet = new HashSet<>();
            final Set<Long> chatIdSet = new HashSet<>();
            final Set<Long> groupIdSet = new HashSet<>();
            JSONObject currentJSONObject;
            long userId;
            boolean isChat;
            for (int i = 0; i < items.length(); i++) {
                currentJSONObject = items.getJSONObject(i);
                userId = currentJSONObject.getLong("user_id");
                isChat = currentJSONObject.has("chat_id");
                if (userId > 0 && !isChat)
                    userIdSet.add(userId);
                else if (isChat)
                    chatIdSet.add(currentJSONObject.getLong("chat_id") + 2000000000);
                else if (userId < 0)
                    groupIdSet.add(userId);
            }

            if (userIdSet.size() > 0)
                VkIdToUserStorage.getUsers(new ArrayList<>(userIdSet));
            if (chatIdSet.size() > 0)
                VkIdToChatStorage.getChats(new ArrayList<>(chatIdSet));
            if (groupIdSet.size() > 0)
                VkIdToGroupStorage.getGroups(new ArrayList<>(groupIdSet));

            VkMessage.Builder builder=new VkMessage.Builder();
            for (int i = 0; i < items.length(); i++) {
                currentJSONObject = items.getJSONObject(i);
                userId = currentJSONObject.getLong("user_id");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
