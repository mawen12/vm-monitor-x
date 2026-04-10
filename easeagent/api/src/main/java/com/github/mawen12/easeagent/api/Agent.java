package com.github.mawen12.easeagent.api;

import com.github.mawen12.easeagent.api.config.Config;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.manager.ContextManager;
import com.github.mawen12.easeagent.api.metrics.MetricRegistry;
import com.github.mawen12.easeagent.api.metrics.MetricRegistryManager;
import com.github.mawen12.easeagent.api.metrics.NameFactory;
import com.github.mawen12.easeagent.api.metrics.Tags;

import java.util.HashMap;
import java.util.Map;

public class Agent {

    public static ContextManager contextManager = () -> Context.NoOp.INSTANCE;
    public static MetricRegistryManager metricRegistryManager = null;
    public static Map<String, Object> additionalAttributes = new HashMap<>();
    public static Config config;

    public static Context getContext() {
        return contextManager.getContext();
    }

    public static MetricRegistry newMetricRegistry(Tags tags, NameFactory nameFactory) {
        return metricRegistryManager.newMetricRegistry(tags, additionalAttributes, nameFactory);
    }
}
