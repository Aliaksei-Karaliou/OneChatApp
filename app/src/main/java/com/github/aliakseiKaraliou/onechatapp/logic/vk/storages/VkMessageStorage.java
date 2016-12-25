package com.github.aliakseiKaraliou.onechatapp.logic.vk.storages;

import android.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;

import java.util.Collection;

public class VkMessageStorage {

    private static LongSparseArray<IMessage> longSparseArray = new LongSparseArray<>();

    public static void put(IMessage message) {
        final long id = message.getId();
        final IMessage bufMessage = get(id);
        if (bufMessage == null) {
            longSparseArray.put(id, message);
        }
    }

    public static void putAll(Collection<IMessage> messages) {
        for (IMessage message : messages) {
            put(message);
        }
    }

    public static IMessage get(long id) {
        return longSparseArray.get(id);
    }
}
