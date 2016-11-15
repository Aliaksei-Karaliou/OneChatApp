package com.github.AliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;


public class OperationMemoryCache implements Cache<Long, Bitmap> {

    private static Map<Long, Bitmap> cache = new HashMap<>();

    @Override
    public void put(Long id, Bitmap bitmap) {
        cache.put(id, bitmap);
    }

    @Nullable
    @Override
    public Bitmap get(Long id) {
        return cache.get(id);
    }

    @Override
    public Map<Long, Bitmap> getCache() {
        return cache;
    }

    @Override
    public boolean contains(Long id) {
        return cache.containsKey(id);
    }
}
