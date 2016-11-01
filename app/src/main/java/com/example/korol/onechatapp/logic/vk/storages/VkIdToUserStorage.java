package com.example.korol.onechatapp.logic.vk.storages;

import android.util.Pair;

import com.example.korol.onechatapp.logic.common.IUser;
import com.example.korol.onechatapp.logic.vk.VkRequester;
import com.example.korol.onechatapp.logic.vk.json.VkBasicUserJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class VkIdToUserStorage {

    private static Map<Long, IUser> idToUserStorage = new HashMap<>();

    public static IUser getUser(Long id) {
        if (idToUserStorage.containsKey(id))
            return idToUserStorage.get(id);
        else {
            IUser user = null;
            try {
                user = new VkBasicUserJsonParser().execute(id.toString()).get(0);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            idToUserStorage.put(id, user);
            return user;
        }
    }

    public static Map<Long, IUser> getUsers(Integer... ids) {
        Map<Long, IUser> result = new HashMap<>();
        StringBuilder builder = new StringBuilder();

        // add new users
        for (int id : ids)
            if (!idToUserStorage.containsKey(id) && id > 0)
                builder.append(id).append(",");
        String params = builder.substring(0, builder.length() - 1);
        List<IUser> userList = null;
        try {
            userList = new VkBasicUserJsonParser().execute(params);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        assert userList != null;
        for (IUser user : userList) {
            idToUserStorage.put(user.getId(), user);
        }

        // return all user from request
        for (long id : ids)
            if (id > 0)
                result.put(id, idToUserStorage.get(id));
        return result;
    }

    public static Map<Long, IUser> getUsers(List<Integer> ids) {
        return getUsers(ids.toArray(new Integer[0]));
    }
}
