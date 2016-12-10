package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollServer;

import org.json.JSONException;
import org.json.JSONObject;

public class VkGetLongPollServerParser {
    public VkLongPollServer parse(String json) {
        try {
            JSONObject object = new JSONObject(json).getJSONObject(Constants.Json.RESPONSE);
            final String key = object.getString(Constants.Json.KEY);
            final String server = object.getString(Constants.Json.SERVER);
            final long ts = object.getLong(Constants.Json.TS);
            return new VkLongPollServer(key, server, ts);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
