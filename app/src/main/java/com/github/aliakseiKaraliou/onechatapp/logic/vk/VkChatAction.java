package com.github.aliakseiKaraliou.onechatapp.logic.vk;

import android.content.Context;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class VkChatAction {
    public String convert(Context context, JSONObject actionJSON) {
        try {
            final String action = actionJSON.getString(Constants.Json.ACTION);
            switch (action) {
                case Constants.Json.CHAT_INVITE_USER: {
                    final long actionMid = actionJSON.getLong(Constants.Json.ACTION_MID);
                    final IUser actioning = (IUser) VkReceiverStorage.get(actionMid);
                    return String.format(Locale.US, context.getString(R.string.message_chat_invite), actioning.getName());
                }
                case Constants.Json.CHAT_KICK_USER: {
                    final long actionMid = actionJSON.getLong(Constants.Json.ACTION_MID);
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