package com.github.AliakseiKaraliou.onechatapp.logic.common;

public interface IUser extends ISender {
    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);
}
