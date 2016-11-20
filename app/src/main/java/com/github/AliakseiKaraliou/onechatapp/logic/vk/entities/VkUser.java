package com.github.AliakseiKaraliou.onechatapp.logic.vk.entities;

import android.os.Parcel;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.ReceiverType;
import com.github.AliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public class VkUser implements IUser {
    private long id;

    public VkUser(int id, String firstName, String lastName, String photoUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl;
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

    private String firstName = "";

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String lastName = "";

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String photoUrl;

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
    public ReceiverType getReceiverType() {
        return ReceiverType.USER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.photoUrl);
    }

    protected VkUser(Parcel in) {
        this.id = in.readLong();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.photoUrl = in.readString();
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