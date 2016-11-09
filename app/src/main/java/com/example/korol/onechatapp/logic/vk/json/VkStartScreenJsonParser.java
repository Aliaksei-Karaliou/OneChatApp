package com.example.korol.onechatapp.logic.vk.json;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.example.korol.onechatapp.logic.vk.entities.VkMessage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToChatStorage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToUserStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VkStartScreenJsonParser extends AsyncOperation<String, List<IMessage>> {

    @Override
    protected List<IMessage> doInBackground(String json) {
        List<IMessage> result = new ArrayList<>();
        try {
            List<Long> userIdList = new ArrayList<>();
            List<Long> chatIdList = new ArrayList<>();
            JSONArray allMessages = new JSONObject(json).getJSONObject("response").getJSONArray("items");
            for (int i = 0; i < allMessages.length(); i++) {
                final JSONObject oneObject = allMessages.getJSONObject(i).getJSONObject("message");
                if (oneObject.getString("title").equals(" ... "))
                    userIdList.add(oneObject.getLong("user_id"));
                else {
                    chatIdList.add(oneObject.getLong("chat_id") + 2000000000);
                }
            }
            VkIdToUserStorage.getUsers(userIdList);
            VkIdToChatStorage.getChats(chatIdList);
            for (int i = 0; i < allMessages.length(); i++) {
                JSONObject oneObject = allMessages.getJSONObject(i).getJSONObject("message");
                VkMessage.Builder builder = new VkMessage.Builder().setText(oneObject.getString("body")).setDate(new Date(oneObject.getLong("date") * 1000)).setRead(oneObject.getInt("read_state") != 0);
                if (oneObject.getString("title").equals(" ... "))
                    builder.setSender(VkIdToUserStorage.getUser(oneObject.getLong("user_id")));
                else
                    builder.setSender(VkIdToChatStorage.getChat(oneObject.getLong("chat_id") + 2000000000));
                result.add(builder.build());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
