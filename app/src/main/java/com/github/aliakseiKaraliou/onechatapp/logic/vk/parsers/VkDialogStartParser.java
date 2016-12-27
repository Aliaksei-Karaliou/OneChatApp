package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IReceiver;
import com.github.aliakseiKaraliou.onechatapp.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.storages.VkReceiverStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class VkDialogStartParser implements IParser<String, Set<Long>> {
    @Nullable
    @Override
    public Set<Long> parse(String jsonString) {
        try {
            Set<Long> set = new HashSet<>();
            JSONArray items = new JSONObject(jsonString).getJSONObject(Constants.Json.RESPONSE).getJSONArray(Constants.Json.ITEMS);
            JSONObject currentObject;
            for (int i = 0; i < items.length(); i++) {
                currentObject = items.getJSONObject(i);
                final long fromId = currentObject.getLong(Constants.Json.FROM_ID);
                final IReceiver receiver = VkReceiverStorage.get(fromId);
                if (receiver == null) {
                    set.add(fromId);
                }
                if (currentObject.has(Constants.Json.ACTION_MID)) {
                    final long actionPeer = currentObject.getLong(Constants.Json.ACTION_MID);
                    final IReceiver actionReceiver = VkReceiverStorage.get(actionPeer);
                    if (actionReceiver == null) {
                        set.add(actionPeer);
                    }
                }
            }
            return set;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}