package com.github.aliakseiKaraliou.onechatapp.logic.vk;

public final class VkConstants {

    public final static class Json {
        public static final String BODY = "body";
        public static final String CHAT_ID = "chat_id";
        public static final String CHAT_IDS = "chat_ids";
        public static final String DATE = "date";
        public static final String GROUP_ID = "group_ids";
        public static final String FIRST_NAME = "first_name";
        public static final String FIELDS = "fields";
        public static final String FROM_ID = "from_id";
        public static final String ID = "id";
        public static final String ITEMS = "items";
        public static final String LAST_NAME = "last_name";
        public static final String MESSAGE = "message";
        public static final String NAME = "name";
        public static final String PHOTO_50 = "photo_50";
        public static final String READ_STATE = "read_state";
        public static final String RESPONSE = "response";
        public static final String TITLE = "title";
        public static final String USER_ID = "user_id";
        public static final String USER_IDS = "user_ids";
    }

    public final static class Method {
        public static final String GROUPS_GETBYID = "groups.getById";
        public static final String MESSAGES_GETDIALOGS = "messages.getDialogs";
        public static final String MESSAGES_GETCHAT = "messages.getChat";
        public static final String MESSAGES_GETHISTORY = "messages.getHistory";
        public static final String USERS_GET = "users.get";
    }

    public final static class Params {
        public static final String OFFSET = "offset";
    }

    public final static class Extra {
        public static final String PEER_ID = "peer id";
    }
}
