package com.github.AliakseiKaraliou.onechatapp.logic.vk.getMethods;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IDialog;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IMessage;
import com.github.AliakseiKaraliou.onechatapp.logic.common.ISender;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.entities.VkDialog;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.json.VkDialogJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VkGetDialog {

    public static IDialog getDialog(Context context, @NonNull ISender sender) {
        return new VkDialog(sender, getMessageList(context, sender));
    }

    public static List<IMessage> getMessageList(Context context, @NonNull ISender sender, int offset) {
        List<IMessage> list = new ArrayList<>();
        try {
            String responce = new VkRequester("messages.getHistory", new Pair<String, String>("peer_id", Long.toString(sender.getId())), new Pair<String, String>("offset", Integer.toString(offset))).execute(null);
            list = new VkDialogJsonParser(context, sender.getSenderType()).execute(responce);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<IMessage> getMessageList(Context context, @NonNull ISender sender) {
        return getMessageList(context, sender, 0);
    }
}