package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.Agent;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

public class ServiceMetricRegistry {

    public static final ConcurrentMap<Tags, ServiceMetric> INSTANCES = new ConcurrentHashMap<>();

    public static <T extends ServiceMetric> T getOrCreate(Tags tags, NameFactory.Supplier nameFactorySupplier, BiFunction<MetricRegistry, NameFactory, T> supplier) {
        return getOrCreate(tags, nameFactorySupplier.nameFactory(), supplier);
    }

    public static <T extends ServiceMetric> T getOrCreate(Tags tags, NameFactory nameFactory, BiFunction<MetricRegistry, NameFactory, T> supplier) {
        ServiceMetric metric = INSTANCES.get(tags);
        if (metric != null) {
            return (T) metric;
        }

        synchronized (INSTANCES) {
            metric = INSTANCES.get(tags);
            if (metric != null) {
                return (T) metric;
            }

            metric = supplier.apply(Agent.newMetricRegistry(tags, new HashMap<>(), nameFactory), nameFactory);
            INSTANCES.put(tags, metric);
        }
        return (T) metric;
    }


}
