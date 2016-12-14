package com.github.aliakseiKaraliou.onechatapp.logic.common;

import android.os.Parcelable;

import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public interface IReceiver extends Parcelable {
    long getId();

    String getName();

    String getPhoto50Url();

    String getPhoto100Url();

    SocialNetwork getSocialNetwork();

    ReceiverType getReceiverType();
}
