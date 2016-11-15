package com.github.AliakseiKaraliou.onechatapp.logic.common;

import android.os.Parcelable;

import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.SenderType;
import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public interface ISender extends Parcelable {

    long getId();

    String getName();

    String getPhotoUrl();

    void setPhotoUrl(String photoUrl);

    SocialNetwork getSocialNetwork();

    SenderType getSenderType();


}
