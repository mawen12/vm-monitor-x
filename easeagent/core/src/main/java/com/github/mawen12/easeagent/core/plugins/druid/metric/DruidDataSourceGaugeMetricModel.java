package com.github.mawen12.easeagent.core.plugins.druid.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.GaugeMetricModel;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@EaseAgentClassLoader
@AllArgsConstructor
public class DruidDataSourceGaugeMetricModel implements GaugeMetricModel {
    private final Integer activeCount;
    private final Integer poolingCount;
    private final Integer maxActive;
    private final Integer waitThreadCount;

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