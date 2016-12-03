package com.github.aliakseiKaraliou.onechatapp.logic.common;

import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;

import java.util.Date;

public interface IMessage extends Cloneable {


    long getId();

    ISender getSender();

    String getText();

    Date getDate();

    long getUnixDate();

    boolean isRead();

    boolean isOut();

    IMessage read();

    IReciever getReciever();

    @Nullable
    IChat getChat();
}

