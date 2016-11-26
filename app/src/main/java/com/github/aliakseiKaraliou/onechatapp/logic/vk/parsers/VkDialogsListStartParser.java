package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;

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
            JSONArray items = new JSONObject(jsonString).getJSONObject(VkConstants.Json.RESPONSE).getJSONArray(VkConstants.Json.ITEMS);
            JSONObject currentObject;
            final VkIdConverter vkIdConverter = new VkIdConverter();
            for (int i = 0; i < items.length(); i++) {
                currentObject = items.getJSONObject(i).getJSONObject(VkConstants.Json.MESSAGE);
                if (currentObject.has(VkConstants.Json.CHAT_ID)) {
                    set.add(vkIdConverter.chatToPeer(currentObject.getLong(VkConstants.Json.CHAT_ID)));
                }
                set.add(currentObject.getLong(VkConstants.Json.USER_ID));
                if (currentObject.has(VkConstants.Json.ACTION_MID)) {
                    set.add(currentObject.getLong(VkConstants.Json.ACTION_MID));
                }
            }
            return set;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}