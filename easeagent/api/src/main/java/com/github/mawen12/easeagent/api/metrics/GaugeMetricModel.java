package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

import java.util.Map;

@SharedToBootstrap
public interface GaugeMetricModel {

    Map<String, Object> toHashMap();
}
