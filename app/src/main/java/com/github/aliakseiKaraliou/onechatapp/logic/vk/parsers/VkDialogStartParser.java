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

public class VkDialogStartParser implements IParser<String, Set<Long>> {
    @Nullable
    @Override
    public Set<Long> parse(String jsonString) {
        try {
            Set<Long> set = new HashSet<>();
            JSONArray items = new JSONObject(jsonString).getJSONObject(VkConstants.Json.RESPONSE).getJSONArray(VkConstants.Json.ITEMS);
            JSONObject currentObject;
            final VkIdConverter vkIdConverter = new VkIdConverter();
            for (int i = 0; i < items.length(); i++) {
                currentObject = items.getJSONObject(i);
                final long aLong = currentObject.getLong(VkConstants.Json.FROM_ID);
                set.add(aLong);
            }
            return set;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}