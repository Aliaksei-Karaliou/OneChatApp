package com.github.aliakseiKaraliou.onechatapp.logic.db.models;

import android.content.ContentValues;
import android.database.Cursor;

public interface AbstractModel<T extends AbstractModel> {
    ContentValues convertToContentValues();
    T convertToModel(Cursor cursor);
}
