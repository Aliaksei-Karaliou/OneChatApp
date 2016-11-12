package com.example.korol.onechatapp.logic.vk.entities;

import android.os.Parcel;

import com.example.korol.onechatapp.logic.common.IUser;
import com.example.korol.onechatapp.logic.common.enums.SenderType;
import com.example.korol.onechatapp.logic.common.enums.SocialNetwork;
import com.example.korol.onechatapp.logic.vk.VkInfo;
import com.example.korol.onechatapp.logic.vk.storages.VkIdToUserStorage;

public class VkUser implements IUser {

    private long id;
    private static IUser me = null;
    private String firstName = "";
    private String lastName = "";
    private String photoUrl;

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
    public IUser getMe() {
        if (me == null)
            me = VkIdToUserStorage.getUser(VkInfo.getUserId());
        return me;
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
        return SenderType.USER;
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
