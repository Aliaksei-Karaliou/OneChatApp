package com.github.aliakseiKaraliou.onechatapp;

import android.app.Application;

import com.github.aliakseiKaraliou.onechatapp.logic.db.SimpleORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbReceiver;

public class App extends Application {

    private SimpleORM<DbMessage> messageORM;
    private SimpleORM<DbReceiver> receiverORM;

    @Override
    public void onCreate() {
        super.onCreate();

        //DbMessage SimpleORM
        messageORM = new SimpleORM<>(this, "Message", DbMessage.class);
        messageORM.createTableIfNotExists();

        //DbReceiver SimpleORM
        receiverORM = new SimpleORM<>(this, "Reciever", DbReceiver.class);
        receiverORM.createTableIfNotExists();
    }

    public SimpleORM<DbMessage> getMessageORM() {
        return messageORM;
    }


    public SimpleORM<DbReceiver> getReceiverORM() {
        return receiverORM;
    }

}
