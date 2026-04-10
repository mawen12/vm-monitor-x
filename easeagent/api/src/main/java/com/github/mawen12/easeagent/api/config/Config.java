package com.github.mawen12.easeagent.api.config;

import com.github.mawen12.easeagent.api.utils.Null;

public interface Config {

    String getString(String key);

    default String getString(String key, String defaultVal) {
        return Null.of(getString(key), defaultVal);
    }

    Integer getInt(String key);

    default Integer getInt(String key, Integer defaultVal) {
        return Null.of(getInt(key), defaultVal);
    }

    Boolean getBoolean(String key);

    default Boolean getBoolean(String key, Boolean defaultVal) {
        return Null.of(getBoolean(key), defaultVal);
    }

    Double getDouble(String key);

    default Double getDouble(String key, Double defaultVal) {
        return Null.of(getDouble(key), defaultVal);
    }

    Long getLong(String key);

    default Long getLong(String key, Long defaultVal) {
        return Null.of(getLong(key), defaultVal);
    }
}
