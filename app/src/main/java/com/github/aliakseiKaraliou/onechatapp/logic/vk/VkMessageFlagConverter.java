package com.github.aliakseiKaraliou.onechatapp.logic.vk;

import java.util.HashSet;
import java.util.Set;

public class VkMessageFlagConverter {

    public Set<VkMessageFlag> Convert(int num) {
        final Set<VkMessageFlag> flagList = new HashSet<>();

        if (num >= 512) {
            num -= 512;
            flagList.add(VkMessageFlag.MEDIA);
        }

        if (num >= 256) {
            num -= 256;
            flagList.add(VkMessageFlag.FIXED);
        }

        if (num >= 128) {
            num -= 128;
            flagList.add(VkMessageFlag.DELЕTЕD);
        }

        if (num >= 64) {
            num -= 64;
            flagList.add(VkMessageFlag.SPAM);
        }

        if (num >= 32) {
            num -= 32;
            flagList.add(VkMessageFlag.FRIENDS);
        }

        if (num >= 16) {
            num -= 16;
            flagList.add(VkMessageFlag.CHAT);
        }

        if (num >= 8) {
            num -= 8;
            flagList.add(VkMessageFlag.IMPORTANT);
        }

        if (num >= 4) {
            num -= 4;
            flagList.add(VkMessageFlag.REPLIED);
        }

        if (num >= 2) {
            num -= 2;
            flagList.add(VkMessageFlag.OUTBOX);
        }

        if (num >= 1) {
            num -= 1;
            flagList.add(VkMessageFlag.UNREAD);
        }
        return flagList;
    }
}
