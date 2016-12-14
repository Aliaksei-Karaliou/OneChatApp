package com.github.aliakseiKaraliou.onechatapp.logic.vk.models;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;


public class VkChat implements IChat {

    private long id = -1;
    private String name = "";
    private String photo50Url = null;
    private String photo100Url = null;

    public VkChat(long id, String name, String photo50Url, String photo100Url) {
        if (id < VkIdConverter.getChatPeerOffset())
            this.id = id + 2000000000;
        else
            this.id = id;
        this.name = name;
        this.photo50Url = photo50Url;
        this.photo100Url = photo100Url;
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPhoto50Url() {
        return photo50Url;
    }

    @Override
    public SocialNetwork getSocialNetwork() {
        return SocialNetwork.VK;
    }

    @Override
    public ReceiverType getReceiverType() {
        return ReceiverType.CHAT;
    }

    @Override
    public boolean isEquals(IReceiver receiver) {
        return getSocialNetwork() == receiver.getSocialNetwork() && getReceiverType() == receiver.getReceiverType() && receiver.getId() == getId();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPhoto100Url() {
        return photo100Url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.photo50Url);
        dest.writeString(this.photo100Url);
    }

    protected VkChat(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.photo50Url = in.readString();
        this.photo100Url = in.readString();
    }

    public static final Creator<VkChat> CREATOR = new Creator<VkChat>() {
        @Override
        public VkChat createFromParcel(Parcel source) {
            return new VkChat(source);
        }

        @Override
        public VkChat[] newArray(int size) {
            return new VkChat[size];
        }
    };
}
