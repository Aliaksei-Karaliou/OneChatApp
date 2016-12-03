package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.content.Context;
import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkChatAction;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.entities.VkMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VkDialogsListFinalParser {

    public List<IMessage> parse(Context context, String json, LongSparseArray<IReciever> array) {
        try {
            List<IMessage> result = new ArrayList<>();
            JSONArray items = new JSONObject(json).getJSONObject(VkConstants.Json.RESPONSE).getJSONArray(VkConstants.Json.ITEMS);
            JSONObject currentObject;
            final VkIdConverter vkIdConverter = new VkIdConverter();
            for (int i = 0; i < items.length(); i++) {
                currentObject = items.getJSONObject(i).getJSONObject(VkConstants.Json.MESSAGE);

                VkMessage.Builder builder = new VkMessage.Builder();

                long id = currentObject.getLong(VkConstants.Json.ID);
                builder.setId(id);

                String text = currentObject.getString(VkConstants.Json.BODY);
                builder.setText(text);

                long date = currentObject.getLong(VkConstants.Json.DATE);
                builder.setDate(date);

                boolean isRead = currentObject.getInt(VkConstants.Json.READ_STATE) > 0;
                builder.setRead(isRead);

                boolean isOut = currentObject.getInt(VkConstants.Json.OUT) > 0;
                builder.setOut(isOut);


                long userId = currentObject.getLong(VkConstants.Json.USER_ID);
                ISender sender = (ISender) array.get(userId);
                builder.setSender(sender);

                if (currentObject.has(VkConstants.Json.CHAT_ID)) {
                    long chatId = currentObject.getLong(VkConstants.Json.CHAT_ID);
                    Long peerID = vkIdConverter.chatToPeer(chatId);
                    if (peerID != null) {
                        IChat chat = (IChat) array.get(peerID);
                        builder.setChat(chat);
                    }
                    if (currentObject.has(VkConstants.Json.ACTION)) {
                        builder.setText(new VkChatAction().convert(context, currentObject));
                    }
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