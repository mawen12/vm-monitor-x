package com.github.mawen12.easeagent.core.plugins.tomcat.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.GaugeMetricModel;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@EaseAgentClassLoader
@AllArgsConstructor
public class TomcatGaugeMetricModel implements GaugeMetricModel {
    private final Integer currentThreadsBusy;
    private final Integer currentThreadCount;
    private final Integer maxThreads;

    @Override
    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("current-threads-busy", currentThreadsBusy);
        map.put("current-thread-count", currentThreadCount);
        map.put("max-threads", maxThreads);
        return map;
    }
}
