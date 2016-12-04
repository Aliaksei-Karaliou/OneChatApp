package com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;

import java.util.ArrayList;
import java.util.List;

public class DbMessage extends DbEntity<DbMessage> {
    @DbPrimaryKey
    @DbType(type = DbType.Type.INTEGER)
    private long messageId;
    @DbType(type = DbType.Type.INTEGER)
    private long peerId;
    @DbType(type = DbType.Type.INTEGER)
    private long fromId;
    @DbType(type = DbType.Type.TEXT)
    private String message;
    @DbType(type = DbType.Type.INTEGER)
    private long unixDate;
    @DbType(type = DbType.Type.INTEGER)
    private long isOut;

    private DbMessage(long messageId, long peerId, long fromId, String message, long unixDate, boolean isOut) {
        this.messageId = messageId;
        this.peerId = peerId;
        this.fromId = fromId;
        this.message = message;
        this.unixDate = unixDate;
        this.isOut = isOut ? 1 : 0;
    }

    private DbMessage(){

    }


    public static List<DbMessage> convertTo(List<IMessage> list) {
        List<DbMessage> result = new ArrayList<>();
        for (IMessage message : list) {
            result.add(convertTo(message));
        }
        return result;
    }

    public static DbMessage convertTo(IMessage message) {
        long messageId = message.getId();
        long senderId = message.getSender().getId();
        long chatId = message.getChat() != null ? message.getChat().getId() : senderId;
        String messageText = message.getText();
        long unixDate = message.getUnixDate();
        boolean isOut = message.isOut();
        return new DbMessage(messageId, chatId, senderId, messageText, unixDate, isOut);
    }

    @Override
    protected DbMessage convert(List<String> list) {
        final long fromId = Long.parseLong(list.get(0));
        final long peerId = Long.parseLong(list.get(1));
        final String message = list.get(2);
        final long unixDate = Long.parseLong(list.get(3));
        final long isOut = Long.parseLong(list.get(4));
        final long messageId = Long.parseLong(list.get(5));
        return new DbMessage(messageId, peerId, fromId, message, unixDate, isOut > 0);
    }
}
