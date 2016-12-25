package com.github.aliakseiKaraliou.onechatapp.logic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private final Class<?>[] clazz;


    public DbHelper(final Context context, final String name, final int version, final Class<?>... clazz) {
        super(context, name, null, version);
        this.clazz = clazz;

    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        if (clazz.length > 0) {
            for (final Class<?> claz : clazz) {
                final String createTableQuery = new QueryGenerator().getCreateTableQuery(claz);
                sqLiteDatabase.execSQL(createTableQuery);
            }
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int i, final int i1) {

    }


}
