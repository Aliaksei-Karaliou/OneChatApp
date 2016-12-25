package com.github.aliakseiKaraliou.onechatapp.logic.vk.events;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;


public class VkAddNewMessageEvent implements IEvent {

    private final IMessage message;

    public VkAddNewMessageEvent(final IMessage message) {
        this.message = message;
    }

    public IMessage getMessage() {
        return message;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(this.message, flags);
    }

    protected VkAddNewMessageEvent(final Parcel in) {
        this.message = in.readParcelable(IMessage.class.getClassLoader());
    }

    public static final Creator<VkAddNewMessageEvent> CREATOR = new Creator<VkAddNewMessageEvent>() {
        @Override
        public VkAddNewMessageEvent createFromParcel(final Parcel source) {
            return new VkAddNewMessageEvent(source);
        }

        @Override
        public VkAddNewMessageEvent[] newArray(final int size) {
            return new VkAddNewMessageEvent[size];
        }
    };
}
