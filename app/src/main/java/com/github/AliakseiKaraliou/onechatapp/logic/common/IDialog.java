package com.github.AliakseiKaraliou.onechatapp.logic.common;

import java.util.List;

public interface IDialog{

    List<IMessage> getMessages();

    ISender getSender();

    void add(IMessage message);

    void add(List<IMessage> messages);

}
