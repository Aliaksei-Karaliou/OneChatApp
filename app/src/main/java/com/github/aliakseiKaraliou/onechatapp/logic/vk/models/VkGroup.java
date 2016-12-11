package com.github.aliakseiKaraliou.onechatapp.logic.vk.models;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.PeerRecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public final class VkGroup implements IGroup {

    private long id;
    private java.lang.String name;

    @Override
    public java.lang.String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public void setPhotoUrl(java.lang.String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private java.lang.String photoUrl;

    public VkGroup(long id, java.lang.String name, java.lang.String photoUrl) {
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
    public SocialNetwork getSocialNetwork() {
        return SocialNetwork.VK;
    }

    @Override
    public PeerRecieverType getPeerReceiverType() {
        return PeerRecieverType.GROUP;
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

    protected VkGroup(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.photoUrl = in.readString();
    }

    public static final Creator<VkGroup> CREATOR = new Creator<VkGroup>() {
        @Override
        public VkGroup createFromParcel(Parcel source) {
            return new VkGroup(source);
        }

        @Override
        public VkGroup[] newArray(int size) {
            return new VkGroup[size];
        }
    };
}
