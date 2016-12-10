package com.github.aliakseiKaraliou.onechatapp.logic.db.annotations;

public @interface DbType {
    enum Type {
        BLOB,
        INTEGER,
        REAL,
        TEXT,
        NUMERIC
    }

    Type type();
}
