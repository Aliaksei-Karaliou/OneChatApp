package com.github.aliakseiKaraliou.onechatapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.github.aliakseiKaraliou.onechatapp.logic.db.SimpleORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.LazyImageLoaderManager;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.services.ReceivingService;
import com.github.aliakseiKaraliou.onechatapp.services.notifications.SimpleNotificationManager;

public class App extends Application {

    private SimpleORM<DbMessage> messageORM;
    private SimpleORM<DbReciever> receiverORM;
    private LazyImageLoaderManager imageLoaderManager;
    private SimpleNotificationManager notificationManager;
    private SharedPreferences applicationSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        if (VkInfo.isUserAuthorized()) {
            startService(new Intent(this, ReceivingService.class));
        }

        //DbMessage SimpleORM
        messageORM = new SimpleORM<>(this, "Message", DbMessage.class);
        messageORM.createTableIfNotExists();

        //DbReciever SimpleORM
        receiverORM = new SimpleORM<>(this, "Receiver", DbReciever.class);
        receiverORM.createTableIfNotExists();

        //imageLoadManager
        imageLoaderManager = new LazyImageLoaderManager();

        //notificationManager
        notificationManager=new SimpleNotificationManager(this);

        //applicationSharedPreferences
        applicationSharedPreferences = getSharedPreferences(VkConstants.Other.PREFERENCES, MODE_PRIVATE);
    }

    public SimpleORM<DbMessage> getMessageORM() {
        return messageORM;
    }

    public SimpleORM<DbReciever> getReceiverORM() {
        return receiverORM;
    }

    public LazyImageLoaderManager getImageLoaderManager() {
        return imageLoaderManager;
    }

    public SimpleNotificationManager getNotificationManager() {
        return notificationManager;
    }

    public SharedPreferences getApplicationSharedPreferences() {
        return applicationSharedPreferences;
    }
}
