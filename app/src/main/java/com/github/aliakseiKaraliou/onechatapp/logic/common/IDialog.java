package com.github.aliakseiKaraliou.onechatapp.logic.common;

import java.util.List;

public interface IDialog {
    IReceiver getReceiver();

    List<IMessage> getAllMessages();

    int getMessageCount();

    void setMessageCount(int count);

    void addMessages(List<IMessage> messageList);

    void addMessages(int position, List<IMessage> messageList);

    void addMessage(IMessage message);

    void addMessage(int position, IMessage message);
}

