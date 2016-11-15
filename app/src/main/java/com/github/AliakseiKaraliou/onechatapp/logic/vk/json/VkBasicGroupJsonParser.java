package com.github.AliakseiKaraliou.onechatapp.logic.vk.json;

import android.support.annotation.Nullable;

import com.github.AliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.entities.VkGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VkBasicGroupJsonParser extends AsyncOperation<String, Map<Long, VkGroup>> {
    @Nullable
    @Override
    protected Map<Long, VkGroup> doInBackground(String json) {
        try {
            Map<Long, VkGroup> result = new HashMap<>();
            JSONArray response = new JSONObject(json).getJSONArray("response");
            for (int i = 0; i < response.length(); i++) {
                final JSONObject current = response.getJSONObject(i);
                result.put(current.getLong("id"), new VkGroup(-current.getLong("id"), current.getString("name"), current.getString("photo_50")));
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
