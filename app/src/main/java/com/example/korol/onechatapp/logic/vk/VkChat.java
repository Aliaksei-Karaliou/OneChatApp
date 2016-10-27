package com.example.korol.onechatapp.logic.vk;

import com.example.korol.onechatapp.logic.common.IChat;
import com.example.korol.onechatapp.logic.common.IUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VkChat implements IChat {

    public VkChat(int id, String name, String photoUrl, List<IUser> userList) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.userList = userList;
    }

    private int id = -1;
    private String name = "";
    private String photoUrl = null;

    private List<IUser> userList = new ArrayList<>();

    @Override
    public int getId() {
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
}
