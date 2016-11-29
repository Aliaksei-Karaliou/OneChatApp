package com.github.aliakseiKaraliou.onechatapp.logic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.db_entities.DbConvert;
import com.github.aliakseiKaraliou.onechatapp.logic.utils.reflection.AnnotatedFields;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

public class SimpleORM<T extends DbConvert> {
    private final SQLiteDatabase database;
    private final DbHelper helper;
    private final Context context;
    private final String name;
    private final Class<T> clazz;
    private final ContentValues contentValues;
    private boolean tableCreated = false;

    private static final String INSERT_INTO_VALUES_QUERY = "INSERT INTO %s VALUES(%s)";
    private static final String INSERT_INTO_VALUES_ARGUMENTS = "'%s', ";
    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS %s";
    private static final String CREATE_IF_NOT_EXISTS_QUERY = "CREATE TABLE IF NOT EXISTS '%s' (%s)";
    private static final String CREATE_PARAMETERS = "'%s' %s %s, ";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String SELECT_QUERY = "SELECT * FROM '%s' WHERE %s";
    private static final String SELECT_ORDERBY_QUERY = "SELECT * FROM %s WHERE %s ORDER BY %s %s";

    private final Map<String, String> annotatedbyDbTypeFields;

    public SimpleORM(Context context, String name, Class<T> clazz) {
        helper = new DbHelper(context, name, null, 1);
        annotatedbyDbTypeFields = new AnnotatedFields().getAnnotatedbyDbTypeFields(clazz);
        database = helper.getWritableDatabase();
        this.context = context;
        this.name = name;
        this.clazz = clazz;
        contentValues = new ContentValues();
    }

    public void createTableIfNotExists() {
        try {
            final Map<String, String> annotatedFields = new AnnotatedFields().getAnnotatedbyDbTypeFields(clazz);
            StringBuilder builder = new StringBuilder();
            for (String key : annotatedFields.keySet()) {
                String value = annotatedFields.get(key);
                final Field declaredField = clazz.getDeclaredField(key);
                String parameter = "";
                if (declaredField.isAnnotationPresent(DbPrimaryKey.class)) {
                    parameter = PRIMARY_KEY;
                }
                builder.append(String.format(Locale.US, CREATE_PARAMETERS, key, value, parameter));
            }
            String result = builder.substring(0, builder.length() - 2);
            String request = String.format(Locale.US, CREATE_IF_NOT_EXISTS_QUERY, name, result);
            database.execSQL(request);
            tableCreated = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createNewTable() {
        drobTable();
        createTableIfNotExists();
    }

    public void drobTable() {
        try {
            String command = String.format(DROP_TABLE_QUERY, name);
            database.execSQL(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insert(T obj) {
        try {
            StringBuilder builder = new StringBuilder();
            for (String field : annotatedbyDbTypeFields.keySet()) {
                final Field declaredField = clazz.getDeclaredField(field);
                declaredField.setAccessible(true);
                final String value = declaredField.get(obj).toString();
                builder.append(String.format(INSERT_INTO_VALUES_ARGUMENTS, value));
            }
            final String substring = builder.substring(0, builder.length() - 2);
            final String sqLiteCommand = String.format(Locale.US, INSERT_INTO_VALUES_QUERY, name, substring);
            database.execSQL(sqLiteCommand);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public enum OrderBy {
        ASC,
        DESC
    }

    public boolean select(String condition) {
        try {
            String query = String.format(Locale.US, SELECT_QUERY, name, condition);
            final Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                final String[] columnNames = cursor.getColumnNames();
                do {}
                while (cursor.moveToNext());
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
