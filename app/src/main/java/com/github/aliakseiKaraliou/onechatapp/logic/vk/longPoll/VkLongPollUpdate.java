package com.github.aliakseiKaraliou.onechatapp.logic.vk.longPoll;

import android.support.annotation.NonNull;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkReceiverStorage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.entities.VkMessage;

import java.util.ArrayList;
import java.util.List;

public class VkLongPollUpdate {
    private long ts;
    private List<List<String>> info;
    private byte MESSAGE_CODE = 4;

    public VkLongPollUpdate(long ts, @NonNull List<List<String>> info) {
        this.ts = ts;
        this.info = info;
    }

    public long getTs() {
        return ts;
    }

    public List<IEvent> getEvents() {
        List<IEvent> eventList = new ArrayList<>();
        for (List<String> stringList : info) {
            byte code = Byte.parseByte(stringList.get(0));
            if (code == MESSAGE_CODE) {
                final long id = Long.parseLong(stringList.get(1));
                final long peerId = Long.parseLong(stringList.get(3));
                final long unixDate = Long.parseLong(stringList.get(4));
                final String text = stringList.get(6);
                IReciever reciever = VkReceiverStorage.get(peerId);
                VkMessage.Builder builder = new VkMessage.Builder().setDate(unixDate)
                        .setId(id)
                        .setOut(true)
                        .setText(text);
                if (peerId > VkIdConverter.getChatPeerOffset()) {
                    builder.setChat((IChat) VkReceiverStorage.get(peerId));
                } else {
                    builder.setSender((ISender) VkReceiverStorage.get(peerId));
                }
                eventList.add(builder.build());
            }
        }
        return eventList;
    }
}
