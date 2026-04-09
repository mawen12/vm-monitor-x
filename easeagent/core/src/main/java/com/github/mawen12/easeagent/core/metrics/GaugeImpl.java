package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.metrics.Gauge;

public class GaugeImpl<T> implements com.codahale.metrics.Gauge<T> {
    private Gauge<T> g;

    private GaugeImpl(Gauge<T> g) {
        this.g = g;
    }

    public static <T> GaugeImpl<T> build(Gauge<T> g) {
        return g == null ? null : new GaugeImpl<>(g);
    }

    @Override
    public T getValue() {
        return g.getValue();
    }

    public Gauge<T> getG() {
        return g;
    }
}
