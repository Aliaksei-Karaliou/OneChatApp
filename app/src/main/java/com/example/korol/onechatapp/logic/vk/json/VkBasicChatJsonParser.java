package com.example.korol.onechatapp.logic.vk.json;

import android.support.annotation.Nullable;

import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.example.korol.onechatapp.logic.vk.entities.VkChat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VkBasicChatJsonParser extends AsyncOperation<String, Map<Long, VkChat>> {

    public static final String DEFAULT_CHAT_PHOTO_URL = "http://vk.com/images/camera_c.gif";

    @Nullable
    @Override
    protected Map<Long, VkChat> doInBackground(String s) {
        try {
            Map<Long, VkChat> map = new HashMap<>();
            JSONArray chatsJsonArray = new JSONObject(s).getJSONArray("response");
            for (int i = 0; i < chatsJsonArray.length(); i++) {
                JSONObject currentObject = ((JSONObject) chatsJsonArray.get(i));
                String photoUrl = DEFAULT_CHAT_PHOTO_URL;
                long id = currentObject.getLong("id")+ 2000000000;
                if (currentObject.has("photo_50"))
                    photoUrl = currentObject.getString("photo_50");
                map.put(id, new VkChat(id, currentObject.getString("title"), photoUrl));
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
