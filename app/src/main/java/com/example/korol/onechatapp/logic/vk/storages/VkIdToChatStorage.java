package com.example.korol.onechatapp.logic.vk.storages;

import android.support.annotation.Nullable;
import android.util.Pair;

import com.example.korol.onechatapp.logic.vk.VkRequester;
import com.example.korol.onechatapp.logic.vk.entities.VkChat;
import com.example.korol.onechatapp.logic.vk.json.VkBasicChatJsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class VkIdToChatStorage {

    private static Map<Long, VkChat> storage = new HashMap<>();

    public static void put(long id, VkChat chat) {
        storage.put(id, chat);
    }

    @Nullable
    public static VkChat getChat(long id) {
        if (contains(id))
            return storage.get(id);
        else {
            List<Long> list = new ArrayList<>();
            list.add(id);
            return getChats(list).get(id);
        }
    }

    @Nullable
    public static Map<Long, VkChat> getChats(List<Long> ids) {
        Map<Long, VkChat> result = new HashMap<>();
        List<Long> requireIds = new ArrayList<>();

        for (Long id : ids) {
            if (!contains(id))
                requireIds.add(id);
            else result.put(id, storage.get(id));
        }

        if (requireIds.size() > 0) {
            StringBuilder builder = new StringBuilder();
            // add new users
            for (long id : requireIds)
                if (!storage.containsKey(id))
                    builder.append(id - 2000000000).append(",");
            String params = builder.substring(0, builder.length() - 1);
            final String chat_ids;
            try {
                chat_ids = new VkRequester("messages.getChat", new Pair<String, String>("chat_ids", params)).execute(null);
                final Map<Long, VkChat> requestResult = new VkBasicChatJsonParser().execute(chat_ids);
                for (Long requestId : requestResult.keySet()) {
                    VkChat chat = requestResult.get(requestId);
                    storage.put(requestId, chat);
                    result.put(requestId, chat);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public static boolean contains(long id) {
        return storage.containsKey(id);
    }

}
