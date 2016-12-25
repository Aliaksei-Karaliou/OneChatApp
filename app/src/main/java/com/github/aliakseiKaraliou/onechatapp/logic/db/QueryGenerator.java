package com.github.aliakseiKaraliou.onechatapp.logic.db;

import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbColumnName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbPrimaryKey;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbTableName;
import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QueryGenerator {

    private static final String CREATE_IF_NOT_EXISTS_QUERY = "CREATE TABLE IF NOT EXISTS '%s' (%s, PRIMARY KEY(%s))";
    private static final String SELECT_QUERY = "SELECT * FROM '%s'";
    private static final String DROP_QUERY = "DROP TABLE IF EXISTS '%s'";

    public String getCreateTableQuery(final Class<?> clazz) {

        //get all colums
        final Field[] declaredFields = clazz.getDeclaredFields();
        final List<Field> columns = new ArrayList<>();
        for (final Field field : declaredFields) {
            if (field.getAnnotation(DbType.class) != null) {
                columns.add(field);
            }
        }

        final StringBuilder builder = new StringBuilder();

        for (final Field column : columns) {
            builder.append(column.getAnnotation(DbColumnName.class).name()).append(' ').append(column.getAnnotation(DbType.class).type()).append(",");
        }

        final StringBuilder primaryKeyBuilder = new StringBuilder();
        for (final Field column : columns) {
            if (column.getAnnotation(DbPrimaryKey.class) != null) {
                primaryKeyBuilder.append(column.getAnnotation(DbColumnName.class).name()).append(",");
            }
        }
        final DbTableName declaredAnnotation = clazz.getAnnotation(DbTableName.class);
        final String name = declaredAnnotation.name();

        return String.format(Locale.US, CREATE_IF_NOT_EXISTS_QUERY, name, builder.substring(0, builder.length() - 1), primaryKeyBuilder.substring(0, primaryKeyBuilder.length() - 1));
    }

    public String getSelectAllQuery(final String name) {
        return String.format(Locale.US, SELECT_QUERY, name);
    }

    public String getDropQuery(final String name) {
        return String.format(Locale.US, DROP_QUERY, name);
    }

}
