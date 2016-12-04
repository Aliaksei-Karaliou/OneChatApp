package com.github.aliakseiKaraliou.onechatapp;

import android.app.Application;
import android.content.Intent;

import com.github.aliakseiKaraliou.onechatapp.logic.db.SimpleORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.ImageLoaderManager;
import com.github.aliakseiKaraliou.onechatapp.services.ReceivingService;
import com.github.aliakseiKaraliou.onechatapp.services.notifications.SimpleNotificationManager;

public class App extends Application {

    private SimpleORM<DbMessage> messageORM;
    private SimpleORM<DbReciever> receiverORM;
    private ImageLoaderManager imageLoaderManager;
    private SimpleNotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, ReceivingService.class));

        //DbMessage SimpleORM
        messageORM = new SimpleORM<>(this, "Message", DbMessage.class);
        messageORM.createTableIfNotExists();

        //DbReciever SimpleORM
        receiverORM = new SimpleORM<>(this, "Receiver", DbReciever.class);
        receiverORM.createTableIfNotExists();

        //imageLoadManager
        imageLoaderManager = new ImageLoaderManager();

        //notificationManager
        notificationManager=new SimpleNotificationManager(this);
    }

    public SimpleORM<DbMessage> getMessageORM() {
        return messageORM;
    }

    public SimpleORM<DbReciever> getReceiverORM() {
        return receiverORM;
    }

    public ImageLoaderManager getImageLoaderManager() {
        return imageLoaderManager;
    }

    public SimpleNotificationManager getNotificationManager() {
        return notificationManager;
    }

}
