package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VkUserDataParser implements IParser<String, LongSparseArray<IUser>> {
    @Nullable
    @Override
    public LongSparseArray<IUser> parse(String json) {
        LongSparseArray<IUser> userLongSparseArray = new LongSparseArray<>();
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(Constants.Json.RESPONSE);
            JSONObject currentObject;
            long id;
            String firstName, lastName, photo50, photo100;
            for (int i = 0; i < jsonArray.length(); i++) {
                currentObject = jsonArray.getJSONObject(i);
                id = currentObject.getLong(Constants.Json.ID);
                firstName = currentObject.getString(Constants.Json.FIRST_NAME);
                lastName = currentObject.getString(Constants.Json.LAST_NAME);
                photo50 = currentObject.getString(Constants.Json.PHOTO_50);
                photo100 = currentObject.getString(Constants.Json.PHOTO_100);
                IUser user = new VkUser(id, firstName, lastName, photo50, photo100);
                userLongSparseArray.put(id, user);
            }
            return userLongSparseArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
