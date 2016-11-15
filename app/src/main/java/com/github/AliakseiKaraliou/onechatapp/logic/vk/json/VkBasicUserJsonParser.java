package com.github.AliakseiKaraliou.onechatapp.logic.vk.json;

import android.util.Pair;

import com.github.AliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.AliakseiKaraliou.onechatapp.logic.utils.asyncOperation.AsyncOperation;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkRequester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VkBasicUserJsonParser extends AsyncOperation<String, List<IUser>> {

    @Override
    protected List<IUser> doInBackground(String s){
        List<IUser> result = new ArrayList<>();
        try {
            VkRequester requester = new VkRequester("users.get", new Pair<String, String>("user_ids", s), new Pair<String, String>("fields", "photo"));
            final String execute = requester.execute(null);

            JSONArray responceArray = new JSONObject(execute).getJSONArray("response");
            for (int i = 0; i < responceArray.length(); i++) {
                JSONObject responceObject = responceArray.getJSONObject(i);
                result.add(new com.github.AliakseiKaraliou.onechatapp.logic.vk.entities.VkUser(responceObject.getInt("id"), responceObject.getString("first_name"), responceObject.getString("last_name"), responceObject.getString("photo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.add(null);
        }
        return result;
    }
}
