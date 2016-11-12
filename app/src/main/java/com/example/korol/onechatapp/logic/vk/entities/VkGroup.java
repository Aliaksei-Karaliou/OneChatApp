package com.example.korol.onechatapp.logic.vk.entities;

import android.os.Parcel;

import com.example.korol.onechatapp.logic.common.IGroup;
import com.example.korol.onechatapp.logic.common.enums.SenderType;
import com.example.korol.onechatapp.logic.common.enums.SocialNetwork;

public final class VkGroup implements IGroup {

    private long id;
    private String name;
    private String photoUrl;

    public VkGroup(long id, String name, String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
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
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public SocialNetwork getSocialNetwork() {
        return SocialNetwork.Vk;
    }

    @Override
    public SenderType getSenderType() {
        return SenderType.GROUP;
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

    public VkGroup() {
    }

    protected VkGroup(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.photoUrl = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VkGroup && ((VkGroup) obj).id==id;
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
