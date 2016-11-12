package com.example.korol.onechatapp.logic.vk.json;

import android.content.Context;

import com.example.korol.onechatapp.R;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.IUser;
import com.example.korol.onechatapp.logic.common.enums.SenderType;
import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.example.korol.onechatapp.logic.vk.entities.VkMessage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToGroupStorage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToUserStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VkDialogJsonParser extends AsyncOperation<String, List<IMessage>> {

    private SenderType senderType;

    public VkDialogJsonParser(Context context, SenderType senderType) {
        this.context = context;
        this.senderType = senderType;
    }

    private Context context;
    
    @Override
    protected List<IMessage> doInBackground(String json) {
        try {
            List<IMessage> list = new ArrayList<>();
            JSONArray allMessages = new JSONObject(json).getJSONObject("response").getJSONArray("items");
            if (senderType == SenderType.CHAT) {
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
                if (senderType == SenderType.CHAT) {
                    if (jsonMessageObject.has("action")) {
                        if (jsonMessageObject.getString("action").equals("chat_invite_user")) {
                            final IUser actioning = VkIdToUserStorage.getUser(jsonMessageObject.getLong("action_mid"));
                            builder.setText(String.format(Locale.getDefault(), context.getString(R.string.message_chat_invite), actioning.getName()));
                        } else if (jsonMessageObject.getString("action").equals( "chat_kick_user")) {
                            final IUser actioning = VkIdToUserStorage.getUser(jsonMessageObject.getLong("action_mid"));
                            builder.setText(String.format(Locale.getDefault(), "%s was removed from this chat", actioning.getName()));
                        }
                    }
                }
                list.add(builder.build());
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
