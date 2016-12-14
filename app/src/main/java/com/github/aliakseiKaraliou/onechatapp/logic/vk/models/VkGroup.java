package com.github.aliakseiKaraliou.onechatapp.logic.vk.models;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public final class VkGroup implements IGroup {

    private long id;
    private String name;
    private String photo50Url;
    private String photo100Url;


    @Override
    public String getPhoto50Url() {
        return photo50Url;
    }
    @Override
    public String getPhoto100Url() {
        return photo100Url;
    }

    public VkGroup(long id, String name, String photo50Url, String photo100Url) {
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
    public SocialNetwork getSocialNetwork() {
        return SocialNetwork.VK;
    }

    @Override
    public ReceiverType getReceiverType() {
        return ReceiverType.GROUP;
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

    protected VkGroup(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.photo50Url = in.readString();
        this.photo100Url = in.readString();
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
