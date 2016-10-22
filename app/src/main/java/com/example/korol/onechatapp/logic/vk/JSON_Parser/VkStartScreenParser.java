package com.example.korol.onechatapp.logic.vk.JSON_Parser;

import android.os.AsyncTask;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.IUser;
import com.example.korol.onechatapp.logic.vk.VkIdToUserStorage;
import com.example.korol.onechatapp.logic.vk.VkMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VkStartScreenParser extends AsyncTask<Void,Void,List<IMessage>> {
    private List<IMessage> parse(String JSONString) {
        List<IMessage> result = new ArrayList<>();
        try {
            List<Integer> userIdList = new ArrayList<>();
            JSONObject jObject = new JSONObject(JSONString);
            JSONArray allMessages = jObject.getJSONArray("response");
            for (int i = 1; i < allMessages.length(); i++) {
                JSONObject oneObject = allMessages.getJSONObject(i);
                userIdList.add(oneObject.getInt("uid"));
            }
            List<IUser> list = VkIdToUserStorage.getUsers(userIdList);
            for (int i = 1; i < allMessages.length(); i++) {
                JSONObject oneObject = allMessages.getJSONObject(i);
                VkMessage.Builder builder = new VkMessage.Builder().setText(oneObject.getString("body")).setSender(list.get(i - 1));
                result.add(builder.build());
            }
            StringBuilder builder = new StringBuilder();
        } catch (JSONException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    @Override
    protected List<IMessage> doInBackground(Void... params) {
        return parse(json);
    }

    public VkStartScreenParser(String json) {
        this.json = json;
    }

    private String json;
}
