package com.github.aliakseiKaraliou.onechatapp.logic.vk.events.messageFlags;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkMessageFlag;

import java.util.Set;

public class VkDeleteMessageFlagEvent implements IEvent {
    private final IMessage message;
    private final VkMessageFlag messageFlag;

    public VkDeleteMessageFlagEvent(IMessage message, VkMessageFlag messageFlag) {
        this.message = message;
        this.messageFlag = messageFlag;
    }

    public IMessage getMessage() {
        return message;
    }

    public VkMessageFlag getMessageFlag() {
        return messageFlag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.message, flags);
        dest.writeInt(this.messageFlag == null ? -1 : this.messageFlag.ordinal());
    }

    protected VkDeleteMessageFlagEvent(Parcel in) {
        this.message = in.readParcelable(IMessage.class.getClassLoader());
        int tmpMessageFlag = in.readInt();
        this.messageFlag = tmpMessageFlag == -1 ? null : VkMessageFlag.values()[tmpMessageFlag];
    }

    public static final Creator<VkDeleteMessageFlagEvent> CREATOR = new Creator<VkDeleteMessageFlagEvent>() {
        @Override
        public VkDeleteMessageFlagEvent createFromParcel(Parcel source) {
            return new VkDeleteMessageFlagEvent(source);
        }

        @Override
        public VkDeleteMessageFlagEvent[] newArray(int size) {
            return new VkDeleteMessageFlagEvent[size];
        }
    };
}
