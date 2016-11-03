package com.example.korol.onechatapp.logic.vk.packers;

import com.example.korol.onechatapp.logic.common.ISender;
import com.example.korol.onechatapp.logic.common.enums.SenderType;
import com.example.korol.onechatapp.logic.common.enums.SocialNetwork;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToChatStorage;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToUserStorage;

public class Packer {

    public String pack(ISender sender) {
        final StringBuilder builder = new StringBuilder();

        if (sender.getSocialNetwork() == SocialNetwork.Vk)
            builder.append("v");
        else if (sender.getSocialNetwork() == SocialNetwork.Facebook)
            builder.append("f");

        if (sender.getSenderType() == SenderType.CHAT)
            builder.append("c");
        else if (sender.getSenderType() == SenderType.USER)
            builder.append("u");
        else if (sender.getSenderType() == SenderType.GROUP)
            builder.append("g");

        return builder.append(sender.getId()).toString();
    }

    public ISender unPack(String packer) throws PackerException {
        SocialNetwork network;
        SenderType type;

        if (packer.charAt(0) == 'v')
            network = SocialNetwork.Vk;
        else if (packer.charAt(0) == 'f')
            network = SocialNetwork.Facebook;
        else throw new PackerException();

        if (packer.charAt(1) == 'c')
            type = SenderType.CHAT;
        else if (packer.charAt(1) == 'u')
            type = SenderType.USER;
        else if (packer.charAt(1) == 'g')
            type = SenderType.GROUP;
        else throw new PackerException();

        long id = Long.parseLong(packer.substring(2));

        if (network == SocialNetwork.Vk) {
            if (type == SenderType.USER)
                return VkIdToUserStorage.getUser(id);
            else if (type == SenderType.CHAT)
                return VkIdToChatStorage.getChat(id);
        } else throw new PackerException();
        return null;
    }

    public class PackerException extends Exception {}
}
