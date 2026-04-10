package com.github.mawen12.easeagent.api.metrics;

public interface Snapshot extends Metric {
    Snapshot NOOP = NoOp.INSTANCE;

    double getValue(double quantile);

    long[] getValues();

    long getMax();

    long getMin();

    double getMean();

    default double getMedian() {
        return getValue(0.5);
    }

    default double get25thPercentile() {
        return getValue(0.25);
    }

    default double get75thPercentile() {
        return getValue(0.75);
    }

    default double get95thPercentile() {
        return getValue(0.95);
    }

    default double get98thPercentile() {
        return getValue(0.98);
    }

    default double get99thPercentile() {
        return getValue(0.99);
    }

    default double get999thPercentile() {
        return getValue(0.999);
    }

    enum NoOp implements Snapshot {
        INSTANCE;

        @Override
        public double getValue(double quantile) {
            return 0;
        }

        @Override
        public long[] getValues() {
            return new long[0];
        }

        @Override
        public long getMax() {
            return 0;
        }

        @Override
        public long getMin() {
            return 0;
        }

        @Override
        public double getMean() {
            return 0;
        }
    }

    interface Wrapper extends Snapshot {

        Snapshot unwrap();

        default double getValue(double quantile) {
            return unwrap().getValue(quantile);
        }

        default long[] getValues() {
            return unwrap().getValues();
        }

        default long getMax() {
            return unwrap().getMax();
        }

        default long getMin() {
            return unwrap().getMin();
        }

        default double getMean() {
            return unwrap().getMean();
        }
    }
}
