package com.example.korol.onechatapp.logic.vk.JSON_Parser;

import com.example.korol.onechatapp.logic.common.IMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VkStartScreenParser implements IJSONParser<List<IMessage>> {
    @Override
    public List<IMessage> parse(String JSONString) {
        List<IMessage> result = new ArrayList<>();
        try {
            result = new ArrayList<>();
            List<Integer> userIdList = new ArrayList<>();
            JSONObject jObject = new JSONObject(JSONString);
            JSONArray allMessages = jObject.getJSONArray("response");
            for (int i = 1; i < allMessages.length(); i++) {
                JSONObject oneObject = allMessages.getJSONObject(i);
                userIdList.add(oneObject.getInt("id"));
            }
            StringBuilder builder = new StringBuilder();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
