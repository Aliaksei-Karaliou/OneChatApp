package com.example.korol.onechatapp.logic.common;

public class Authorized {
    public static boolean isVkAuthorized() {
        return vkAuthorized;
    }

    public static void setVkAuthorized(boolean vkAuthorized) {
        Authorized.vkAuthorized = vkAuthorized;
    }

    private static boolean vkAuthorized;
}
