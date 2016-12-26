package com.github.aliakseiKaraliou.onechatapp.logic.vk.storages;

import android.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;

import java.util.Collection;

public class VkMessageStorage {

    private static final LongSparseArray<IMessage> longSparseArray = new LongSparseArray<>();

    public static void put(final IMessage message) {
        final long id = message.getId();
        final IMessage bufMessage = get(id);
        if (bufMessage == null) {
            longSparseArray.put(id, message);
        }
    }

    public static void putAll(final Collection<IMessage> messages) {
        for (final IMessage message : messages) {
            put(message);
        }
    }

    public static IMessage get(final long id) {
        return longSparseArray.get(id);
    }
}
