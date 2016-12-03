package com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities;

import com.github.aliakseiKaraliou.onechatapp.logic.common.IReciever;
import com.github.aliakseiKaraliou.onechatapp.logic.common.IUser;
import com.github.aliakseiKaraliou.onechatapp.logic.common.enums.PeerRecieverType;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.entities.VkUser;

import java.util.ArrayList;
import java.util.List;

public class DbReciever extends DbEntity<DbReciever> {

    @DbPrimaryKey
    @DbType(type = DbType.Type.INTEGER)
    private long receiverId;
    @DbType(type = DbType.Type.TEXT)
    private String firstName;
    @DbType(type = DbType.Type.TEXT)
    private String secondName;
    @DbType(type = DbType.Type.TEXT)
    private String photoUrl;
    @DbType(type = DbType.Type.TEXT)
    private String socialNetwork;
    @DbType(type = DbType.Type.TEXT)
    private String type;

    private DbReciever(long receiverId, String firstName, String secondName, java.lang.String photoUrl, String socialNetwork, String type) {
        this.receiverId = receiverId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.photoUrl = photoUrl;
        this.socialNetwork = socialNetwork;
        this.type = type;
    }

    private DbReciever() {

    }

    public static List<DbReciever> convertTo(List<IReciever> recieverList) {
        List<DbReciever> dbRecievers = new ArrayList<>();
        for (IReciever reciever : recieverList) {
            final long receiverId = reciever.getId();
            final String photoUrl = reciever.getPhotoUrl();
            final String socialNetwork = reciever.getSocialNetwork().toString();
            final String receiverType = reciever.getPeerReceiverType().toString();
            java.lang.String firstName = "", secondName = "";
            if (receiverType.equals(PeerRecieverType.USER.toString())) {
                final IUser user = (IUser) reciever;
                firstName = user.getFirstName();
                secondName = user.getLastName();
            } else {
                secondName = reciever.getName();
            }
            DbReciever receiver = new DbReciever(receiverId, firstName, secondName, photoUrl, socialNetwork, receiverType);
            dbRecievers.add(receiver);
        }
        return dbRecievers;
    }

    public static List<IReciever> convertFrom(List<DbReciever> list) {
        List<IReciever> recieverList = new ArrayList<>();
        for (DbReciever receiver : list) {

            final String firstName = receiver.firstName;
            final String photoUrl = receiver.photoUrl;
            final String secondName = receiver.secondName;
            final String socialNetwork = receiver.socialNetwork;
            final String type = receiver.type;
            final long receiverId = receiver.receiverId;


            if (socialNetwork.equals("VK")) {
                IReciever reciever = null;
                if (type.equals("USER")) {
                    reciever = ((IReciever) new VkUser(receiverId, firstName, secondName, photoUrl));
                }
                recieverList.add(reciever);
            }
        }
        return recieverList;
    }

    @Override
    protected DbReciever convert(List<String> list) {
        final long receiverId = Long.parseLong(list.get(0));
        final String firstName = list.get(1);
        final String network = list.get(2);
        final String secondName = list.get(3);
        final String type = list.get(4);
        final String photoUrl = list.get(5);
        return new DbReciever(receiverId, firstName, secondName, photoUrl, network, type);
    }
}
