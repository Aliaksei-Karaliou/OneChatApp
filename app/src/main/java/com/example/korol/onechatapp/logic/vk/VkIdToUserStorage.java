package com.example.korol.onechatapp.logic.vk;

import android.util.Pair;

import com.example.korol.onechatapp.logic.common.IUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class VkIdToUserStorage {

    private static Map<Integer, IUser> idToUserStorage = new HashMap<>();

    public static IUser getUser(int id) {
        if (idToUserStorage.containsKey(id))
            return idToUserStorage.get(id);
        else {
            IUser user = userJsonParser(Integer.toString(id)).get(0);
            idToUserStorage.put(id, user);
            return user;
        }
    }

    public static List<IUser> getUsers(int... ids) {
        List<IUser> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int id : ids)
            if (idToUserStorage.containsKey(id))
                result.add(idToUserStorage.get(id));
            else {
                builder.append(id).append("&");
            }
        String params = builder.substring(0, builder.length() - 2);
        return result;
    }

    private static List<IUser> userJsonParser(String params) {
        List<IUser> result = new ArrayList<>();
        try {
            VkRequester requester = new VkRequester("users.get", new Pair<String, String>("user_ids", params));
            JSONObject jsonObject = new JSONObject(requester.execute().get());
            result.add(new User(jsonObject.getInt("id"), jsonObject.getString("first_name"), jsonObject.getString("last_name")));
        } catch (JSONException e) {
            e.printStackTrace();
            result.add(null);
        }
        return result;
    }
}
