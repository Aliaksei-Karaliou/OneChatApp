package com.example.korol.onechatapp.logic.common;

import android.os.Parcelable;

import com.example.korol.onechatapp.logic.common.enums.SenderType;
import com.example.korol.onechatapp.logic.common.enums.SocialNetwork;

public interface ISender extends Parcelable {

    long getId();

    String getName();

    String getPhotoUrl();

    void setPhotoUrl(String photoUrl);

    SocialNetwork getSocialNetwork();

    SenderType getSenderType();
}
