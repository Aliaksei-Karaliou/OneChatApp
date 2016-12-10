package com.github.aliakseiKaraliou.onechatapp.logic.common;

import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.PeerRecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public interface IReceiver {
    long getId();

    String getName();

    String getPhotoUrl();

    void setPhotoUrl(String photoUrl);

    SocialNetwork getSocialNetwork();

    PeerRecieverType getPeerReceiverType();
}
