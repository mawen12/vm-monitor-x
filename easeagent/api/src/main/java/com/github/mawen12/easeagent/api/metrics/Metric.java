package com.github.mawen12.easeagent.api.metrics;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

public interface Metric {

    enum Type {
        Timer, Histogram, Meter, Counter, Gauge;
    }

    @Getter
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

    @Getter
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


        PRODUCER_MIN_EXECUTION_TIME("prodmin"),
        PRODUCER_MAX_EXECUTION_TIME("prodmax"),
        PRODUCER_MEAN_EXECUTION_TIME("prodmean"),
        PRODUCER_P25_EXECUTION_TIME("prodp25"),
        PRODUCER_P50_EXECUTION_TIME("prodp50"),
        PRODUCER_P75_EXECUTION_TIME("prodp75"),
        PRODUCER_P95_EXECUTION_TIME("prodp95"),
        PRODUCER_P98_EXECUTION_TIME("prodp98"),
        PRODUCER_P99_EXECUTION_TIME("prodp99"),
        PRODUCER_P999_EXECUTION_TIME("prodp999"),

        CONSUMER_MIN_EXECUTION_TIME("consmin"),
        CONSUMER_MAX_EXECUTION_TIME("consmax"),
        CONSUMER_MEAN_EXECUTION_TIME("consmean"),
        CONSUMER_P25_EXECUTION_TIME("consp25"),
        CONSUMER_P50_EXECUTION_TIME("consp50"),
        CONSUMER_P75_EXECUTION_TIME("consp75"),
        CONSUMER_P95_EXECUTION_TIME("consp95"),
        CONSUMER_P98_EXECUTION_TIME("consp98"),
        CONSUMER_P99_EXECUTION_TIME("consp99"),
        CONSUMER_P999_EXECUTION_TIME("consp999"),

        EXECUTION_PRODUCER_COUNT("prodcnt"),
        EXECUTION_PRODUCER_ERROR_COUNT("proderrcnt"),
        EXECUTION_CONSUMER_COUNT("conscnt"),
        EXECUTION_CONSUMER_ERROR_COUNT("conserrcnt"),

        PRODUCER_M1_RATE("prodm1"),
        PRODUCER_M5_RATE("prodm5"),
        PRODUCER_M15_RATE("prodm15"),
        PRODUCER_M1_ERROR_RATE("proderrm1"),
        PRODUCER_M5_ERROR_RATE("proderrm5"),
        PRODUCER_M15_ERROR_RATE("proderrm15"),

        CONSUMER_M1_RATE("consm1"),
        CONSUMER_M5_RATE("consm5"),
        CONSUMER_M15_RATE("consm15"),
        CONSUMER_M1_ERROR_RATE("conserrm1"),
        CONSUMER_M5_ERROR_RATE("conserrm5"),
        CONSUMER_M15_ERROR_RATE("conserrm15"),


        ;

        private final String field;

        Field(String field) {
            this.field = field;
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

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class FieldWrapper {
        private final Field field;
        private final ValueFetcher valueFetcher;

        public static FieldWrapper of(Field field, ValueFetcher valueFetcher) {
            return new FieldWrapper(field, valueFetcher);
        }
    }
}
