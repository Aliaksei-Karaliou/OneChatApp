package com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll;

import android.support.annotation.NonNull;

import java.util.List;

public class VkLongPollUpdate {
    private long ts;
    private List<List<String>> info;

    public VkLongPollUpdate(long ts, @NonNull List<List<String>> info) {
        this.ts = ts;
        this.info = info;
    }

    public long getTs() {
        return ts;
    }
}
