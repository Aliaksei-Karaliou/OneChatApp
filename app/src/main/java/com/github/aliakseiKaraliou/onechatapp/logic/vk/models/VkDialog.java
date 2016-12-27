package com.github.aliakseiKaraliou.onechatapp.logic.vk.models;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IDialog;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;

import java.util.ArrayList;
import java.util.List;

public class VkDialog implements IDialog {

    private final IReceiver receiver;
    private final List<IMessage> messageList;
    private int messageCount = -1;

    public VkDialog(final IReceiver receiver) {
        this.receiver = receiver;
        this.messageList = new ArrayList<>();
    }

    @Override
    public IReceiver getReceiver() {
        return receiver;
    }

    @Override
    public List<IMessage> getAllMessages() {
        return messageList;
    }

    @Override
    public int getMessageCount() {
        return messageCount > 0 ? messageCount : messageList.size();
    }

    @Override
    public void setMessageCount(final int count) {
        if (count >= messageList.size()) {
            messageCount = count;
        }
    }

    @Override
    public void addMessages(final List<IMessage> messageList) {
        this.messageList.addAll(messageList);
    }

    @Override
    public void addMessages(final int position, final List<IMessage> messageList) {
        this.messageList.addAll(position, messageList);
    }

    @Override
    public void addMessage(final IMessage message) {
        this.messageList.add(message);
    }

    @Override
    public void addMessage(final int position, final IMessage message) {
        this.messageList.add(position, message);
    }


}
