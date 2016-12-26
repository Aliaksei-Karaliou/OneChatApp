package com.github.aliakseiKaraliou.onechatapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;

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
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.AvatarImageLoaderManager;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.network.NetworkConnectionChecker;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private AvatarImageLoaderManager imageLoaderManager;
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
        imageLoaderManager = new AvatarImageLoaderManager();

        //applicationSharedPreferences
        applicationSharedPreferences = getSharedPreferences(Constants.Other.PREFERENCES, MODE_PRIVATE);

        if (NetworkConnectionChecker.check(this)) {
            VkInfo.userGetAuth(applicationSharedPreferences);
        }

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

    public AvatarImageLoaderManager getImageLoaderManager() {
        return imageLoaderManager;
    }


    public SharedPreferences getApplicationSharedPreferences() {
        return applicationSharedPreferences;
    }

    public int getApplicationBackgroundColour() {
        final TypedArray array = getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground,
        });
        final int color = array.getColor(0, Color.WHITE);
        final int red = Color.red(color);
        final int blue = Color.blue(color);
        final int green = Color.green(color);
        return color;
    }
}
