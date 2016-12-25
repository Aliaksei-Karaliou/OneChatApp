package com.github.aliakseiKaraliou.onechatapp.logic.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.github.aliakseiKaraliou.onechatapp.logic.db.models.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public class ORM {

    private final DbHelper helper;

    public ORM(final DbHelper helper) {
        this.helper = helper;
    }

    public <T extends AbstractModel> long insert(final String tableName, @NonNull final T model) {
        try {
            final SQLiteDatabase database = helper.getWritableDatabase();
            final ContentValues contentValues = model.convertToContentValues();
            return database.insert(tableName, null, contentValues);
        } catch (final Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public <T extends AbstractModel> List<Long> insertAll(final String tableName, @NonNull final List<T> modelList) {
        final List<Long> longList = new ArrayList<>();
        for (final T model : modelList) {
            longList.add(insert(tableName, model));
        }
        return longList;
    }

    public <T extends AbstractModel> List<T> select(final String tableName, final T object, final SelectCondition<T> condition) {
        final SQLiteDatabase database = helper.getReadableDatabase();
        final List<T> modelList = new ArrayList<>();

        final String allQuery = new QueryGenerator().getSelectAllQuery(tableName);
        final Cursor cursor = database.rawQuery(allQuery, null);

        cursor.moveToFirst();
        do {
            final T currentModel = (T) object.convertToModel(cursor);
            if (condition.select(currentModel)) {
                modelList.add(currentModel);
            }
        }
        while (cursor.moveToNext());
        return modelList;
    }

    public <T extends AbstractModel> List<T> selectAll(final String tableName, final T object) {
        final SQLiteDatabase database = helper.getReadableDatabase();
        final List<T> modelList = new ArrayList<>();

        final String allQuery = new QueryGenerator().getSelectAllQuery(tableName);
        final Cursor cursor = database.rawQuery(allQuery, null);

        if (cursor.moveToFirst()) {
            do {
                final T currentModel = (T) object.convertToModel(cursor);
                modelList.add(currentModel);
            }
            while (cursor.moveToNext());
        }
        return modelList;
    }

//    public <T extends AbstractModel> void update(final String tableName, final T object) {
//        final SQLiteDatabase database = helper.getWritableDatabase();
//        final String allQuery = new QueryGenerator().getSelectAllQuery(tableName);
//
//        final Cursor cursor = database.rawQuery(allQuery, null);
//        do {
//            final T model = (T) object.convertToModel(cursor);
//            if (model.isEquals(object)){
//            }
//        }
//        while (cursor.moveToNext());
//    }


    public interface SelectCondition<T extends AbstractModel> {
        boolean select(T model);
    }

}
