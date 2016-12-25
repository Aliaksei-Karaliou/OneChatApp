package com.github.aliakseiKaraliou.onechatapp.logic.vk;

public enum VkMessageFlag {

    UNREAD(1),
    OUTBOX(2),
    REPLIED(4),
    IMPORTANT(8),
    CHAT(16),
    FRIENDS(32),
    SPAM(64),
    DELЕTЕD(128),
    FIXED(256),
    MEDIA(512);

    private long key;

    VkMessageFlag(long key) {
        this.key = key;
    }

    public long getKey() {
        return key;
    }
}
