package com.github.mawen12.easeagent.api.metrics;

public interface Histogram extends Metric {
    Histogram NOOP = NoOp.INSTANCE;

    void update(long value);

    long getCount();

    enum NoOp implements Histogram {
        INSTANCE;

        @Override
        public void update(long value) {

        }

        @Override
        public long getCount() {
            return 0;
        }
    }
}
