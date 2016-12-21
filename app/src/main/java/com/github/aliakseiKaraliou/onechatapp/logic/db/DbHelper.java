package com.github.aliakseiKaraliou.onechatapp.logic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private final Class<?>[] clazz;


    public DbHelper(Context context, String name, int version, final Class<?>... clazz) {
        super(context, name, null, version);
        this.clazz = clazz;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if (clazz.length > 0) {
            for (Class<?> claz : clazz) {
                final String createTableQuery = new QueryGenerator().getCreateTableQuery(claz);
                sqLiteDatabase.execSQL(createTableQuery);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
