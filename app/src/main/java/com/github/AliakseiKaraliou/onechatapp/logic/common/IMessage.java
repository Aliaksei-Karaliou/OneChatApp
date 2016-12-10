package com.github.aliakseiKaraliou.onechatapp.logic.common;

import android.support.annotation.Nullable;

import java.util.Date;

public interface IMessage extends Cloneable, IEvent {

    long getId();

    ISender getSender();

    String getText();

    Date getDate();

    long getUnixDate();

    boolean isRead();

    boolean isOut();

    IMessage read();

    IReceiver getReceiver();

    @Nullable
    IChat getChat();
}

