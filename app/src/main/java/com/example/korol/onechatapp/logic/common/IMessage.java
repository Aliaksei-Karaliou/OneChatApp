package com.example.korol.onechatapp.logic.common;

public interface IMessage {
    ISender getSender();

    void setSender(ISender sender);

    String getText();

    void setText(String text);
}
