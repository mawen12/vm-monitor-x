package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.Agent;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class ServiceMetricRegistry {

    public static final ConcurrentMap<Tags, ServiceMetric> INSTANCES = new ConcurrentHashMap<>();

    public static <T extends ServiceMetric> T getOrCreate(Tags tags, Function<MetricRegistry, T> supplier) {
        ServiceMetric metric = INSTANCES.get(tags);
        if (metric != null) {
            return (T) metric;
        }

        synchronized (INSTANCES) {
            metric = INSTANCES.get(tags);
            if (metric != null) {
                return (T) metric;
            }

            metric = supplier.apply(Agent.getMetricRegistry());
            INSTANCES.put(tags, metric);
        }
        return (T) metric;
    }



}
