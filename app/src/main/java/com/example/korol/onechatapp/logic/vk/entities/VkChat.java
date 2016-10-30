package com.example.korol.onechatapp.logic.vk.entities;

import com.example.korol.onechatapp.logic.common.IChat;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.VkUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VkChat implements IChat {

    private List<VkUser> userList;
    private List<IMessage> messageList;
    private long id = -1;
    private String name = "";
    private String photoUrl = null;

    public VkChat(int id, String name, String photoUrl, List<VkUser> userList) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.userList = userList;
        this.messageList = new ArrayList<>();

    }

    public VkChat(int id, String name, String photoUrl) {
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<VkUser> getUserList() {
        return userList;
    }

    @Override
    public void addUser(VkUser user) {
        userList.add(user);
    }

    @Override
    public void addUsers(List<VkUser> users) {
        userList.addAll(users);
    }

    @Override
    public void addUsers(VkUser... users) {
        userList.addAll(Arrays.asList(users));
    }

    @Override
    public boolean deleteUser(VkUser user) {
        return userList.remove(user);
    }

    @Override
    public boolean containsUser(VkUser user) {
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
