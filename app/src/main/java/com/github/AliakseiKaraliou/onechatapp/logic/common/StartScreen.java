package com.github.aliakseiKaraliou.onechatapp.logic.common;


import java.util.ArrayList;
import java.util.List;

public final class StartScreen {
    private List<IMessage> messages;

    public StartScreen(List<IMessage> messages) {
        this.messages = messages;
    }

    public List<IMessage> getMessages() {
        return new ArrayList<>(messages);
    }

    public void add(IMessage message) {
        messages.add(message);
    }
}
