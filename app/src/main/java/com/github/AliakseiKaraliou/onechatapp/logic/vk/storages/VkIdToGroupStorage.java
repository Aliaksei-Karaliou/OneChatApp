package com.github.AliakseiKaraliou.onechatapp.logic.vk.storages;

import android.util.Pair;

import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkRequester;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.entities.VkGroup;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.json.VkBasicGroupJsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VkIdToGroupStorage {

    private static Map<Long, VkGroup> storage = new HashMap<>();

    public static Map<Long, VkGroup> getGroups(List<Long> groupIds) {
        Map<Long, VkGroup> result = new HashMap<>();
        List<Long> requireIds = new ArrayList<>();

        for (Long groupId : groupIds)
            if (!storage.containsKey(groupId))
                requireIds.add(groupId);
            else result.put(groupId, storage.get(groupId));

        if (requireIds.size() > 0) {
            final StringBuilder builder = new StringBuilder();
            for (Long requireId : requireIds)
                builder.append(-requireId + ",");
            final String json = new VkRequester("groups.getById", new Pair<String, String>("group_ids", builder.toString())).execute(null);
            final Map<Long, VkGroup> requireResult = new VkBasicGroupJsonParser().execute(json);
            for (Long requireId : requireIds) {
                final VkGroup group = requireResult.get(-requireId);
                storage.put(requireId, group);
                result.put(requireId, group);
            }
        }
        return result;
    }

    public static VkGroup getGroup(long groupId) {
        List<Long> ids = new ArrayList<>();
        ids.add(groupId);
        return getGroups(ids).get(groupId);
    }
}
