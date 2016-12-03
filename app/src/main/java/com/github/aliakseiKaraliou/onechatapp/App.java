package com.github.aliakseiKaraliou.onechatapp;

import android.app.Application;

import com.github.aliakseiKaraliou.onechatapp.logic.db.SimpleORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.ImageLoaderManager;

public class App extends Application {

    private SimpleORM<DbMessage> messageORM;
    private SimpleORM<DbReciever> receiverORM;
    private ImageLoaderManager imageLoaderManager;

    @Override
    public void onCreate() {
        super.onCreate();

        //DbMessage SimpleORM
        messageORM = new SimpleORM<>(this, "Message", DbMessage.class);
        messageORM.createTableIfNotExists();

        //DbReciever SimpleORM
        receiverORM = new SimpleORM<>(this, "Reciever", DbReciever.class);
        receiverORM.createTableIfNotExists();

        //imageLoadManager
        imageLoaderManager = new ImageLoaderManager();
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

}
