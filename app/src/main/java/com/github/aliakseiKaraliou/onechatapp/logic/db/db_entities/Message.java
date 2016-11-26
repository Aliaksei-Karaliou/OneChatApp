package com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;

import java.util.ArrayList;
import java.util.List;

public class Message {
    @DbType(type = DbType.Type.INTEGER)
    private long messageId;
    @DbType(type = DbType.Type.INTEGER)
    private long chatId;
    @DbType(type = DbType.Type.INTEGER)
    private long senderId;
    @DbType(type = DbType.Type.TEXT)
    private String message;
    @DbType(type = DbType.Type.INTEGER)
    private long unixDate;

    private Message(long messageId, long chatId, long senderId, String message, long unixDate) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderId = senderId;
        this.message = message;
        this.unixDate = unixDate;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getChatId() {
        return chatId;
    }

    public long getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public long getUnixDate() {
        return unixDate;
    }

    public static List<Message> convert(List<IMessage> list) {
        List<Message> result = new ArrayList<>();
        for (IMessage message : list) {
            long messageId = message.getId();
            long chatId = message.getChat() != null ? message.getChat().getId() : 0;
            long senderId = message.getSender().getId();
            String messageText = message.getText();
            long unixDate = message.getUnixDate();
            Message newMessage = new Message(messageId, chatId, senderId, messageText, unixDate);
            result.add(newMessage);
        }
        return result;
    }
}
