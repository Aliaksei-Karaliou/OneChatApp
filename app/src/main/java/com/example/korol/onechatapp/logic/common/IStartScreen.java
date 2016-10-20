package com.example.korol.onechatapp.logic.common;

import java.util.List;

public interface IStartScreen {

    List<IMessage> getMessages();

    void setMessages(List<IMessage> allMessages);

    void add(IMessage message);
}
