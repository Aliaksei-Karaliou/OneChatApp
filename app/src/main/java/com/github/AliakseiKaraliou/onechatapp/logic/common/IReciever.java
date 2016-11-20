package com.github.AliakseiKaraliou.onechatapp.logic.common;

import android.os.Parcelable;

import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public interface IReciever extends Parcelable{
    long getId();

    String getName();

    String getPhotoUrl();

    void setPhotoUrl(String photoUrl);

    SocialNetwork getSocialNetwork();

    ReceiverType getReceiverType();
}
