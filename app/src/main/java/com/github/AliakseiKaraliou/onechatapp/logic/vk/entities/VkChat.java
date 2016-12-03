package com.github.aliakseiKaraliou.onechatapp.logic.vk.entities;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IChat;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.PeerRecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VkChat implements IChat {

    private List<IUser> userList;
    private List<IMessage> messageList;
    private long id = -1;
    private java.lang.String name = "";
    private java.lang.String photoUrl = null;

    public VkChat(long id, java.lang.String name, java.lang.String photoUrl, List<IUser> userList) {
        if (id < 2000000000)
            this.id = id + 2000000000;
        else
            this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.userList = userList;
        this.messageList = new ArrayList<>();

    }

    public VkChat(long id, java.lang.String name, java.lang.String photoUrl) {
        this(id, name, photoUrl, new ArrayList<IUser>());
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
}
