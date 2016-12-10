package com.github.aliakseiKaraliou.onechatapp.logic.vk.models;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IDialog;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;

import java.util.ArrayList;
import java.util.List;

public class VkDialog implements IDialog {

    private List<IMessage> list;
    private IReceiver reciever;

    public VkDialog(IReceiver reciever) {
        this.reciever = reciever;
        list=new ArrayList<>();
    }

    public VkDialog(IReceiver reciever, List<IMessage> list) {
        this.list = list;
        this.reciever = reciever;
    }

    @Override
    public List<IMessage> getMessages() {
        return list;
    }

    @Override
    public IReceiver getReciever() {
        return reciever;
    }

    @Override
    public void add(IMessage message) {
        list.add(message);
    }

    @Override
    public void add(List<IMessage> messages) {
        list.addAll(messages);
    }

}
