package com.github.mawen12.easeagent.core.config;

import com.github.mawen12.easeagent.api.config.Config;

import java.util.Map;

public class ConfigImpl implements Config {

    private final Map<String, String> source;

    public ConfigImpl(Map<String, String> source) {
        this.source = source;
    }

    @Override
    public String getString(String key) {
        return this.source.get(key);
    }

    @Override
    public Integer getInt(String key) {
        String value = this.source.get(key);
        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean getBoolean(String key) {
        String value = this.source.get(key);
        if (value == null) {
            return null;
        }

        return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true");
    }

    @Override
    public Double getDouble(String key) {
        String value = this.source.get(key);
        if (value == null) {
            return null;
        }

        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Long getLong(String key) {
        String value = this.source.get(key);
        if (value == null) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return null;
        }
    }
}
