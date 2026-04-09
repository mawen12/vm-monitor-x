package com.github.mawen12.easeagent.api.metrics;

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
}
