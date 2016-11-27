package com.github.aliakseiKaraliou.onechatapp.logic.vk.entities;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.RecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;

public final class VkGroup implements IGroup {

    private long id;
    private java.lang.String name;

    @Override
    public java.lang.String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public void setPhotoUrl(java.lang.String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private java.lang.String photoUrl;

    public VkGroup(long id, java.lang.String name, java.lang.String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
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
    public SocialNetwork getSocialNetwork() {
        return SocialNetwork.VK;
    }

    @Override
    public RecieverType getReceiverType() {
        return RecieverType.GROUP;
    }

}
