package com.github.aliakseiKaraliou.onechatapp.logic.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
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
