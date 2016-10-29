package com.example.korol.onechatapp.logic.vk.JSON_Parsers;

import android.util.Pair;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.example.korol.onechatapp.logic.vk.VkChat;
import com.example.korol.onechatapp.logic.vk.VkMessage;
import com.example.korol.onechatapp.logic.vk.VkRequester;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToChatStorage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToUserStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VkStartScreenJsonParser extends AsyncOperation<Void, List<IMessage>> {
    private List<IMessage> parse(String JSONString) {
        List<IMessage> result = new ArrayList<>();
        try {
            List<Integer> userIdList = new ArrayList<>();
            JSONObject jObject = new JSONObject(JSONString);
            JSONArray allMessages = jObject.getJSONArray("response");
            for (int i = 1; i < allMessages.length(); i++) {
                JSONObject oneObject = allMessages.getJSONObject(i);

                if (oneObject.getString("title").equals(" ... "))
                    userIdList.add(oneObject.getInt("uid"));
                else {
                    int id = oneObject.getInt("chat_id");
                    String url = new VkRequester("messages.getChat", new Pair<String, String>("chat_id", Integer.toString(id))).execute(null);
                    VkChat chat = BasicChatJsonParser.parse(url);
                    VkIdToChatStorage.put(id, chat);
                }
            }
            VkIdToUserStorage.getUsers(userIdList);
            for (int i = 1; i < allMessages.length(); i++) {
                JSONObject oneObject = allMessages.getJSONObject(i);
                VkMessage.Builder builder = new VkMessage.Builder().setText(oneObject.getString("body")).setDate(new Date(oneObject.getLong("date") * 1000)).setRead(oneObject.getInt("read_state") != 0);
                if (oneObject.getString("title").equals(" ... "))
                    builder.setSender(VkIdToUserStorage.getUser(oneObject.getInt("uid")));
                else
                    builder.setSender(VkIdToChatStorage.get(oneObject.getInt("chat_id")));
                result.add(builder.build());
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            result = null;
        }
        StringBuilder builder = new StringBuilder();
        return result;
    }

    public VkStartScreenJsonParser(String json) {
        this.json = json;
    }

    private String json;

    @Override
    protected List<IMessage> doInBackground(Void aVoid) {
        return parse(json);
    }
}
