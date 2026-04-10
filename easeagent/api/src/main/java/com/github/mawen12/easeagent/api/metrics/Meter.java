package com.github.mawen12.easeagent.api.metrics;

public interface Meter extends Metric {
    Meter NOOP = NoOp.INSTANCE;

    void mark();

    double getOneMinuteRate();

    double getFiveMinuteRate();

    double getFifteenMinuteRate();

    double getMeanRate();

    long getCount();

    enum NoOp implements Meter {
        INSTANCE;

        @Override
        public void mark() {

        }

        @Override
        public double getOneMinuteRate() {
            return 0;
        }

        @Override
        public double getFiveMinuteRate() {
            return 0;
        }

        @Override
        public double getFifteenMinuteRate() {
            return 0;
        }

        @Override
        public double getMeanRate() {
            return 0;
        }


        @Override
        public long getCount() {
            return 0;
        }
    }
}
