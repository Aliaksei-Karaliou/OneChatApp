package com.github.aliakseiKaraliou.onechatapp;

import android.app.Application;

import com.github.aliakseiKaraliou.onechatapp.logic.db.SimpleORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.ImageLoaderManager;

public class App extends Application {

    private SimpleORM<DbMessage> messageORM;
    private SimpleORM<DbReceiver> receiverORM;
    private ImageLoaderManager imageLoaderManager;

    @Override
    public void onCreate() {
        super.onCreate();

        //DbMessage SimpleORM
        messageORM = new SimpleORM<>(this, "Message", DbMessage.class);
        messageORM.createTableIfNotExists();

        //DbReceiver SimpleORM
        receiverORM = new SimpleORM<>(this, "Reciever", DbReceiver.class);
        receiverORM.createTableIfNotExists();

        //imageLoadManager
        imageLoaderManager = new ImageLoaderManager();
    }

    public SimpleORM<DbMessage> getMessageORM() {
        return messageORM;
    }

    public SimpleORM<DbReceiver> getReceiverORM() {
        return receiverORM;
    }

    public ImageLoaderManager getImageLoaderManager() {
        return imageLoaderManager;
    }

}
