package com.github.AliakseiKaraliou.onechatapp.logic.common;

import com.github.AliakseiKaraliou.onechatapp.logic.db.annotations.DbTableName;

import java.util.ArrayList;
import java.util.List;

@DbTableName(name = "StartScreen")
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
