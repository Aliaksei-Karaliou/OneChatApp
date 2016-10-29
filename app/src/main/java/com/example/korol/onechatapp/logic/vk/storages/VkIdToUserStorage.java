package com.example.korol.onechatapp.logic.vk.storages;

import android.util.Pair;

import com.example.korol.onechatapp.logic.common.VkUser;
import com.example.korol.onechatapp.logic.vk.VkRequester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VkIdToUserStorage {

    private static Map<Long, VkUser> idToUserStorage = new HashMap<>();

    public static VkUser getUser(long id) {
        if (idToUserStorage.containsKey(id))
            return idToUserStorage.get(id);
        else {
            VkUser user = userJsonParser(Long.toString(id)).get(0);
            idToUserStorage.put(id, user);
            return user;
        }
    }

    public static Map<Long, VkUser> getUsers(Integer... ids) {
        Map<Long, VkUser> result = new HashMap<>();
        StringBuilder builder = new StringBuilder();

        // add new users
        for (int id : ids)
            if (!idToUserStorage.containsKey(id) && id > 0)
                builder.append(id).append(",");
        String params = builder.substring(0, builder.length() - 1);
        List<VkUser> userList = userJsonParser(params);
        for (VkUser user : userList) {
            idToUserStorage.put(user.getId(), user);
        }

        // return all user from request
        for (long id : ids)
            if (id > 0)
                result.put(id, idToUserStorage.get(id));
        return result;
    }

    public static Map<Long, VkUser> getUsers(List<Integer> ids) {
        return getUsers(ids.toArray(new Integer[0]));
    }

    private static List<VkUser> userJsonParser(String params) {
        List<VkUser> result = new ArrayList<>();
        try {
            VkRequester requester = new VkRequester("users.get", new Pair<String, String>("user_ids", params), new Pair<String, String>("fields", "photo"));
            JSONObject jsonObject = new JSONObject(requester.execute(null));
            JSONArray responceArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < responceArray.length(); i++) {
                JSONObject responceObject = responceArray.getJSONObject(i);
                result.add(new com.example.korol.onechatapp.logic.vk.VkUser(responceObject.getInt("uid"), responceObject.getString("first_name"), responceObject.getString("last_name"), responceObject.getString("photo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.add(null);
        }
        return result;
    }
}