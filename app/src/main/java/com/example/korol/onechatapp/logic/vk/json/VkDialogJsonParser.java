package com.example.korol.onechatapp.logic.vk.json;

import android.content.Context;

import com.example.korol.onechatapp.R;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.IUser;
import com.example.korol.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.example.korol.onechatapp.logic.vk.entities.VkMessage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToUserStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class VkDialogJsonParser extends AsyncOperation<String, List<IMessage>> {

    public VkDialogJsonParser(Context context) {
        this.context = context;
    }

    private Context context;
    
    @Override
    protected List<IMessage> doInBackground(String json) throws JSONException {
        List<IMessage> list = new ArrayList<>();
        JSONArray allMessages = new JSONObject(json).getJSONObject("response").getJSONArray("items");
        for (int i = 0; i < allMessages.length(); i++) {
            JSONObject jsonMessageObject = allMessages.getJSONObject(i);
            VkMessage.Builder builder = new VkMessage.Builder().setText(jsonMessageObject.getString("body")).setSender(VkIdToUserStorage.getUser(jsonMessageObject.getLong("from_id"))).setDate(new Date(jsonMessageObject.getLong("date") * 1000));
            if (jsonMessageObject.has("action")) {
                if (Objects.equals(jsonMessageObject.getString("action"), "chat_invite_user")) {
                    final IUser actioning = VkIdToUserStorage.getUser(jsonMessageObject.getLong("action_mid"));
                    builder.setText(String.format(Locale.getDefault(), context.getString(R.string.message_chat_invite), actioning.getName()));
                } else if (Objects.equals(jsonMessageObject.getString("action"), "chat_kick_user")) {
                    final IUser actioning = VkIdToUserStorage.getUser(jsonMessageObject.getLong("action_mid"));
                    builder.setText(String.format(Locale.getDefault(), "%s was removed from this chat", actioning.getName()));
                }
            }
            list.add(builder.build());
        }
        return list;
    }
}
