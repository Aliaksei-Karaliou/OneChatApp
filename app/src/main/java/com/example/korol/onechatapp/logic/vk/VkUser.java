package com.example.korol.onechatapp.logic.vk;

public class VkUser implements com.example.korol.onechatapp.logic.common.VkUser {
    private long id;

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

    private String firstName = "";

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String lastName = "";

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String photoUrl;

    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
