package com.github.aliakseiKaraliou.onechatapp.logic.vk.models;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.PeerRecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VkChat implements IChat {

    private long id = -1;
    private java.lang.String name = "";
    private java.lang.String photoUrl = null;

    public VkChat(long id, String name, java.lang.String photoUrl) {
        if (id < 2000000000)
            this.id = id + 2000000000;
        else
            this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public java.lang.String getName() {
        return name;
    }

    @Override
    public java.lang.String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public void setPhotoUrl(java.lang.String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public SocialNetwork getSocialNetwork() {
        return SocialNetwork.VK;
    }

    @Override
    public PeerRecieverType getPeerReceiverType() {
        return PeerRecieverType.CHAT;
    }

    @Override
    public void setName(java.lang.String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.photoUrl);
    }

    protected VkChat(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.photoUrl = in.readString();
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
