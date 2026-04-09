package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.metrics.Histogram;

public class HistogramImpl implements Histogram {

    private com.codahale.metrics.Histogram histogram;

    private HistogramImpl(com.codahale.metrics.Histogram histogram) {
        this.histogram = histogram;
    }

    public static Histogram build(com.codahale.metrics.Histogram histogram) {
        return histogram == null ? Histogram.NOOP : new HistogramImpl(histogram);
    }

    @Override
    public void update(long value) {
        histogram.update(value);
    }

    @Override
    public long getCount() {
        return histogram.getCount();
    }
}
