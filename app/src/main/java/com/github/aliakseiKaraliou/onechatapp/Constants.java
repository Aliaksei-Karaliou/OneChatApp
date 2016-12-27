package com.github.aliakseiKaraliou.onechatapp;

public interface Constants {

    interface Json {
        String ACTION = "action";
        String ACTION_MID = "action_mid";
        String BODY = "body";
        String CHAT_ID = "chat_id";
        String CHAT_IDS = "chat_ids";
        String CHAT_INVITE_USER = "chat_invite_user";
        String CHAT_KICK_USER = "chat_kick_user";
        String DATE = "date";
        String GROUP_ID = "group_ids";
        String FIRST_NAME = "first_name";
        String FIELDS = "fields";
        String FROM_ID = "from_id";
        String ID = "id";
        String ITEMS = "items";
        String KEY = "key";
        String LAST_NAME = "last_name";
        String MESSAGE = "message";
        String NAME = "name";
        String OUT = "out";
        String PEER_ID = "peer_id";
        String PHOTO_50 = "photo_50";
        String PHOTO_100 = "photo_100";
        String PHOTO_50_100 = "photo_50,photo_100";
        String READ_STATE = "read_state";
        String RESPONSE = "response";
        String SERVER = "server";
        String TITLE = "title";
        String TS = "ts";
        String UPDATES = "updates";
        String USER_ID = "user_id";
        String USER_IDS = "user_ids";
    }

    interface Method {
        String GROUPS_GETBYID = "groups.getById";
        String MESSAGES_DELETEDIALOG = "messages.deleteDialog";
        String MESSAGES_GETBYID = "messages.getById";
        String MESSAGES_GETDIALOGS = "messages.getDialogs";
        String MESSAGES_GETCHAT = "messages.getChat";
        String MESSAGES_GETHISTORY = "messages.getHistory";
        String MESSAGES_GETLONGPOLLSERVER = "messages.getLongPollServer";
        String MESSAGES_SEND = "messages.send";
        String USERS_GET = "users.get";
    }

    interface Params {
        String ACCESS_TOKEN = "access_token";
        String AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=%d&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=offline,%s&response_type=token&v=%f&revoke=1";
        String JOIN = "join";
        String MESSAGE = "message";
        String MESSAGES = "messages";
        String MESSAGE_IDS = "message_ids";
        String OFFSET = "offset";
        String PEER_ID = "peer_id";
        String USER_ID = "user_id";
    }

    interface Db {
        String ALL_MESSAGES = "AllMessages";
        String CHATS = "Chats";
        String DIALOGS_LIST = "DialogsList";
        String GROUPS = "Groups";
        String MESSAGES = "Messages";
        String RECEIVERS = "Receivers";
        String USERS = "Users";
    }

    interface Other {
        String BROADCAST_EVENT_RECEIVER_NAME = "com.github.aliakseiKaraliou.newEvent";
        String CONVERT = "convert";
        String DEVELOPER_EMAIL = "alexeykoroliov@gmail.com";
        String EVENT_LIST = "event list";
        String EVENT_SERVICE = "event service";
        String MAILTO = "mailto";
        String PEER_ID = "peer id";
        String PREFERENCES = "preferences";
        String DARK_THEME = "light theme";
        String THEME_SWITCH_KEY = "Theme key";
        String VK_REQUEST_TEMPLATE = "https://api.vk.com/method/%s?%s&access_token=%s&v=%.2f";
        String VK_LONG_POLL_REQUEST = "https://%s?act=a_check&key=%s&ts=%d&wait=25&mode=2";
    }
}
