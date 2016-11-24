package com.github.aliakseiKaraliou.onechatapp.logic.vk.entities;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.ISender;

import java.util.Date;

public class VkMessage implements IMessage {

    private ISender sender;
    private long id;
    private String text;
    private Date date;
    private IChat chat;

    private VkMessage(long id, ISender sender, String text, Date date, boolean isRead, IChat chat) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.date = date;
        this.isRead = isRead;
        this.chat = chat;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getText() {
        if (text != null)
            return text;
        else
            return "Some group";
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public ISender getSender() {
        return sender;
    }

    public boolean isRead() {
        return isRead;
    }

    public IMessage read() {
        isRead = true;
        return this;
    }

    @Override
    public IReciever getReciever() {
        if (chat != null)
            return chat;
        else
            return sender;
    }

    private boolean isRead = false;

    public static class Builder {

        private long id;
        private Date date;
        private String text;
        private ISender sender;
        private boolean read;
        private IChat chat;


        public void setChat(IChat chat) {
            this.chat = chat;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setRead(boolean read) {
            this.read = read;
            return this;
        }

        public Builder setText(String text) {
            if (text != null)
                this.text = text;
            else
                this.text = "";
            return this;
        }

        public Builder setSender(ISender sender) {
            this.sender = sender;
            return this;
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder setDate(long timeStamp) {
            this.date = new Date(timeStamp * 1000);
            return this;
        }

        public VkMessage build() {
            return new VkMessage(id, sender, text, date, read, chat);
        }
    }
}
