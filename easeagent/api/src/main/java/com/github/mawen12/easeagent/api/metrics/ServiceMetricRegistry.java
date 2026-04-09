package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.Agent;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class ServiceMetricRegistry {

    public static final ConcurrentMap<Key, ServiceMetric> INSTANCES = new ConcurrentHashMap<>();

    public static <T extends ServiceMetric> T getOrCreate(String category, String type, String keyFieldName, Function<MetricRegistry, T> supplier) {
        Key key = new Key(category, type, keyFieldName);
        ServiceMetric metric = INSTANCES.get(key);
        if (metric != null) {
            return (T) metric;
        }

        synchronized (INSTANCES) {
            metric = INSTANCES.get(key);
            if (metric != null) {
                return (T) metric;
            }

            metric = supplier.apply(Agent.getMetricRegistry());
            INSTANCES.put(key, metric);
        }
        return (T) metric;
    }


    public static class Key {
        private final String category;
        private final String type;
        private final String keyFieldName;

        public Key(String category, String type, String keyFieldName) {
            this.category = category;
            this.type = type;
            this.keyFieldName = keyFieldName;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(category, key.category) && Objects.equals(type, key.type) && Objects.equals(keyFieldName, key.keyFieldName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(category, type, keyFieldName);
        }
    }
}
