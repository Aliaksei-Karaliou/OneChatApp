package com.github.aliakseiKaraliou.onechatapp.logic.vk.entities;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IDialog;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;

import java.util.ArrayList;
import java.util.List;

public class VkDialog implements IDialog {

    private List<IMessage> list;
    private IReciever reciever;

    public VkDialog(IReciever reciever) {
        this.reciever = reciever;
        list=new ArrayList<>();
    }

    public VkDialog(IReciever reciever, List<IMessage> list) {
        this.list = list;
        this.reciever = reciever;
    }

    @Override
    public List<IMessage> getMessages() {
        return list;
    }

    @Override
    public IReciever getReciever() {
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
