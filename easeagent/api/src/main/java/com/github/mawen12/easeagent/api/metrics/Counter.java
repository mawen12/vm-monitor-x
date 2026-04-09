package com.github.mawen12.easeagent.api.metrics;

public interface Counter extends Metric {
    Counter NOOP = NoOp.INSTANCE;

    void inc();

    void inc(long n);

    long count();

    enum NoOp implements Counter {
        INSTANCE;

        @Override
        public void inc() {

        }

        @Override
        public void inc(long n) {

        }

        @Override
        public long count() {
            return 0;
        }
    }

}
