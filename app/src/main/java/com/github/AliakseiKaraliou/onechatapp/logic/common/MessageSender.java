package com.github.AliakseiKaraliou.onechatapp.logic.common;

import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.SenderType;
import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkMessageSender;

public abstract class MessageSender {

    public static MessageSender getInstance(SocialNetwork socialNetwork, SenderType senderType) {
        if (socialNetwork == SocialNetwork.Vk)
            return new VkMessageSender();
        else return null;
    }

    public abstract boolean send(ISender sender, String message);
}
