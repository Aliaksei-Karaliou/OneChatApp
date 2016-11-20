package com.github.AliakseiKaraliou.onechatapp.logic.common;

import android.support.annotation.NonNull;

import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkMessageSender;

public abstract class MessageSender {

    public static MessageSender getInstance(SocialNetwork socialNetwork, ReceiverType receiverType) {
       /* if (socialNetwork == SocialNetwork.Vk)
            return new VkMessageSender();
        else return null;*/
        return new VkMessageSender();
    }

    public abstract boolean send(@NonNull IReciever reciever, @NonNull String message);
}
