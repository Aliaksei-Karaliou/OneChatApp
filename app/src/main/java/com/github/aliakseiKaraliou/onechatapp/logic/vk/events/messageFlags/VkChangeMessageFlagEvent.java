package com.github.aliakseiKaraliou.onechatapp.logic.vk.events.messageFlags;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IEvent;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkMessageFlag;

public class VkChangeMessageFlagEvent implements IEvent {

    private final IMessage message;
    private final VkMessageFlag flag;

    public VkChangeMessageFlagEvent(IMessage message, VkMessageFlag flag) {
        this.message = message;
        this.flag = flag;
    }

    public IMessage getMessage() {
        return message;
    }

    public VkMessageFlag getFlag() {
        return flag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.message, flags);
        dest.writeInt(this.flag == null ? -1 : this.flag.ordinal());
    }

    protected VkChangeMessageFlagEvent(Parcel in) {
        this.message = in.readParcelable(IMessage.class.getClassLoader());
        int tmpFlag = in.readInt();
        this.flag = tmpFlag == -1 ? null : VkMessageFlag.values()[tmpFlag];
    }

    public static final Creator<VkChangeMessageFlagEvent> CREATOR = new Creator<VkChangeMessageFlagEvent>() {
        @Override
        public VkChangeMessageFlagEvent createFromParcel(Parcel source) {
            return new VkChangeMessageFlagEvent(source);
        }

        @Override
        public VkChangeMessageFlagEvent[] newArray(int size) {
            return new VkChangeMessageFlagEvent[size];
        }
    };
}
