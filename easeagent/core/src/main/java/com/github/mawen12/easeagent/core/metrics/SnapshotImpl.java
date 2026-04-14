package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.Snapshot;

@EaseAgentClassLoader
public class SnapshotImpl implements Snapshot {

    private final com.codahale.metrics.Snapshot snapshot;

    private SnapshotImpl(com.codahale.metrics.Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public static Snapshot build(com.codahale.metrics.Snapshot snapshot) {
        return snapshot == null ? Snapshot.NOOP : new SnapshotImpl(snapshot);
    }

    @Override
    public double getValue(double quantile) {
        return snapshot.getValue(quantile);
    }

    @Override
    public long[] getValues() {
        return snapshot.getValues();
    }

    @Override
    public long getMax() {
        return snapshot.getMax();
    }

    @Override
    public long getMin() {
        return snapshot.getMin();
    }

    @Override
    public double getMean() {
        return snapshot.getMean();
    }
}
