package com.github.AliakseiKaraliou.onechatapp.logic.vk;

import android.support.annotation.Nullable;

public class VkIdConverter {
    private static final long chatPeerOffset = 2000000000;

    public static long getChatPeerOffset() {
        return chatPeerOffset;
    }

    @Nullable
    public Long chatToPeer(long id) {
        if (id > 0 && id < chatPeerOffset)
            return id + chatPeerOffset;
        else {
            return null;
        }
    }

    @Nullable
    public Long peerToChat(long id) {
        if (id > chatPeerOffset)
            return id - chatPeerOffset;
        else {
            return null;
        }
    }

    @Nullable
    public Long peerToGroup(long id) {
        if (id < 0)
            return -id;
        else {
            return null;
        }
    }
}
