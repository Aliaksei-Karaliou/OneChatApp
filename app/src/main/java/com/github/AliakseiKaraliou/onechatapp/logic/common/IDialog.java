package com.github.AliakseiKaraliou.onechatapp.logic.common;

import java.util.List;

public interface IDialog{

    List<IMessage> getMessages();

    IReciever getReciever();

    void add(IMessage message);

    void add(List<IMessage> messages);

}
