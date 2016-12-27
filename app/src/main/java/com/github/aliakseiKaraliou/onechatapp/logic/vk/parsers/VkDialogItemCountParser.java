package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class VkDialogItemCountParser implements IParser<String, Integer> {
    @Nullable
    @Override
    public Integer parse(final String json) {
        try {
            return new JSONObject(json).getJSONObject(Constants.Json.RESPONSE).getInt(Constants.Json.COUNT);
        } catch (final JSONException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
