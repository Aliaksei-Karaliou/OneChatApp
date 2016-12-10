package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll.VkLongPollUpdate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VkLongPollParser {
    public VkLongPollUpdate parse(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            long ts = jsonObject.getLong(Constants.Json.TS);
            JSONArray array = jsonObject.getJSONArray(Constants.Json.UPDATES);
            final List<List<String>> result = new ArrayList<>();
            List<String> currentResult = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONArray currentArray = array.getJSONArray(i);
                for (int j = 0; j < currentArray.length(); j++) {
                    currentResult.add(currentArray.getString(j));
                }
                result.add(currentResult);
                currentResult = new ArrayList<>();
            }
            return new VkLongPollUpdate(ts, result);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
