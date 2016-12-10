package com.github.aliakseiKaraliou.onechatapp.logic.db.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.SocialNetwork;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbColumnName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbTableName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.models.VkUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DbTableName(name = Constants.Db.USERS)
public final class UserModel implements AbstractModel<UserModel> {

    private static final String UID = "uid";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String PHOTO = "photo";
    private static final String SOCIAL_NETWORK = "social_network";

    @DbType(type = DbType.Type.INTEGER)
    @DbPrimaryKey
    @DbColumnName(name = UID)
    private long id;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = FIRST_NAME)
    private String firstName;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = LAST_NAME)
    private String lastName;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = PHOTO)
    private String photoUrl;

    @DbType(type = DbType.Type.TEXT)
    @DbColumnName(name = SOCIAL_NETWORK)
    @DbPrimaryKey
    private String socialNetwork;

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    @Override
    public ContentValues convertToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID, id);
        contentValues.put(FIRST_NAME, firstName);
        contentValues.put(LAST_NAME, lastName);
        contentValues.put(PHOTO, photoUrl);
        contentValues.put(SOCIAL_NETWORK, socialNetwork);
        return contentValues;
    }


    @NonNull
    @Override
    public UserModel convertToModel(Cursor cursor) {
        String firstName = cursor.getString(0);
        long uid = cursor.getLong(1);
        String lastname = cursor.getString(2);
        String photo = cursor.getString(3);
        String socialNetwork = cursor.getString(4);
        return new UserModel(uid, firstName, lastname, photo, SocialNetwork.valueOf(socialNetwork));
    }

    private UserModel(long id, String firstName, String lastName, String photoUrl, SocialNetwork socialNetwork) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl;
        this.socialNetwork = socialNetwork.toString();
    }

    private UserModel(IUser user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.photoUrl = user.getPhotoUrl();
        this.socialNetwork = user.getSocialNetwork().toString();
    }

    private UserModel() {
    }

    public static UserModel getInstance(){
        return new UserModel();
    }

    public static List<UserModel> convertTo(Collection<IUser> users) {
        List<UserModel> userModelList = new ArrayList<>();
        for (IUser user : users) {
            userModelList.add(new UserModel(user));
        }
        return userModelList;
    }

    public static List<IUser> convertFrom(Collection<UserModel> userModels) {
        List<IUser> userList = new ArrayList<>();
        IUser user;
        for (UserModel userModel : userModels) {
            user = new VkUser(userModel.id, userModel.firstName, userModel.lastName, userModel.photoUrl);
            userList.add(user);
        }
        return userList;
    }
}
