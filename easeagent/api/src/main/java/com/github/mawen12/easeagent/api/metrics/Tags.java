package com.github.mawen12.easeagent.api.metrics;

import java.util.Objects;

public class Tags {
    private final String category;
    private final String type;
    private final String keyFieldName;

    public Tags(String category, String type, String keyFieldName) {
        this.category = category;
        this.type = type;
        this.keyFieldName = keyFieldName;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getKeyFieldName() {
        return keyFieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tags key = (Tags) o;
        return Objects.equals(category, key.category) && Objects.equals(type, key.type) && Objects.equals(keyFieldName, key.keyFieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, type, keyFieldName);
    }


}