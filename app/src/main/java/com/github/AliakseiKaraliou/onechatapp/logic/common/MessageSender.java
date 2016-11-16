package com.github.AliakseiKaraliou.onechatapp.logic.common;

import android.support.annotation.NonNull;

import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.SenderType;
import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkMessageSender;

public abstract class MessageSender {

    public static MessageSender getInstance(SocialNetwork socialNetwork, SenderType senderType) {
       /* if (socialNetwork == SocialNetwork.Vk)
            return new VkMessageSender();
        else return null;*/
        return new VkMessageSender();
    }

    public abstract boolean send(@NonNull ISender sender, @NonNull String message);
}
