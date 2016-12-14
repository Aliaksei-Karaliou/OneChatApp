package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IParser;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkIdConverter;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VkGroupDataParser implements IParser<String, LongSparseArray<IGroup>> {

    @Nullable
    @Override
    public LongSparseArray<IGroup> parse(String s) {
        try {
            LongSparseArray<IGroup> sparseArray = new LongSparseArray<>();
            VkIdConverter converter = new VkIdConverter();
            final JSONArray jsonArray = new JSONObject(s).getJSONArray(Constants.Json.RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject currentObject = jsonArray.getJSONObject(i);
                Long id = converter.groupToPeer(currentObject.getLong(Constants.Json.ID));
                final String name = currentObject.getString(Constants.Json.NAME);
                final String photo50 = currentObject.getString(Constants.Json.PHOTO_50);
                final String photo100 = currentObject.getString(Constants.Json.PHOTO_100);
                if (id!=null){
                    IGroup currentGroup = new VkGroup(id, name, photo50, photo100);
                    sparseArray.put(id, currentGroup);
                }
            }
            return sparseArray;
        } catch (JSONException  e) {
            e.printStackTrace();
            return null;
        }
    }
}