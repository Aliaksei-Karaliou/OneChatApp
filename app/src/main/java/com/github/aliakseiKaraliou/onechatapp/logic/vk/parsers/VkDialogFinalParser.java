package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.content.Context;
import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkChatAction;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VkDialogFinalParser {

    public List<IMessage> parse(Context context, String json, LongSparseArray<IReceiver> array) {
        try {
            List<IMessage> result = new ArrayList<>();
            JSONArray items = new JSONObject(json).getJSONObject(Constants.Json.RESPONSE).getJSONArray(Constants.Json.ITEMS);
            JSONObject currentObject;
            final VkIdConverter vkIdConverter = new VkIdConverter();
            for (int i = 0; i < items.length(); i++) {
                currentObject = items.getJSONObject(i);

                VkMessage.Builder builder = new VkMessage.Builder();

                long id = currentObject.getLong(Constants.Json.ID);
                builder.setId(id);

                String text = currentObject.getString(Constants.Json.BODY);
                builder.setText(text);

                long date = currentObject.getLong(Constants.Json.DATE);
                builder.setDate(date);

                boolean isRead = currentObject.getInt(Constants.Json.READ_STATE) > 0;
                builder.setRead(isRead);

                boolean isOut = currentObject.getInt(Constants.Json.OUT) > 0;
                builder.setOut(isOut);

                long userId = currentObject.getLong(Constants.Json.FROM_ID);
                ISender sender = (ISender) array.get(userId);
                builder.setSender(sender);

                if (currentObject.has(Constants.Json.CHAT_ID)) {
                    long chatId = currentObject.getLong(Constants.Json.CHAT_ID);
                    Long peerId = vkIdConverter.chatToPeer(chatId);
                    if (peerId != null) {
                        IChat chat = (IChat) array.get(peerId);
                        builder.setChat(chat);
                    }
                }

                if (currentObject.has(Constants.Json.ACTION)) {
                    builder.setText(new VkChatAction().convert(context, currentObject));
                }

                final VkMessage resultMessage = builder.build();
                result.add(resultMessage);
            }
            return result;
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}