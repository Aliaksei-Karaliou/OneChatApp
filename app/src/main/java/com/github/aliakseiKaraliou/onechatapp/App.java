package com.github.aliakseiKaraliou.onechatapp;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.db.DbHelper;
import com.github.aliakseiKaraliou.onechatapp.logic.db.ORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.ChatModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.DialogListMessageModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.GroupModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.MessageModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.UserModel;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.LazyImageLoaderManager;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.network.NetworkConnectionChecker;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.services.ReceivingService;
import com.github.aliakseiKaraliou.onechatapp.services.broadcastReceiver.MessageBroadcastReceiver;
import com.github.aliakseiKaraliou.onechatapp.services.notifications.SimpleNotificationManager;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private LazyImageLoaderManager imageLoaderManager;
    private SimpleNotificationManager notificationManager;
    private SharedPreferences applicationSharedPreferences;
    private ORM recieverORM;
    private ORM messageORM;

    @Override
    public void onCreate() {
        super.onCreate();

        //recieverORM
        recieverORM = new ORM(new DbHelper(this, Constants.Db.RECEIVERS, 1, UserModel.class ,ChatModel.class, GroupModel.class));

        //messageORM
        messageORM = new ORM(new DbHelper(this, Constants.Db.MESSAGES, 1, MessageModel.class, DialogListMessageModel.class));

        //imageLoadManager
        imageLoaderManager = new LazyImageLoaderManager();

        //notificationManager
        notificationManager = new SimpleNotificationManager(this);

        //applicationSharedPreferences
        applicationSharedPreferences = getSharedPreferences(Constants.Other.PREFERENCES, MODE_PRIVATE);

        if (NetworkConnectionChecker.check(this)) {
            // boolean isOnline = true;
            VkInfo.userGetAuth(applicationSharedPreferences);
        }

        if (VkInfo.isUserAuthorized()) {
            startService(new Intent(this, ReceivingService.class));
        }

        final MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
        registerReceiver(messageBroadcastReceiver, new IntentFilter(Constants.Other.BROADCAST_MESSAGE_RECEIVER_NAME));

        //read receivers from db
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<IUser> userList = UserModel.convertFrom(recieverORM.selectAll(Constants.Db.USERS, UserModel.getInstance()));
                final List<IChat> chatList = ChatModel.convertFrom(recieverORM.selectAll(Constants.Db.CHATS, ChatModel.getInstance()));
                final List<IGroup> groupList = GroupModel.convertFrom(recieverORM.selectAll(Constants.Db.GROUPS, GroupModel.getInstance()));
                VkReceiverStorage.putAll(new ArrayList<IReceiver>(userList));
                VkReceiverStorage.putAll(new ArrayList<IReceiver>(groupList));
                VkReceiverStorage.putAll(new ArrayList<IReceiver>(chatList));
            }
        }).run();
    }

    public ORM getRecieverORM() {
        return recieverORM;
    }

    public ORM getMessageORM() {
        return messageORM;
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
