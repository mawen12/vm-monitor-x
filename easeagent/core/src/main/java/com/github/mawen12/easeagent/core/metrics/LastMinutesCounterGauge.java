package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.GaugeMetricModel;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@EaseAgentClassLoader
@AllArgsConstructor
public class LastMinutesCounterGauge implements GaugeMetricModel {
    private final long m1Count;
    private final long m5Count;
    private final long m15Count;

    @Override
    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("m1cnt", m1Count);
        map.put("m5cnt", m5Count);
        map.put("m15cnt", m15Count);
        return map;
    }
}
