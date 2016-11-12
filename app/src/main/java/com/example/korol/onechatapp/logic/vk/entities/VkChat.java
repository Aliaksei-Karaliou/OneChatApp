package com.example.korol.onechatapp.logic.vk.entities;

import android.os.Parcel;

import com.example.korol.onechatapp.logic.common.IChat;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.IUser;
import com.example.korol.onechatapp.logic.common.enums.SenderType;
import com.example.korol.onechatapp.logic.common.enums.SocialNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VkChat implements IChat {

    private List<IUser> userList;
    private List<IMessage> messageList;
    private long id = -1;
    private String name = "";
    private String photoUrl = null;

    public VkChat(int id, String name, String photoUrl, List<IUser> userList) {
        if (id < 2000000000)
            this.id = id + 2000000000;
        else
            this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.userList = userList;
        this.messageList = new ArrayList<>();

    }

    public VkChat(long id, String name, String photoUrl) {
        if (id < 2000000000)
            this.id = id + 2000000000;
        else
            this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.userList = new ArrayList<>();
        this.messageList = new ArrayList<>();
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
        return SenderType.CHAT;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<IUser> getUserList() {
        return userList;
    }

    @Override
    public void addUser(IUser user) {
        userList.add(user);
    }

    @Override
    public void addUsers(List<IUser> users) {
        userList.addAll(users);
    }

    @Override
    public void addUsers(IUser... users) {
        userList.addAll(Arrays.asList(users));
    }

    @Override
    public boolean deleteUser(IUser user) {
        return userList.remove(user);
    }

    @Override
    public boolean containsUser(IUser user) {
        return userList.contains(user);
    }

    @Override
    public void addMessage(IMessage message) {
        messageList.add(message);
    }

    @Override
    public void addMessages(List<IMessage> messages) {
        messageList.addAll(messages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.userList);
        dest.writeList(this.messageList);
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.photoUrl);
    }

    protected VkChat(Parcel in) {
        this.userList = new ArrayList<>();
        userList.addAll(in.createTypedArrayList(VkUser.CREATOR));
        this.messageList = new ArrayList<IMessage>();
        in.readList(this.messageList, IMessage.class.getClassLoader());
        this.id = in.readLong();
        this.name = in.readString();
        this.photoUrl = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VkChat && ((VkChat)obj).id == id;
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
