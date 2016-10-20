package com.example.korol.onechatapp.logic.vk;

import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.ISender;

import java.util.Date;

public class VkMessage implements IMessage {
    private ISender sender;

    @Override
    public ISender getSender() {
        return sender;
    }

    private String text;

    @Override
    public String getText() {
        return text;
    }

    private Date date;

    @Override
    public Date getDate() {
        return date;
    }

    private VkMessage(ISender sender, String text, Date date) {
        this.sender = sender;
        this.text = text;
        this.date = date;
    }

    private VkMessage() {
    }

    public boolean isRead() {
        return isRead;
    }

    public IMessage read() {
        isRead = true;
        return this;
    }

    private boolean isRead = false;

    public static class Builder {

        private Date date;
        private String text;
        private ISender sender;

        public Builder setText(String text) {
            this.text = text;
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

        public VkMessage build() {
            return new VkMessage(sender, text, date);
        }
    }
}
