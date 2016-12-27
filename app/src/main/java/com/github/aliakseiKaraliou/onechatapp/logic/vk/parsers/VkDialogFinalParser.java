package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.content.Context;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.aliakseiKaraliou.onechatapp.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkChatAction;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VkDialogFinalParser {

    public List<IMessage> parse(final Context context, final String json) {
        try {
            final List<IMessage> result = new ArrayList<>();
            final JSONArray items = new JSONObject(json).getJSONObject(Constants.Json.RESPONSE).getJSONArray(Constants.Json.ITEMS);
            JSONObject currentObject;
            final VkIdConverter vkIdConverter = new VkIdConverter();
            for (int i = 0; i < items.length(); i++) {
                currentObject = items.getJSONObject(i);

                final VkMessage.Builder builder = new VkMessage.Builder();

                final long id = currentObject.getLong(Constants.Json.ID);
                builder.setId(id);

                final String text = currentObject.getString(Constants.Json.BODY);
                builder.setText(text);

                final long date = currentObject.getLong(Constants.Json.DATE);
                builder.setDate(date);

                final boolean isRead = currentObject.getInt(Constants.Json.READ_STATE) > 0;
                builder.setRead(isRead);

                final boolean isOut = currentObject.getInt(Constants.Json.OUT) > 0;
                builder.setOut(isOut);

                final long userId = currentObject.getLong(Constants.Json.USER_ID);
                final ISender sender = (ISender) VkReceiverStorage.get(userId);
                builder.setSender(sender);

                if (currentObject.has(Constants.Json.CHAT_ID)) {
                    final long chatId = currentObject.getLong(Constants.Json.CHAT_ID);
                    final long peerId = vkIdConverter.chatToPeer(chatId);
                    final IChat chat = (IChat) VkReceiverStorage.get(peerId);
                    builder.setChat(chat);
                    new StringBuilder();
                }

                if (currentObject.has(Constants.Json.ACTION)) {
                    builder.setText(new VkChatAction().convert(context, currentObject));
                }

                final IMessage resultMessage = builder.build();
                result.add(resultMessage);
            }
            return result;
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}