package com.github.aliakseiKaraliou.onechatapp.logic.vk.models;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.ISender;

import java.util.Date;

public class VkMessage implements IMessage {

    private ISender sender;
    private long id;
    private String text;
    private Date date;
    private IChat chat;
    private boolean isRead = false;
    private boolean out = false;

    private VkMessage(long id, ISender sender, String text, Date date, boolean isRead, boolean out, IChat chat) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.date = date;
        this.isRead = isRead;
        this.out = out;
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
    public long getUnixDate() {
        return date.getTime() / 1000;
    }

    @Override
    public ISender getSender() {
        return sender;
    }

    public boolean isRead() {
        return isRead;
    }

    @Override
    public boolean isOut() {
        return out;
    }

    public IMessage read() {
        isRead = true;
        return this;
    }

    @Override
    public IReceiver getReceiver() {
        if (chat != null)
            return chat;
        else
            return sender;
    }

    @Override
    @Nullable
    public IChat getChat() {
        return chat;
    }

    public static class Builder {

        private long id;
        private Date date;
        private String text;
        private ISender sender;
        private boolean read;
        private boolean out;
        private IChat chat;

        public Builder setOut(boolean out) {
            this.out = out;
            return this;
        }


        public Builder setChat(IChat chat) {
            this.chat = chat;
            return this;
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

        public Builder setDate(long timeStamp) {
            this.date = new Date(timeStamp * 1000);
            return this;
        }

        public VkMessage build() {
            return new VkMessage(id, sender, text, date, read, out, chat);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.sender, flags);
        dest.writeLong(this.id);
        dest.writeString(this.text);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeParcelable(this.chat, flags);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeByte(this.out ? (byte) 1 : (byte) 0);
    }

    protected VkMessage(Parcel in) {
        this.sender = in.readParcelable(ISender.class.getClassLoader());
        this.id = in.readLong();
        this.text = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.chat = in.readParcelable(IChat.class.getClassLoader());
        this.isRead = in.readByte() != 0;
        this.out = in.readByte() != 0;
    }

    public static final Creator<VkMessage> CREATOR = new Creator<VkMessage>() {
        @Override
        public VkMessage createFromParcel(Parcel source) {
            return new VkMessage(source);
        }

        @Override
        public VkMessage[] newArray(int size) {
            return new VkMessage[size];
        }
    };
}
