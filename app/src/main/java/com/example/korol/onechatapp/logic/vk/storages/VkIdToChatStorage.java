package com.example.korol.onechatapp.logic.vk.storages;

import com.example.korol.onechatapp.logic.vk.entities.VkChat;

import java.util.HashMap;
import java.util.Map;

public class VkIdToChatStorage {

    private static Map<Integer, VkChat> storage = new HashMap<>();

    public static void put(int id, VkChat chat) {
        storage.put(id, chat);
    }

    public static VkChat get(int id) {
        return storage.get(id);
    }

    public static boolean containsKey(int id){
        return storage.containsKey(id);
    }

}
