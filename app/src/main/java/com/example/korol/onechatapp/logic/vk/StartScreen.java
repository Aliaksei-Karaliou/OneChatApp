package com.example.korol.onechatapp.logic.vk;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.IStartScreen;

import java.util.List;

public class StartScreen implements IStartScreen {
    private List<IMessage> messages;

    @Override
    public List<IMessage> getMessages() {
        return messages;
    }

    @Override
    public void setMessages(List<IMessage> allMessages) {
        this.messages = messages;
    }

    @Override
    public void add(IMessage message) {
        messages.add(message);
    }
}
