package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

@SharedToBootstrap
public interface Gauge<T> extends Metric {

    T getValue();

    class NoOp<T> implements Gauge<T> {
        public static NoOp INSTANCE = new NoOp();

        @Override
        public T getValue() {
            return null;
        }
    }
}
