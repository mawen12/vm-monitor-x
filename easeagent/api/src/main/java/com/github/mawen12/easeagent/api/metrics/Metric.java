package com.github.mawen12.easeagent.api.metrics;

import java.util.function.Function;

public interface Metric {

    enum Type {
        Timer,
        Histogram,
        Meter,
        Counter,
        Gauge;
    }

    enum SubType {
        DEFAULT("00"),
        ERROR("01"),
        CHANNEL("02"),
        CONSUMER("03"),
        PRODUCER("04"),
        CONSUMER_ERROR("05"),
        PRODUCER_ERROR("06"),
        NONE("99");

        private final String code;

        SubType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static SubType value(String code) {
            switch (code) {
                case "00":
                    return DEFAULT;
                case "01":
                    return ERROR;
                case "02":
                    return CHANNEL;
                case "03":
                    return CONSUMER;
                case "04":
                    return PRODUCER;
                case "05":
                    return CONSUMER_ERROR;
                case "06":
                    return PRODUCER_ERROR;
                case "99":
                    return NONE;
                default:
                    throw new IllegalArgumentException("code " + code + " is invalid");
            }
        }
    }

    enum Field {
        MIN_EXECUTION_TIME("min"),
        MAX_EXECUTION_TIME("max"),
        MEAN_EXECUTION_TIME("mean"),
        P25_EXECUTION_TIME("p25"),
        P50_EXECUTION_TIME("p50"),
        P75_EXECUTION_TIME("p75"),
        P95_EXECUTION_TIME("p95"),
        P98_EXECUTION_TIME("p98"),
        P99_EXECUTION_TIME("p99"),
        P999_EXECUTION_TIME("p999"),

        EXECUTION_COUNT("cnt"),
        EXECUTION_ERR_COUNT("errcnt"),

        M1_RATE("m1"),
        M5_RATE("m5"),
        M15_RATE("m15"),
        M1_ERR_RATE("m1err"),
        M5_ERR_RATE("m5err"),
        M15_ERR_RATE("m15err"),

        TIMES("times"),
        TIMES_RATE("times_rate"),

        TOTAL_COLLECTION_TIME("total_collection_time"),
        ;



        private final String field;

        Field(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }

    enum ValueFetcher {
        SnapshotMaxValue(Timer::getMax, Timer.class),
        SnapshotMinValue(Timer::getMin, Timer.class),
        SnapshotMeanValue(Timer::getMean, Timer.class),
        Snapshot25thPercentileValue(Timer::get25thPercentile, Timer.class),
        Snapshot50thPercentileValue(Timer::getMedian, Timer.class),
        Snapshot75thPercentileValue(Timer::get75thPercentile, Timer.class),
        Snapshot95thPercentileValue(Timer::get95thPercentile, Timer.class),
        Snapshot98thPercentileValue(Timer::get98thPercentile, Timer.class),
        Snapshot99thPercentileValue(Timer::get99thPercentile, Timer.class),
        Snapshot999thPercentileValue(Timer::get999thPercentile, Timer.class),
        MeterM1Rate(Meter::getOneMinuteRate, Meter.class),
        MeterM5Rate(Meter::getFiveMinuteRate, Meter.class),
        MeterM15Rate(Meter::getFifteenMinuteRate, Meter.class),
        MeterMeanRate(Meter::getMeanRate, Meter.class),
        MeterCount(Meter::getCount, Meter.class),
        CountingCount(Counter::count, Counter.class),
        ;

        private final Function func;
        private final Class clazz;

        <T, V> ValueFetcher(Function<T, V> func, Class<T> clazz) {
            this.func = func;
            this.clazz = clazz;
        }

        public Object apply(Metric obj) {
            return func.apply(clazz.cast(obj));
        }
    }

    class FieldWrapper {
        private Field field;
        private ValueFetcher valueFetcher;

        public FieldWrapper(Field field, ValueFetcher valueFetcher) {
            this.field = field;
            this.valueFetcher = valueFetcher;
        }

        public static FieldWrapper of(Field field, ValueFetcher valueFetcher) {
            return new FieldWrapper(field, valueFetcher);
        }

        public Field getField() {
            return field;
        }

        public ValueFetcher getValueFetcher() {
            return valueFetcher;
        }
    }
}
