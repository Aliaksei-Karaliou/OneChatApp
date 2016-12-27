package com.github.aliakseiKaraliou.onechatapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.db.DbHelper;
import com.github.aliakseiKaraliou.onechatapp.logic.db.ORM;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.ChatModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.GroupModel;
import com.github.aliakseiKaraliou.onechatapp.logic.db.models.UserModel;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.imageLoader.AvatarImageLoaderManager;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.network.NetworkConnectionChecker;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.ui.activities.DialogsListActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private AvatarImageLoaderManager imageLoaderManager;
    private SharedPreferences applicationSharedPreferences;
    private ORM recieverORM;

    private static final String GET_RES_ID_METHOD="getThemeResId";

    @Override
    public void onCreate() {
        super.onCreate();

        recieverORM = new ORM(new DbHelper(this, Constants.Db.RECEIVERS, 1, UserModel.class ,ChatModel.class, GroupModel.class));

        imageLoaderManager = new AvatarImageLoaderManager();

        applicationSharedPreferences = getSharedPreferences(Constants.Other.PREFERENCES, MODE_PRIVATE);

        if (NetworkConnectionChecker.check(this)) {
            VkInfo.userGetAuth(applicationSharedPreferences);
        }

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

    public AvatarImageLoaderManager getImageLoaderManager() {
        return imageLoaderManager;
    }

    public SharedPreferences getApplicationSharedPreferences() {
        return applicationSharedPreferences;
    }

    public void restartApp() {
        final Intent i = new Intent(this, DialogsListActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
