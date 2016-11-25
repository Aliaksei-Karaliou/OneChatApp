package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.entities.VkUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VkUserDataParser implements IParser<String, LongSparseArray<IUser>> {
    @Nullable
    @Override
    public LongSparseArray<IUser> parse(String json) {
        LongSparseArray<IUser> userLongSparseArray = new LongSparseArray<>();
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(VkConstants.Json.RESPONSE);
            JSONObject currentObject;
            long id;
            String firstName, lastName, photoUrl;
            for (int i = 0; i < jsonArray.length(); i++) {
                currentObject = jsonArray.getJSONObject(i);
                id = currentObject.getLong(VkConstants.Json.ID);
                firstName = currentObject.getString(VkConstants.Json.FIRST_NAME);
                lastName = currentObject.getString(VkConstants.Json.LAST_NAME);
                photoUrl = currentObject.getString(VkConstants.Json.PHOTO_50);
                IUser user = new VkUser(id, firstName, lastName, photoUrl);
                userLongSparseArray.put(id, user);
            }
            return userLongSparseArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
