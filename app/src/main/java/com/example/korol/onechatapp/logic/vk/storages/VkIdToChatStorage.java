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
        //2000000000
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ids.size() - 1; i++)
            builder.append(ids.get(i) - 2000000000 + ",");
        String idsStringValue = builder.append(ids.get(ids.size() - 1) - 2000000000).toString();
        try {
            final String chat_ids = new VkRequester("messages.getChat", new Pair<>("chat_ids", idsStringValue)).execute(null);
            final Map<Long, VkChat> result = new VkBasicChatJsonParser().execute(chat_ids);
            for (Long id : result.keySet())
                storage.put(id, result.get(id));
            return result;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean contains(long id) {
        return storage.containsKey(id);
    }

}
