package com.example.korol.onechatapp.logic.common;

import java.util.List;

public interface IChat extends ISender {
    void setName(String name);

    List<VkUser> getUserList();

    void addUser(VkUser user);

    void addUsers(List<VkUser> users);

    void addUsers(VkUser... users);

    boolean deleteUser(VkUser user);

    boolean containsUser(VkUser user);

    void addMessage(IMessage message);

    void addMessages(List<IMessage> messages);
}