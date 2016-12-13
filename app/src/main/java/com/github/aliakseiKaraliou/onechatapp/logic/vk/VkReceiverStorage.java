package com.github.aliakseiKaraliou.onechatapp.logic.vk;

import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;

import java.util.List;

public class VkReceiverStorage {
    private static LongSparseArray<IReceiver> storage = new LongSparseArray<>();

    public static void put(IReceiver reciever) {
        final IReceiver receiver = storage.get(reciever.getId());
        if (receiver == null) {
            storage.put(reciever.getId(), reciever);
        }
    }

    public static IReceiver get(Long id) {
        return id != null ? storage.get(id) : null;
    }

    public static void putAll(LongSparseArray<IReceiver> array) {
        for (int i = 0; i < array.size(); i++) {
            long key = array.keyAt(i);
            IReceiver reciever = storage.get(key);
            if (reciever == null) {
                IReceiver value = array.valueAt(i);
                storage.put(key, value);
            }
        }
    }

    public static void putAll(List<IReceiver> receiverList) {
        for (IReceiver reciever : receiverList) {
            storage.put(reciever.getId(), reciever);
        }
    }
}