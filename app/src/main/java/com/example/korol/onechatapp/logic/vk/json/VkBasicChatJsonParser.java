package com.example.korol.onechatapp.logic.vk.json;

import android.support.annotation.Nullable;

import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.example.korol.onechatapp.logic.vk.entities.VkChat;

import org.json.JSONException;
import org.json.JSONObject;

public class VkBasicChatJsonParser extends AsyncOperation<String, VkChat> {

    public static final String DEFAULT_CHAT_PHOTO_URL = "http://vk.com/images/camera_c.gif";

    @Nullable
    @Override
    protected VkChat doInBackground(String json) {
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(json);
            JSONObject allMessages = jObject.getJSONObject("response");
            String url;
            try {
                url = allMessages.getString("photo_50");
            } catch (Exception e) {
                url = DEFAULT_CHAT_PHOTO_URL;
            }
            return new VkChat(allMessages.getInt("chat_id"), allMessages.getString("title"), url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
