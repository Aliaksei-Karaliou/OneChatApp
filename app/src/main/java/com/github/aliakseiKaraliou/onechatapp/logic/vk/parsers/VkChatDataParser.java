package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.entities.VkChat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VkChatDataParser implements IParser<String, LongSparseArray<IChat>> {

    private static final String DEFAULT_CHAT_PHOTO_URL = "https://vk.com/images/community_50.png";

    public LongSparseArray<IChat> parse(String chatResponse) {
        try {
            LongSparseArray<IChat> array = new LongSparseArray<>();
            JSONArray jsonArray = new JSONObject(chatResponse).getJSONArray(VkConstants.Json.RESPONSE);
            VkIdConverter converter = new VkIdConverter();
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject currentObject = jsonArray.getJSONObject(i);
                Long id = converter.chatToPeer(currentObject.getLong(VkConstants.Json.ID));
                String title = currentObject.getString(VkConstants.Json.TITLE);
                String photoUrl;
                if (currentObject.has(VkConstants.Json.PHOTO_50))
                    photoUrl = currentObject.getString(VkConstants.Json.PHOTO_50);
                else {
                    photoUrl=DEFAULT_CHAT_PHOTO_URL;
                }
                assert id != null;
                VkChat chat = new VkChat(id, title, photoUrl);
                array.put(id, chat);
            }
            return array;
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
