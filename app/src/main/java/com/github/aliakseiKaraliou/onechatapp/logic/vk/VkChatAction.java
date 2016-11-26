package com.github.aliakseiKaraliou.onechatapp.logic.vk;

import android.content.Context;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class VkChatAction {
    public String convert(Context context, JSONObject actionJSON) {
        try {
            final String action = actionJSON.getString("action");
            switch (action) {
                case "chat_invite_user": {
                    final long actionMid = actionJSON.getLong("action_mid");
                    final IUser actioning = (IUser) VkReceiverStorage.get(actionMid);
                    return String.format(Locale.US, context.getString(R.string.message_chat_invite), actioning.getName());
                }
                case "chat_kick_user": {
                    final long actionMid = actionJSON.getLong("action_mid");
                    final IUser actioning = (IUser) VkReceiverStorage.get(actionMid);
                    return String.format(Locale.US, context.getString(R.string.message_chat_kick), actioning.getName());
                }
                default:
                    return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}