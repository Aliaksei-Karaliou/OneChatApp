package com.example.korol.onechatapp.logic.vk;

import android.util.Pair;

import com.example.korol.onechatapp.logic.common.IUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    public static List<IUser> getUsers(Integer... ids) {
        List<IUser> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        // add new users
        for (int id : ids)
            if (!idToUserStorage.containsKey(id))
                builder.append(id).append(",");
        String params = builder.substring(0, builder.length() - 1);
        List<IUser> userList = userJsonParser(params);
        for (IUser user : userList) {
            idToUserStorage.put(user.getId(), user);
        }

        // return all user from request
        for (int id : ids) {
            result.add(idToUserStorage.get(id));
        }
        return result;
    }

    public static List<IUser> getUsers(List<Integer> ids) {
        return getUsers(ids.toArray(new Integer[0]));
    }

    private static List<IUser> userJsonParser(String params) {
        List<IUser> result = new ArrayList<>();
        try {
            VkRequester requester = new VkRequester("users.get", new Pair<String, String>("user_ids", params));
            JSONObject jsonObject = new JSONObject(requester.getJSON());
            JSONArray responceArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < responceArray.length(); i++) {
                JSONObject responceObject = responceArray.getJSONObject(i);
                result.add(new User(responceObject.getInt("uid"), responceObject.getString("first_name"), responceObject.getString("last_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.add(null);
        }
        return result;
    }
}
