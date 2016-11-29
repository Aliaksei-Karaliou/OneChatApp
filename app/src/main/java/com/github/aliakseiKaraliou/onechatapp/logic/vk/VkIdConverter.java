package com.github.aliakseiKaraliou.onechatapp.logic.vk;

import android.support.annotation.Nullable;

public class VkIdConverter {
    private static final long CHAT_PEER_OFFSET = 2000000000;
    private static final long EMAIL_PEER_OFFSET = -2000000000;

    public static long getChatPeerOffset() {
        return CHAT_PEER_OFFSET;
    }

    public static long getEmailPeerOffset() {
        return EMAIL_PEER_OFFSET;
    }

    @Nullable
    public Long chatToPeer(long id) {
        if (id > 0 && id < CHAT_PEER_OFFSET)
            return id + CHAT_PEER_OFFSET;
        else {
            return null;
        }
    }

    @Nullable
    public Long groupToPeer(long id) {
        if (id > 0)
            return -id;
        else {
            return null;
        }
    }

    @Nullable
    public Long peerToChat(long id) {
        if (id > CHAT_PEER_OFFSET)
            return id - CHAT_PEER_OFFSET;
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
