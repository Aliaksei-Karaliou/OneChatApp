package com.example.korol.onechatapp.logic.vk.getMethods;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.example.korol.onechatapp.logic.common.IDialog;
import com.example.korol.onechatapp.logic.common.IMessage;
import com.example.korol.onechatapp.logic.common.ISender;
import com.example.korol.onechatapp.logic.vk.VkRequester;
import com.example.korol.onechatapp.logic.vk.entities.VkDialog;
import com.example.korol.onechatapp.logic.vk.json.VkDialogJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VkGetDialog {

    public static IDialog getDialog(@NonNull ISender sender) {
        List<IMessage> list = new ArrayList<>();
        try {
            String responce = new VkRequester("messages.getHistory", new Pair<String, String>("user_id", Long.toString(sender.getId()))).execute(null);
            list = new VkDialogJsonParser().execute(responce);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new VkDialog(sender, list);
    }
}
