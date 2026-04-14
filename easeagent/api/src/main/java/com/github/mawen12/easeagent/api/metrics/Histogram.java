package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

@SharedToBootstrap
public interface Histogram extends Snapshot, Metric {
    Histogram NOOP = NoOp.INSTANCE;

    void update(long value);

    long getCount();

    enum NoOp implements Histogram, Snapshot.Wrapper {
        INSTANCE;

        @Override
        public void update(long value) {

        }

        @Override
        public long getCount() {
            return 0;
        }

        @Override
        public Snapshot unwrap() {
            return Snapshot.NOOP;
        }
    }
}
