package com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities;

import java.util.List;

public abstract class DbEntity<T> {
    protected abstract T convert(List<String> list);
}
