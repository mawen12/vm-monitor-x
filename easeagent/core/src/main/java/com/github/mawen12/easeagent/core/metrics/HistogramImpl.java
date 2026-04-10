package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.metrics.Histogram;
import com.github.mawen12.easeagent.api.metrics.Snapshot;

public class HistogramImpl implements Histogram, Snapshot.Wrapper {

    private final com.codahale.metrics.Histogram histogram;
    private final Snapshot snapshot;

    private HistogramImpl(com.codahale.metrics.Histogram histogram) {
        this.histogram = histogram;
        this.snapshot = SnapshotImpl.build(histogram.getSnapshot());
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

    @Override
    public Snapshot unwrap() {
        return snapshot;
    }

//    @Override
//    public double getValue(double quantile) {
//        return histogram.getSnapshot().getValue(quantile);
//    }
//
//    @Override
//    public long[] getValues() {
//        return histogram.getSnapshot().getValues();
//    }
//
//    @Override
//    public long getMax() {
//        return histogram.getSnapshot().getMax();
//    }
//
//    @Override
//    public long getMin() {
//        return histogram.getSnapshot().getMin();
//    }
//
//    @Override
//    public double getMean() {
//        return histogram.getSnapshot().getMean();
//    }
}
