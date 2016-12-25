package com.github.aliakseiKaraliou.onechatapp.logic.vk;

public interface IVkMessageFlagsMethods {

    void addFlag(VkMessageFlag flag);

    void deleteFlag(VkMessageFlag flag);

    int getIntFlags();
}
