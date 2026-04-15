package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.utils.Sets;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.github.mawen12.easeagent.api.metrics.Metric.Field.*;
import static com.github.mawen12.easeagent.api.metrics.Metric.FieldWrapper.of;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.ERROR;
import static com.github.mawen12.easeagent.api.metrics.Metric.ValueFetcher.*;

@SharedToBootstrap
public class ServerMetric extends ServiceMetric {
    public static final NameFactory NAME_FACTORY = ServerNameFactory.INSTANCE.nameFactory();

    public ServerMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void collectMetric(String key, int statusCode, Throwable throwable, long startMillis, long endMillis) {
        timer(key, DEFAULT).update(Duration.ofMillis(endMillis - startMillis));
        meter(key, DEFAULT).mark();
        counter(key, DEFAULT).inc();

        Meter meter = meter(key, DEFAULT);
        meter.mark();
        Meter errMeter = meter(key, ERROR);

        if (statusCode >= 400 || throwable != null) {
            errMeter.mark();
            counter(key, ERROR).inc();
        }

        gauge(key, DEFAULT, () -> () -> {
            BigDecimal m1ErrorPercent = BigDecimal.ZERO;
            BigDecimal m5ErrorPercent = BigDecimal.ZERO;
            BigDecimal m15ErrorPercent = BigDecimal.ZERO;

            BigDecimal error = BigDecimal.valueOf(errMeter.getOneMinuteRate()).setScale(5, BigDecimal.ROUND_HALF_DOWN);
            BigDecimal n = BigDecimal.valueOf(meter.getOneMinuteRate());
            if (n.compareTo(BigDecimal.ZERO) > 0) {
                m1ErrorPercent = error.divide(n, 2, BigDecimal.ROUND_HALF_UP);
            }

            error = BigDecimal.valueOf(errMeter.getFiveMinuteRate()).setScale(5, BigDecimal.ROUND_HALF_DOWN);
            n = BigDecimal.valueOf(meter.getFiveMinuteRate());
            if (n.compareTo(BigDecimal.ZERO) > 0) {
                m5ErrorPercent = error.divide(n, 2, BigDecimal.ROUND_HALF_UP);
            }

            error = BigDecimal.valueOf(errMeter.getFifteenMinuteRate()).setScale(5, BigDecimal.ROUND_HALF_DOWN);
            n = BigDecimal.valueOf(meter.getFifteenMinuteRate());
            if (n.compareTo(BigDecimal.ZERO) > 0) {
                m5ErrorPercent = error.divide(n, 2, BigDecimal.ROUND_HALF_UP);
            }

            return new ErrorPercentModelGauge(m1ErrorPercent, m5ErrorPercent, m15ErrorPercent);
        });
    }

    static class ErrorPercentModelGauge implements GaugeMetricModel {
        private final BigDecimal m1ErrorPercent;
        private final BigDecimal m5ErrorPercent;
        private final BigDecimal m15ErrorPercent;

        public ErrorPercentModelGauge(BigDecimal m1ErrorPercent, BigDecimal m5ErrorPercent, BigDecimal m15ErrorPercent) {
            this.m1ErrorPercent = m1ErrorPercent;
            this.m5ErrorPercent = m5ErrorPercent;
            this.m15ErrorPercent = m15ErrorPercent;
        }

        @Override
        public Map<String, Object> toHashMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("m1errpct", m1ErrorPercent);
            map.put("m5errpct", m5ErrorPercent);
            map.put("m15errpct", m15ErrorPercent);
            return map;
        }
    }

    enum ServerNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createDefault();
        }
    }
}
