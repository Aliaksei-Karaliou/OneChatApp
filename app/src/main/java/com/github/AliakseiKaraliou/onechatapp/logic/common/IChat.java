package com.github.AliakseiKaraliou.onechatapp.logic.common;

import java.util.List;

public interface IChat extends ISender {
    void setName(String name);

    List<IUser> getUserList();

    void addUser(IUser user);

    void addUsers(List<IUser> users);

    void addUsers(IUser... users);

    boolean deleteUser(IUser user);

    boolean containsUser(IUser user);

    void addMessage(IMessage message);

    void addMessages(List<IMessage> messages);
}