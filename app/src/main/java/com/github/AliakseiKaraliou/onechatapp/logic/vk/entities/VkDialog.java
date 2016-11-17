package com.github.AliakseiKaraliou.onechatapp.logic.vk.entities;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IDialog;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.common.ISender;

import java.util.ArrayList;
import java.util.List;

public class VkDialog implements IDialog {

    private List<IMessage> list;
    private ISender sender;

    public VkDialog(ISender sender) {
        this.sender = sender;
        list=new ArrayList<>();
    }

    public VkDialog(ISender sender, List<IMessage> list) {
        this.list = list;
        this.sender = sender;
    }

    @Override
    public List<IMessage> getMessages() {
        return list;
    }

    @Override
    public ISender getSender() {
        return sender;
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
