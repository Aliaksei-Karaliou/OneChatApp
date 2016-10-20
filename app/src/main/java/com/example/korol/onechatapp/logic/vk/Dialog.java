package com.example.korol.onechatapp.logic.vk;

import com.example.korol.onechatapp.logic.common.IDialog;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.ISender;

import java.util.List;

public class Dialog implements IDialog {
    @Override
    public ISender getInterlocutor() {
        return interlocutor;
    }

    @Override
    public void setInterlocutor(ISender interlocutor) {
        this.interlocutor = interlocutor;
    }

    private ISender interlocutor;

    @Override
    public List<IMessage> getMessages() {
        return allMessages;
    }

    @Override
    public void add(IMessage message) {
        allMessages.add(message);

    }

    private List<IMessage> allMessages;
}
