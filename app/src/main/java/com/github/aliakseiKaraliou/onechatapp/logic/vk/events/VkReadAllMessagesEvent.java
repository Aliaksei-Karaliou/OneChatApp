package com.github.aliakseiKaraliou.onechatapp.logic.vk.events;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;


public class VkReadAllMessagesEvent implements IEvent {

    private final IMessage finalMessage;
    private final IReceiver receiver;

    public VkReadAllMessagesEvent(final IMessage finalMessage) {
        this.finalMessage = finalMessage;
        this.receiver = finalMessage.getReceiver();
    }

    public IMessage getFinalMessage() {
        return finalMessage;
    }

    public IReceiver getReceiver() {
        return receiver;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(this.finalMessage, flags);
        dest.writeParcelable(this.receiver, flags);
    }

    protected VkReadAllMessagesEvent(final Parcel in) {
        this.finalMessage = in.readParcelable(IMessage.class.getClassLoader());
        this.receiver = in.readParcelable(IReceiver.class.getClassLoader());
    }

    public static final Creator<VkReadAllMessagesEvent> CREATOR = new Creator<VkReadAllMessagesEvent>() {
        @Override
        public VkReadAllMessagesEvent createFromParcel(final Parcel source) {
            return new VkReadAllMessagesEvent(source);
        }

        @Override
        public VkReadAllMessagesEvent[] newArray(final int size) {
            return new VkReadAllMessagesEvent[size];
        }
    };
}
