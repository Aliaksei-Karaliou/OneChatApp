package com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll;

public class VkLongPollServer {
    private String key;
    private String server;
    private long ts;

    public VkLongPollServer(String key, String server, long ts) {
        this.key = key;
        this.server = server;
        this.ts = ts;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getServer() {
        return server;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
