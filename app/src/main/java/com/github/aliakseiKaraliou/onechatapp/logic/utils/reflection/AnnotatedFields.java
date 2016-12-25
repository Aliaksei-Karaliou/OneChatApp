package com.github.aliakseiKaraliou.onechatapp.logic.utils.reflection;

import com.github.aliakseiKaraliou.onechatapp.logic.db.annotations.DbType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotatedFields {
    public Map<String, String> getAnnotatedbyDbTypeFields(final Class<?> primaryClass) {
        final Map<String, String> map = new HashMap<>();
        final Field[] declaredFields = primaryClass.getDeclaredFields();
        final List<Field> fieldList = new ArrayList<>();
        for (final Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(DbType.class)) {
                fieldList.add(declaredField);
            }
        }
        for (final Field field : fieldList) {
            final DbType.Type type = field.getAnnotation(DbType.class).type();
            final String fieldName = field.getName();
            map.put(fieldName, type.toString());
        }
        return map;
    }
}
