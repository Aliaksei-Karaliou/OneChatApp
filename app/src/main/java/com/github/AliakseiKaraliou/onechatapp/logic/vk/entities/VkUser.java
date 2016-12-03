package com.github.aliakseiKaraliou.onechatapp.logic.vk.entities;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.PeerRecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public class VkUser implements IUser {
    private long id;

    public VkUser(long id, java.lang.String firstName, java.lang.String lastName, java.lang.String photoUrl) {
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
    public java.lang.String getName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else return "";
    }

    private java.lang.String firstName = "";

    @Override
    public java.lang.String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }

    private java.lang.String lastName = "";

    @Override
    public java.lang.String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    private java.lang.String photoUrl;

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
        return PeerRecieverType.USER;
    }
}