package com.github.aliakseiKaraliou.onechatapp.logic.vk.models;

import android.os.Parcel;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public class VkUser implements IUser {
    private long id;
    private String photo50Url;
    private String photo100Url;
    private String firstName = "";
    private String lastName = "";

    public VkUser(long id, String firstName, String lastName, String photo50Url, String photo100Url) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo50Url = photo50Url;
        this.photo100Url = photo100Url;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else return "";
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Override
    public String getPhoto50Url() {
        return photo50Url;
    }

    @Override
    public String getPhoto100Url() {
        return photo100Url;
    }

    @Override
    public SocialNetwork getSocialNetwork() {
        return SocialNetwork.VK;
    }

    @Override
    public ReceiverType getReceiverType() {
        return ReceiverType.USER;
    }

    @Override
    public boolean isEquals(IReceiver receiver) {
        return getSocialNetwork() == receiver.getSocialNetwork() && getReceiverType() == receiver.getReceiverType() && receiver.getId() == getId();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.photo100Url);
        dest.writeString(this.photo50Url);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
    }

    protected VkUser(Parcel in) {
        this.id = in.readLong();
        this.photo100Url = in.readString();
        this.photo50Url = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
    }

    public static final Creator<VkUser> CREATOR = new Creator<VkUser>() {
        @Override
        public VkUser createFromParcel(Parcel source) {
            return new VkUser(source);
        }

        @Override
        public VkUser[] newArray(int size) {
            return new VkUser[size];
        }
    };
}