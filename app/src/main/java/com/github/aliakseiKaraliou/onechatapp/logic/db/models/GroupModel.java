package com.github.aliakseiKaraliou.onechatapp.logic.db.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IGroup;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbColumnName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbTableName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DbTableName(name = Constants.Db.GROUPS)
public class GroupModel implements AbstractModel<GroupModel> {

    private static final String SOCIAL_NETWORK = "social_network";
    private static final String PEER_ID = "peerId";
    private static final String NAME = "name";
    private static final String PHOTO = "photo";

    @DbType(type = DbType.Type.INTEGER)
    @DbColumnName(name = PEER_ID)
    @DbPrimaryKey
    private long id;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = NAME)
    private String name;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = PHOTO)
    private String photo;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = SOCIAL_NETWORK)
    @DbPrimaryKey
    private String socialNetwork;

    public GroupModel(long id, String name, String photo, SocialNetwork socialNetwork) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.socialNetwork = socialNetwork.toString();
    }

    public GroupModel(IGroup group) {
        this.id = group.getId();
        this.name = group.getName();
        this.photo = group.getPhotoUrl();
        this.socialNetwork = group.getSocialNetwork().toString();
    }

    @Override
    public ContentValues convertToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PEER_ID, id);
        contentValues.put(NAME, name);
        contentValues.put(PHOTO, photo);
        contentValues.put(SOCIAL_NETWORK, socialNetwork);
        return contentValues;
    }

    @Override
    public GroupModel convertToModel(Cursor cursor) {
        final long peerId = cursor.getLong(0);
        final String name = cursor.getString(1);
        final String photoUrl = cursor.getString(2);
        final String socialNetwork = cursor.getString(3);

        return new GroupModel(peerId, name, photoUrl, SocialNetwork.valueOf(socialNetwork));
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
            groupList.add(new VkGroup(groupModel.id, groupModel.name, groupModel.photo));
        }
        return groupList;
    }
}
