package com.github.aliakseiKaraliou.onechatapp.logic.common;

import android.os.Parcelable;

import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.PeerRecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public interface IReceiver extends Parcelable {
    long getId();

    String getName();

    String getPhotoUrl();

    void setPhotoUrl(String photoUrl);

    SocialNetwork getSocialNetwork();

    PeerRecieverType getPeerReceiverType();
}
