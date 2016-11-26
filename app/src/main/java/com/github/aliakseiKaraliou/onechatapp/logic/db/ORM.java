package com.github.aliakseiKaraliou.onechatapp.logic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.aliakseiKaraliou.onechatapp.logic.utils.reflection.AnnotatedFields;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

public class ORM<T> {
    private final SQLiteDatabase database;
    private final DbHelper helper;
    private final Context context;
    private final String name;
    private final Class<T> clazz;
    private final ContentValues contentValues;
    private boolean tableCreated = false;

    private static final String INSERT_INTO_VALUES = "INSERT INTO %s VALUES(%s)";
    private static final String ARGUMENTS = "'%s', ";

    private final Map<String, String> annotatedbyDbTypeFields;

    public ORM(Context context, String name, Class<T> clazz) {
        helper = new DbHelper(context, name, clazz);
        annotatedbyDbTypeFields = new AnnotatedFields().getAnnotatedbyDbTypeFields(clazz);
        database = helper.getWritableDatabase();
        this.context = context;
        this.name = name;
        this.clazz = clazz;
        contentValues = new ContentValues();
    }

    public void createTable() {
        helper.onCreate(database);
        tableCreated = true;
    }

    public boolean put(T obj) {
        try {
            if (!tableCreated) {
                return false;
            }
            StringBuilder builder = new StringBuilder();
            for (String field : annotatedbyDbTypeFields.keySet()) {
                final Field declaredField = clazz.getDeclaredField(field);
                declaredField.setAccessible(true);
                final String value = declaredField.get(obj).toString();
                builder.append(String.format(ARGUMENTS, value));
            }
            final String substring = builder.substring(0, builder.length() - 2);
            final String sqLiteCommand = String.format(Locale.US, INSERT_INTO_VALUES, name, substring);
            database.execSQL(sqLiteCommand); ;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public DbHelper getHelper() {
        return helper;
    }

    public String getName() {
        return name;
    }
}
