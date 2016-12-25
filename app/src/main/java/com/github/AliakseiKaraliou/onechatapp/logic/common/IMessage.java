package com.github.aliakseiKaraliou.onechatapp.logic.common;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.Equatable;

import java.util.Date;

public interface IMessage extends Cloneable, Parcelable, Equatable<IMessage> {

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

