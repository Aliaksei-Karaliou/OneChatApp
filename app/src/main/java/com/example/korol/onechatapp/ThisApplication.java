package com.example.korol.onechatapp;

import com.example.korol.onechatapp.logic.imageLoader.OperationMemoryCache;

public class ThisApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OperationMemoryCache.createCache();
    }
}
