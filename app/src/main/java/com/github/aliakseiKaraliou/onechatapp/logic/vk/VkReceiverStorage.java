package com.github.aliakseiKaraliou.onechatapp.logic.vk;

import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;

public class VkReceiverStorage {
    private static LongSparseArray<IReciever> storage = new LongSparseArray<>();

    public static void put(IReciever reciever) {
        storage.put(reciever.getId(), reciever);
    }

    public static IReciever get(long id) {
        return storage.get(id);
    }

    public static void putAll(LongSparseArray<IReciever> array) {
        for (int i = 0; i < array.size(); i++) {
            long key = array.keyAt(i);
            IReciever reciever = storage.get(key);
            if (reciever == null) {
                IReciever value = array.valueAt(i);
                storage.put(key, value);
            }
        }
    }
}
