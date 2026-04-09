package com.github.mawen12.easeagent.core.metrics;

import com.codahale.metrics.Counter;
import io.prometheus.client.Collector;

import java.util.*;

public class AgentPrometheusExports extends Collector implements Collector.Describable {

    @Override
    public List<MetricFamilySamples> collect() {
        Map<String, MetricFamilySamples> maps = new HashMap<>();



        return new ArrayList<>(maps.values());
    }

    @Override
    public List<MetricFamilySamples> describe() {
        return Collections.emptyList();
    }

//    class CounterExports extends Exports<Counter> {
//
//    }
}
