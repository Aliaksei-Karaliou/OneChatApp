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

import java.util.List;

public class VkGetDialog {

    public static IDialog getDialog(Context context, @NonNull ISender sender) {
        return new VkDialog(sender, getMessageList(context, sender));
    }

    public static List<IMessage> getMessageList(Context context, @NonNull ISender sender, int offset) {
        String responce = new VkRequester("messages.getHistory", new Pair<String, String>("peer_id", Long.toString(sender.getId())), new Pair<String, String>("offset", Integer.toString(offset))).execute(null);
        if (!responce.contains("Error"))
            return new VkDialogJsonParser(context, sender.getSenderType()).execute(responce);
        else
            return null;
    }

    public static List<IMessage> getMessageList(Context context, @NonNull ISender sender) {
        return getMessageList(context, sender, 0);
    }
}
