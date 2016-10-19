package com.example.korol.onechatapp.logic.common;

/**
 * Created by korol on 19-Oct-16.
 */

public class AuthIndormation {
    public boolean isVkAuth() {
        return vkAuth;
    }

    public void setVkAuth(boolean vkAuth) {
        AuthIndormation.vkAuth = vkAuth;
    }

    private static boolean vkAuth;
}
