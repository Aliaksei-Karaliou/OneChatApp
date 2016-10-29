package com.example.korol.onechatapp.logic.vk.JSON_Parsers;

import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.example.korol.onechatapp.logic.vk.VkChat;

import org.json.JSONException;
import org.json.JSONObject;

public class BasicChatJsonParser extends AsyncOperation<Void, VkChat> {

    private final String json;

    public static VkChat parse(String JSONString) {
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(JSONString);
            JSONObject allMessages = jObject.getJSONObject("response");
            String url;
            try {
                url = allMessages.getString("photo_50");
            } catch (Exception e) {
                url = "http://vk.com/images/camera_c.gif";
            }
            return new VkChat(allMessages.getInt("chat_id"), allMessages.getString("title"), url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected VkChat doInBackground(Void aVoid) {
        return parse(json);
    }

    public BasicChatJsonParser(String json) {
        this.json = json;
    }
}
