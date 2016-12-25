package com.github.aliakseiKaraliou.onechatapp.logic.vk.storages;

import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;

import java.util.List;

public class VkReceiverStorage {
    private static LongSparseArray<IReceiver> storage = new LongSparseArray<>();

    public static void put(IReceiver reciever) {
        final long id = reciever.getId();
        final IReceiver receiver = storage.get(id);
        if (receiver != null) {
            storage.delete(id);
        }
        storage.put(id, reciever);
    }

    public static IReceiver get(Long id) {
        return id != null ? storage.get(id) : null;
    }

    public static void putAll(LongSparseArray<IReceiver> array) {
        for (int i = 0; i < array.size(); i++) {
            final IReceiver receiver = array.valueAt(i);
            VkReceiverStorage.put(receiver);
        }
    }

    public static void putAll(List<IReceiver> receiverList) {
        for (IReceiver reciever : receiverList) {
            storage.put(reciever.getId(), reciever);
        }
    }
}