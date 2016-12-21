package com.github.aliakseiKaraliou.onechatapp.logic.db.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbTableName {
    String name();
}
