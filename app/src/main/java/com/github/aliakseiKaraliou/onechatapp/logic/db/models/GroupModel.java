package com.github.aliakseiKaraliou.onechatapp.logic.db.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbColumnName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbTableName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;
import com.github.aliakseiKaraliou.onechatapp.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DbTableName(name = Constants.Db.GROUPS)
public final class GroupModel implements AbstractModel<GroupModel> {

    private static final String SOCIAL_NETWORK = "social_network";
    private static final String PEER_ID = "peerId";
    private static final String NAME = "name";
    private static final String PHOTO50 = "photo50";
    private static final String PHOTO100 = "photo100";

    @DbType(type = DbType.Type.INTEGER)
    @DbColumnName(name = PEER_ID)
    @DbPrimaryKey
    private long id;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = NAME)
    private String name;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = PHOTO50)
    private String photo50;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = PHOTO100)
    private String photo100;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = SOCIAL_NETWORK)
    @DbPrimaryKey
    private String socialNetwork;

    public GroupModel(long id, String name, String photo50, String photo100, SocialNetwork socialNetwork) {
        this.id = id;
        this.name = name;
        this.photo50 = photo50;
        this.photo100 = photo100;
        this.socialNetwork = socialNetwork.toString();
    }

    public GroupModel(IGroup group) {
        this.id = group.getId();
        this.name = group.getName();
        this.photo50 = group.getPhoto50Url();
        this.photo100 = group.getPhoto100Url();
        this.socialNetwork = group.getSocialNetwork().toString();
    }

    @Override
    public ContentValues convertToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PEER_ID, id);
        contentValues.put(NAME, name);
        contentValues.put(PHOTO50, photo50);
        contentValues.put(PHOTO100, photo100);
        contentValues.put(SOCIAL_NETWORK, socialNetwork);
        return contentValues;
    }

    @Override
    public GroupModel convertToModel(Cursor cursor) {
        final String[] columnNames = cursor.getColumnNames();
        String name = null, photo50 = null, photo100 = null, socialNetwork = null;
        long peerId = 0;
        for (int i = 0; i < columnNames.length; i++) {
            switch (columnNames[i]) {
                case PEER_ID:
                    peerId = cursor.getLong(i);
                    break;
                case NAME:
                    name = cursor.getString(i);
                    break;
                case PHOTO50:
                    photo50 = cursor.getString(i);
                    break;
                case PHOTO100:
                    photo100 = cursor.getString(i);
                    break;
                case SOCIAL_NETWORK:
                    socialNetwork = cursor.getString(i);
                    break;
            }
        }
        return new GroupModel(peerId, name, photo50, photo100, SocialNetwork.valueOf(socialNetwork));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GroupModel) {
            final GroupModel groupModel = (GroupModel) obj;
            return groupModel.socialNetwork.equals(socialNetwork) && groupModel.id == id;
        } else {
            return false;
        }
    }

    private GroupModel() {

    }

    public static GroupModel getInstance() {
        return new GroupModel();
    }

    public static List<GroupModel> convertTo(Collection<IGroup> groupCollection) {
        List<GroupModel> modelList = new ArrayList<>();
        for (IGroup group : groupCollection) {
            modelList.add(new GroupModel(group));
        }
        return modelList;
    }

    public static List<IGroup> convertFrom(Collection<GroupModel> groupModelCollection) {
        List<IGroup> groupList = new ArrayList<>();
        for (GroupModel groupModel : groupModelCollection) {
            groupList.add(new VkGroup(groupModel.id, groupModel.name, groupModel.photo50, groupModel.photo100));
        }
        return groupList;
    }
}
