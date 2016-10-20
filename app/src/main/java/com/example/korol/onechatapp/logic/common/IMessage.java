package com.example.korol.onechatapp.logic.common;

import java.util.Date;

public interface IMessage {
    ISender getSender();

    String getText();

    Date getDate();

    boolean isRead();

    IMessage read();
}

