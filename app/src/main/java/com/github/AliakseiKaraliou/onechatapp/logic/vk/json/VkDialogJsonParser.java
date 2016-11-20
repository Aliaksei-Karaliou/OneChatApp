package com.github.AliakseiKaraliou.onechatapp.logic.vk.json;

import android.content.Context;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.AliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkChatAction;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.entities.VkMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.storages.VkIdToGroupStorage;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.storages.VkIdToUserStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VkDialogJsonParser extends AsyncOperation<String, List<IMessage>> {

    private ReceiverType receiverType;

    public VkDialogJsonParser(Context context, ReceiverType receiverType) {
        this.context = context;
        this.receiverType = receiverType;
    }

    private Context context;
    
    @Override
    protected List<IMessage> doInBackground(String json) {
        try {
            List<IMessage> list = new ArrayList<>();
            JSONArray allMessages = new JSONObject(json).getJSONObject("response").getJSONArray("items");
            if (receiverType == ReceiverType.CHAT) {
                List<Long> idList = new ArrayList<>();
                for (int i = 0; i < allMessages.length(); i++) {
                    JSONObject jsonMessageObject = allMessages.getJSONObject(i);
                    long userId = jsonMessageObject.getLong("user_id");
                    idList.add(userId);
                }
                VkIdToUserStorage.getUsers(idList);
            }
            for (int i = 0; i < allMessages.length(); i++) {
                JSONObject jsonMessageObject = allMessages.getJSONObject(i);
                VkMessage.Builder builder = new VkMessage.Builder().setText(jsonMessageObject.getString("body")).setDate(new Date(jsonMessageObject.getLong("date") * 1000)).setId(jsonMessageObject.getLong("id"));
                if (jsonMessageObject.getLong("from_id") > 0)
                    builder = builder.setSender(VkIdToUserStorage.getUser(jsonMessageObject.getLong("from_id")));
                else
                    builder.setSender(VkIdToGroupStorage.getGroup(jsonMessageObject.getLong("from_id")));
                new StringBuilder();
                if (receiverType == ReceiverType.CHAT && jsonMessageObject.has("action"))
                    builder.setText(VkChatAction.convert(context, jsonMessageObject));
                list.add(builder.build());
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
