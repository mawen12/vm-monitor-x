package com.github.mawen12.easeagent.core.utils;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;

import java.lang.reflect.Field;

@EaseAgentClassLoader
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
