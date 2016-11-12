package com.example.korol.onechatapp.logic.vk.json;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.IUser;
import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.example.korol.onechatapp.logic.vk.entities.VkMessage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToChatStorage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToGroupStorage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToUserStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VkStartScreenJsonParser extends AsyncOperation<String, List<IMessage>> {

    @Override
    protected List<IMessage> doInBackground(String json) {
        List<IMessage> result = new ArrayList<>();
        try {
            List<Long> userIdList = new ArrayList<>();
            List<Long> chatIdList = new ArrayList<>();
            List<Long> groupIdList = new ArrayList<>();
            JSONArray allMessages = new JSONObject(json).getJSONObject("response").getJSONArray("items");
            for (int i = 0; i < allMessages.length(); i++) {
                final JSONObject oneObject = allMessages.getJSONObject(i).getJSONObject("message");
                if (oneObject.getLong("user_id") > 0 && oneObject.getString("title").equals(" ... "))
                    userIdList.add(oneObject.getLong("user_id"));
                else if (oneObject.getLong("user_id") < 0)
                    groupIdList.add(oneObject.getLong("user_id"));
                else
                    chatIdList.add(oneObject.getLong("chat_id") + 2000000000);
            }
            VkIdToUserStorage.getUsers(userIdList);
            VkIdToChatStorage.getChats(chatIdList);
            VkIdToGroupStorage.getGroups(groupIdList);
            for (int i = 0; i < allMessages.length(); i++) {
                JSONObject oneObject = allMessages.getJSONObject(i).getJSONObject("message");
                VkMessage.Builder builder = new VkMessage.Builder().setText(oneObject.getString("body")).setDate(new Date(oneObject.getLong("date") * 1000)).setRead(oneObject.getInt("read_state") != 0).setId(oneObject.getLong("id"));
                if (oneObject.getLong("user_id") > 0 && oneObject.getString("title").equals(" ... "))
                    builder.setSender(VkIdToUserStorage.getUser(oneObject.getLong("user_id")));
                else if (oneObject.getLong("user_id") < 0)
                    builder.setSender(VkIdToGroupStorage.getGroup(oneObject.getLong("user_id")));
                else {
                    builder.setSender(VkIdToChatStorage.getChat(oneObject.getLong("chat_id") + 2000000000));
                    if (oneObject.has("action")) {
                        if (oneObject.getString("action").equals("chat_invite_user")) {
                            final IUser actioning = VkIdToUserStorage.getUser(oneObject.getLong("action_mid"));
                            builder.setText(String.format(Locale.getDefault(), "%s was invited to this chat", actioning.getName()));
                        } else if (oneObject.getString("action").equals("chat_kick_user")) {
                            final IUser actioning = VkIdToUserStorage.getUser(oneObject.getLong("action_mid"));
                            builder.setText(String.format(Locale.getDefault(), "%s was removed from this chat", actioning.getName()));
                        }
                    }
                }
                result.add(builder.build());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
