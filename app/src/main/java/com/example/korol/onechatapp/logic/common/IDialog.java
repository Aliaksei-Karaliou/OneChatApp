package com.example.korol.onechatapp.logic.common;

import java.util.List;

public interface IDialog{

    List<IMessage> getMessages();

    ISender getSender();

    void add(IMessage message);

}
