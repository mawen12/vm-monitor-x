package com.github.mawen12.easeagent.core.utils;

import java.lang.reflect.Field;

public class ClassUtils {

    public static Object getStaticField(String className, String fieldName) {
        try {
            Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
            Field f = c.getDeclaredField(fieldName);
            return f.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
