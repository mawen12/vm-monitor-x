package com.github.mawen12.utils;

import com.github.mawen12.model.Jsonable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Json {

    public static String toJson(Object... kvPairs) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < kvPairs.length -1; i += 2) {
            sb.append('"').append(kvPairs[i]).append('"').append(":")
                    .append(toJsonValue(kvPairs[i+1]));

            if (i < kvPairs.length -2) {
                sb.append(",");
            }
        }
        sb.append("}");

        return sb.toString();
    }

    public static String toJsonValue(Object value) {
        if (value == null)
            return null;

        if (value instanceof String || value instanceof Enum)
            return "\"" + value + "\"";

        if (value instanceof Jsonable)
            return ((Jsonable) value).toJson();

        if (value instanceof Map)
            return toJsonObject((Map) value);

        if (value instanceof Collection<?>)
            return toJsonObject((Collection) value);

        return value.toString();
    }

    public static <T> String toJsonObject(Map<String, T> data) {
        StringBuilder sb = new StringBuilder("{");

        Iterator<Map.Entry<String, T>> iter = data.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, T> entry = iter.next();
            sb.append('"').append(entry.getKey()).append('"').append(':');
            sb.append(toJsonValue(entry.getValue()));

            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("}");

        return sb.toString();
    }

    public static String toJsonObject(Collection items) {
        Stream<String> stream = items.stream().map(Json::toJsonValue);
        return stream.collect(joining(",", "[", "]"));
    }
}
