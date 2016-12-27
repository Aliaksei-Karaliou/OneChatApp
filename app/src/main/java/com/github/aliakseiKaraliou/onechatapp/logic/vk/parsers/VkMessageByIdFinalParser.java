package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.aliakseiKaraliou.onechatapp.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class VkMessageByIdFinalParser implements IParser<String, List<IMessage>> {

    @Nullable
    @Override
    public List<IMessage> parse(final String s) {
        final List<IMessage> messageList = new ArrayList<>();
        try {
            final JSONArray messages = new JSONObject(s).getJSONObject(Constants.Json.RESPONSE).getJSONArray(Constants.Json.ITEMS);
            for (int i = 0; i < messages.length(); i++) {
                final JSONObject currentObject = messages.getJSONObject(i);
                final long id = currentObject.getLong(Constants.Json.ID);
                final long timestamp = currentObject.getLong(Constants.Json.DATE);
                final long userId = currentObject.getLong(Constants.Json.USER_ID);
                final ISender sender = (ISender) VkReceiverStorage.get(userId);
                final long out = currentObject.getLong(Constants.Json.OUT);
                final long readState = currentObject.getLong(Constants.Json.READ_STATE);
                final String messageText = currentObject.getString(Constants.Json.BODY);
                final VkMessage.Builder builder = new VkMessage.Builder();
                builder.setSender(sender).setOut(out > 0).setRead(readState > 0).setDate(timestamp).setId(id).setText(messageText);
                if (currentObject.has(Constants.Json.CHAT_ID)) {
                    final long chatId = currentObject.getLong(Constants.Json.CHAT_ID);
                    final long peerId = new VkIdConverter().chatToPeer(chatId);
                    final IChat chat = (IChat) VkReceiverStorage.get(peerId);
                    builder.setChat(chat);
                }
                final VkMessage message = builder.build();
                messageList.add(message);
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }
        return messageList;
    }
}
