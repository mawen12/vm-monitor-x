package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

@SharedToBootstrap
public interface MetricRegistry {
    MetricRegistry NOOP = NoOp.INSTANCE;

    Counter counter(String name);

    Timer timer(String name);

    Histogram histogram(String name);

    Meter meter(String name);

    <T> Gauge<T> gauge(String name, MetricSupplier<Gauge<T>> supplier);

    enum NoOp implements MetricRegistry {
        INSTANCE;

        @Override
        public Counter counter(String name) {
            return null;
        }

        @Override
        public Timer timer(String name) {
            return null;
        }

        @Override
        public Histogram histogram(String name) {
            return null;
        }

        @Override
        public Meter meter(String name) {
            return null;
        }

        @Override
        public <T> Gauge<T> gauge(String name, MetricSupplier<Gauge<T>> supplier) {
            return null;
        }
    }
}
