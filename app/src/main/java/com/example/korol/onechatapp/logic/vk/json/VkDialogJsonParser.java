package com.example.korol.onechatapp.logic.vk.json;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.example.korol.onechatapp.logic.vk.entities.VkMessage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToUserStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VkDialogJsonParser extends AsyncOperation<String, List<IMessage>> {

    @Override
    protected List<IMessage> doInBackground(String json) throws JSONException {
        List<IMessage> list = new ArrayList<>();
        JSONArray allMessages = new JSONObject(json).getJSONArray("response");
        for (int i = 1; i < allMessages.length(); i++) {
            JSONObject jsonMessageObject = allMessages.getJSONObject(i);
            VkMessage.Builder builder = new VkMessage.Builder().setText(jsonMessageObject.getString("body")).setSender(VkIdToUserStorage.getUser(jsonMessageObject.getInt("from_id"))).setDate(new Date(jsonMessageObject.getLong("date") * 1000));
            list.add(builder.build());
        }
        return list;
    }
}
