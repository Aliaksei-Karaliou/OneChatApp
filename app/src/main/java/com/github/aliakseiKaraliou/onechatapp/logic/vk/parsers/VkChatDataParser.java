package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkChat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VkChatDataParser implements IParser<String, LongSparseArray<IChat>> {

    private static final String DEFAULT_CHAT_PHOTO50_URL = "https://vk.com/images/community_50.png";
    private static final String DEFAULT_CHAT_PHOTO100_URL = "https://vk.com/images/community_100.png";

    public LongSparseArray<IChat> parse(String chatResponse) {
        try {
            LongSparseArray<IChat> array = new LongSparseArray<>();
            JSONArray jsonArray = new JSONObject(chatResponse).getJSONArray(Constants.Json.RESPONSE);
            VkIdConverter converter = new VkIdConverter();
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject currentObject = jsonArray.getJSONObject(i);
                Long id = converter.chatToPeer(currentObject.getLong(Constants.Json.ID));
                String title = currentObject.getString(Constants.Json.TITLE);
                String photo50, photo100;
                if (currentObject.has(Constants.Json.PHOTO_50))
                    photo50 = currentObject.getString(Constants.Json.PHOTO_50);
                else {
                    photo50 = DEFAULT_CHAT_PHOTO50_URL;
                }
                if (currentObject.has(Constants.Json.PHOTO_100))
                    photo100 = currentObject.getString(Constants.Json.PHOTO_100);
                else {
                    photo100 = DEFAULT_CHAT_PHOTO100_URL;
                }
                assert id != null;
                VkChat chat = new VkChat(id, title, photo50, photo100);
                array.put(id, chat);
            }
            return array;
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}