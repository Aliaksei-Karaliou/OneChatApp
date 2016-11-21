package com.github.AliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.AliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkConstants;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.entities.VkUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VkUserDataParser implements IParser<String, List<IUser>> {
    @Nullable
    @Override
    public List<IUser> parse(String json) {
        List<IUser> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(VkConstants.Json.RESPONSE);
            JSONObject currentObject;
            long id;
            String firstName = "", lastName = "", photoUrl = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                currentObject = jsonArray.getJSONObject(i);
                id = currentObject.getLong(VkConstants.Json.ID);
                firstName = currentObject.getString(VkConstants.Json.FIRST_NAME);
                lastName = currentObject.getString(VkConstants.Json.LAST_NAME);
                photoUrl = currentObject.getString(VkConstants.Json.PHOTO_50);
                list.add(new VkUser(id, firstName, lastName, photoUrl));
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
