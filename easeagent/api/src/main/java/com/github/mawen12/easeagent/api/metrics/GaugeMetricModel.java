package com.github.mawen12.easeagent.api.metrics;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface GaugeMetricModel {

    Map<String, Object> toHashMap();

    class LastMinutesCounterGauge implements GaugeMetricModel {
        private final long m1Count;
        private final long m5Count;
        private final long m15Count;

        public LastMinutesCounterGauge(Meter meter) {
            this.m1Count = (long) meter.getOneMinuteRate();
            this.m5Count = (long) meter.getFiveMinuteRate();
            this.m15Count = (long) meter.getFifteenMinuteRate();
        }

        @Override
        public Map<String, Object> toHashMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("m1cnt", m1Count);
            map.put("m5cnt", m5Count);
            map.put("m15cnt", m15Count);
            return map;
        }
    }

    class ErrorPercentModelGauge implements GaugeMetricModel {
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

    class JvmMemoryGaugeMetricModel implements GaugeMetricModel {
        private Long bytesInit;
        private Long bytesUsed;
        private Long bytesCommitted;
        private Long bytesMax;

        public JvmMemoryGaugeMetricModel(Long bytesInit, Long bytesUsed, Long bytesCommitted, Long bytesMax) {
            this.bytesInit = bytesInit;
            this.bytesUsed = bytesUsed;
            this.bytesCommitted = bytesCommitted;
            this.bytesMax = bytesMax;
        }

        @Override
        public Map<String, Object> toHashMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("bytes-init", bytesInit);
            map.put("bytes-used", bytesUsed);
            map.put("bytes-committed", bytesCommitted);
            map.put("bytes-max", bytesMax);
            return map;
        }
    }

    class DruidDataSourceGaugeMetricModel implements GaugeMetricModel {
        private Integer activeCount;
        private Integer poolingCount;
        private Integer maxActive;
        private Integer waitThreadCount;

        public DruidDataSourceGaugeMetricModel(Integer activeCount, Integer poolingCount, Integer maxActive, Integer waitThreadCount) {
            this.activeCount = activeCount;
            this.poolingCount = poolingCount;
            this.maxActive = maxActive;
            this.waitThreadCount = waitThreadCount;
        }

        @Override
        public Map<String, Object> toHashMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("active-count", activeCount);
            map.put("pooling-count", poolingCount);
            map.put("max-active", maxActive);
            map.put("wait-thread-count", waitThreadCount);
            return map;
        }
    }

    class TomcatGaugeMetricModel implements GaugeMetricModel {
        private Integer currentThreadsBusy;
        private Integer currentThreadCount;
        private Integer maxThreads;

        public TomcatGaugeMetricModel(Integer currentThreadsBusy, Integer currentThreadCount, Integer maxThreads) {
            this.currentThreadsBusy = currentThreadsBusy;
            this.currentThreadCount = currentThreadCount;
            this.maxThreads = maxThreads;
        }

        @Override
        public Map<String, Object> toHashMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("current-threads-busy", currentThreadsBusy);
            map.put("current-thread-count", currentThreadCount);
            map.put("max-threads", maxThreads);
            return map;
        }
    }
}
