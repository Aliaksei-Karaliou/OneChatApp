package com.example.korol.onechatapp.logic.vk.JSON_Parser;

import android.util.JsonToken;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.vk.VkMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.message;

public class VkStartScreenParser implements IJSONParser<List<IMessage>> {
    @Override
    public List<IMessage> parse(String JSONString) {
        List<IMessage> result = new ArrayList<>();
        try {
            result = new ArrayList<>();
            List<Integer> userList = new ArrayList<>();
            JSONObject jObject = new JSONObject(JSONString);
            JSONArray allMessages = jObject.getJSONArray("response");
            for (int i = 1; i < allMessages.length(); i++) {
                JSONObject oneObject = allMessages.getJSONObject(i);
                VkMessage.Builder builder = new VkMessage.Builder().setText(oneObject.getString("body"));
                result.add(builder.build());
            }
            StringBuilder builder = new StringBuilder();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
