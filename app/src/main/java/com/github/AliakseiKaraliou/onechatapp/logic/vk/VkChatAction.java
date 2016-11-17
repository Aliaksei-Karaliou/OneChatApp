package com.github.AliakseiKaraliou.onechatapp.logic.vk;

import android.content.Context;

import com.github.AliakseiKaraliou.onechatapp.R;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.storages.VkIdToUserStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class VkChatAction {
    public static String convert(Context context, JSONObject actionJSON) {
        try {
            final String action = actionJSON.getString("action");
            if (action.equals("chat_invite_user")) {
                final IUser actioning = VkIdToUserStorage.getUser(actionJSON.getLong("action_mid"));
                return String.format(Locale.US, context.getString(R.string.message_chat_invite), actioning.getName());
            } else if (action.equals("chat_kick_user")) {
                final IUser actioning = VkIdToUserStorage.getUser(actionJSON.getLong("action_mid"));
                return String.format(Locale.US, context.getString(R.string.message_chat_kick), actioning.getName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
}
