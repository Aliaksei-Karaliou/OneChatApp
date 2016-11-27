package com.github.aliakseiKaraliou.onechatapp.logic.common;

import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.RecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public interface IReciever {
    long getId();

    String getName();

    String getPhotoUrl();

    void setPhotoUrl(String photoUrl);

    SocialNetwork getSocialNetwork();

    RecieverType getReceiverType();
}
