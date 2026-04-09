package com.github.mawen12.easeagent.api;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.manager.ContextManager;
import com.github.mawen12.easeagent.api.metrics.MetricRegistry;
import com.github.mawen12.easeagent.api.metrics.MetricRegistryManager;

public class Agent {

    public static ContextManager contextManager = () -> Context.NoOp.INSTANCE;
    public static MetricRegistryManager metricRegistryManager = () -> MetricRegistry.NOOP;

    public static Context getContext() {
        return contextManager.getContext();
    }

    public static MetricRegistry getMetricRegistry() {
        return metricRegistryManager.getMetricRegistry();
    }
}
