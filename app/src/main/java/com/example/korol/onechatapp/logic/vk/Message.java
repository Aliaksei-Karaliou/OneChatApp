package com.example.korol.onechatapp.logic.vk;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.ISender;

/**
 * Created by korol on 20-Oct-16.
 */

public class Message implements IMessage {
    private ISender sender;

    @Override
    public ISender getSender() {
        return sender;
    }

    @Override
    public void setSender(ISender sender) {
        this.sender = sender;
    }

    private String text;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
