package com.github.aliakseiKaraliou.onechatapp.logic.vk.models;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.IVkMessageFlagsMethods;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkMessageFlag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VkMessage implements IMessage, IVkMessageFlagsMethods {

    private final ISender sender;
    private final long id;
    private final String text;
    private final Date date;
    private final IChat chat;
    private boolean isRead = false;
    private boolean out = false;
    private final List<VkMessageFlag> flags;

    private VkMessage(final long id, final ISender sender, final String text, final Date date, final boolean isRead, final boolean out, final IChat chat) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.date = date;
        this.isRead = isRead;
        this.out = out;
        this.chat = chat;
        flags = new ArrayList<>();
        if (!isRead) {
            flags.add(VkMessageFlag.UNREAD);
        }
    }

    @Override
    public void addFlag(final VkMessageFlag flag) {
        flags.add(flag);
        if (flag == VkMessageFlag.UNREAD) {
            isRead = false;
        }
    }

    @Override
    public void deleteFlag(final VkMessageFlag flag) {
        flags.remove(flag);
        if (flag == VkMessageFlag.UNREAD) {
            isRead = true;
        }
    }

    @Override
    public int getIntFlags() {
        int flagsValue = 0;
        for (final VkMessageFlag flag : flags) {
            flagsValue += flag.getKey();
        }
        return flagsValue;
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
            return "";
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

    @Override
    public boolean isEquals(final IMessage message) {
        return id == message.getId();
    }

    public static class Builder {

        private long id;
        private Date date;
        private String text;
        private ISender sender;
        private boolean read;
        private boolean out;
        private IChat chat;

        public Builder setOut(final boolean out) {
            this.out = out;
            return this;
        }


        public Builder setChat(final IChat chat) {
            this.chat = chat;
            return this;
        }

        public Builder setId(final long id) {
            this.id = id;
            return this;
        }

        public Builder setRead(final boolean read) {
            this.read = read;
            return this;
        }

        public Builder setText(final String text) {
            if (text != null)
                this.text = text;
            else
                this.text = "";
            return this;
        }

        public Builder setSender(final ISender sender) {
            this.sender = sender;
            return this;
        }

        public Builder setDate(final long timeStamp) {
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
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(this.sender, flags);
        dest.writeLong(this.id);
        dest.writeString(this.text);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeParcelable(this.chat, flags);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeByte(this.out ? (byte) 1 : (byte) 0);
        dest.writeList(this.flags);
    }

    protected VkMessage(final Parcel in) {
        this.sender = in.readParcelable(ISender.class.getClassLoader());
        this.id = in.readLong();
        this.text = in.readString();
        final long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.chat = in.readParcelable(IChat.class.getClassLoader());
        this.isRead = in.readByte() != 0;
        this.out = in.readByte() != 0;
        this.flags = new ArrayList<>();
        in.readList(this.flags, VkMessageFlag.class.getClassLoader());
    }

    public static final Creator<VkMessage> CREATOR = new Creator<VkMessage>() {
        @Override
        public VkMessage createFromParcel(final Parcel source) {
            return new VkMessage(source);
        }

        @Override
        public VkMessage[] newArray(final int size) {
            return new VkMessage[size];
        }
    };
}
