package com.github.aliakseiKaraliou.onechatapp.logic.vk.events.messageFlags;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkMessageFlag;

public class VkAddMessageFlagEvent implements IEvent {
    private final IMessage message;
    private final VkMessageFlag messageFlag;

    public IMessage getMessage() {
        return message;
    }

    public VkMessageFlag getMessageFlag() {
        return messageFlag;
    }

    public VkAddMessageFlagEvent(final IMessage message, final VkMessageFlag messageFlag) {
        this.message = message;
        this.messageFlag = messageFlag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(this.message, flags);
        dest.writeInt(this.messageFlag == null ? -1 : this.messageFlag.ordinal());
    }

    protected VkAddMessageFlagEvent(final Parcel in) {
        this.message = in.readParcelable(IMessage.class.getClassLoader());
        final int tmpMessageFlag = in.readInt();
        this.messageFlag = tmpMessageFlag == -1 ? null : VkMessageFlag.values()[tmpMessageFlag];
    }

    public static final Creator<VkAddMessageFlagEvent> CREATOR = new Creator<VkAddMessageFlagEvent>() {
        @Override
        public VkAddMessageFlagEvent createFromParcel(final Parcel source) {
            return new VkAddMessageFlagEvent(source);
        }

        @Override
        public VkAddMessageFlagEvent[] newArray(final int size) {
            return new VkAddMessageFlagEvent[size];
        }
    };
}
