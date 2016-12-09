package com.github.aliakseiKaraliou.onechatapp.logic.vk;

public final class VkConstants {

    public final static class Json {
        public static final String ACTION = "action";
        public static final String ACTION_MID = "action_mid";
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
        public static final String KEY = "key";
        public static final String LAST_NAME = "last_name";
        public static final String MESSAGE = "message";
        public static final String NAME = "name";
        public static final String OUT= "out";
        public static final String PHOTO_50 = "photo_50";
        public static final String READ_STATE = "read_state";
        public static final String RESPONSE = "response";
        public static final String SERVER = "server";
        public static final String TITLE = "title";
        public static final String TS = "ts";
        public static final String UPDATES = "updates";
        public static final String USER_ID = "user_id";
        public static final String USER_IDS = "user_ids";
    }

    public final static class Method {
        public static final String GROUPS_GETBYID = "groups.getById";
        public static final String MESSAGES_DELETEDIALOG = "messages.deleteDialog";
        public static final String MESSAGES_GETDIALOGS = "messages.getDialogs";
        public static final String MESSAGES_GETCHAT = "messages.getChat";
        public static final String MESSAGES_GETHISTORY = "messages.getHistory";
        public static final String MESSAGES_GETLONGPOLLSERVER = "messages.getLongPollServer";
        public static final String MESSAGES_SEND = "messages.send";
        public static final String USERS_GET = "users.get";
    }

    public final static class Params {
        public static final String ACCESS_TOKEN = "access_token";
        public static final String AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=%d&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=offline,%s&response_type=token&v=%f&revoke=1";
        public static final String MESSAGE = "message";
        public static final String OFFSET = "offset";
        public static final String PEER_ID = "peer_id";
        public static final String USER_ID = "user_id";
    }

    public final static class Other {
        public static final String CONVERT = "convert";
        public static final String PEER_ID = "peer id";
        public static final String PREFERENCES = "preferences";
        public static final String LIGHT_THEME = "light theme";
        public static final String THEME_SWITCH_KEY = "Theme key";
        public static final String VK_REQUEST_TEMPLATE = "https://api.vk.com/method/%s?%s&access_token=%s&v=%.2f";
        public static final String VK_LONG_POLL_REQUEST = "http://%s?act=a_check&key=%s&ts=%d&wait=25&mode=2";
    }
}
