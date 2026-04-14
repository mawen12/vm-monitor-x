package com.github.mawen12.easeagent.api;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.config.Config;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.manager.ContextManager;
import com.github.mawen12.easeagent.api.metrics.MetricRegistry;
import com.github.mawen12.easeagent.api.metrics.MetricRegistryManager;
import com.github.mawen12.easeagent.api.metrics.NameFactory;
import com.github.mawen12.easeagent.api.metrics.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SharedToBootstrap("used by CommonInlineAdvice")
public class Agent {

    public static ContextManager contextManager = () -> Context.NoOp.INSTANCE;
    public static MetricRegistryManager metricRegistryManager = null;
    public static Map<String, Object> additionalAttributes = new HashMap<>();
    public static Config config;
    public static Map<Agent.State, List<Listener>> listeners = new HashMap<>();

    public static Context getContext() {
        return contextManager.getContext();
    }

    public static MetricRegistry newMetricRegistry(Tags tags, NameFactory nameFactory) {
        return metricRegistryManager.newMetricRegistry(tags, additionalAttributes, nameFactory);
    }

    public static void addListener(State state, Listener listener) {
        listeners.computeIfAbsent(state, k -> new ArrayList<>()).add(listener);
    }

    public static void markStart() {
        notifyState(State.Start);
    }

    public static void markDruidReady() {
        notifyState(State.DruidReady);
    }

    public static void markSpringBootReady() {
        notifyState(State.SpringBootReady);
    }

    public static void markAgentReady() {
        notifyState(State.AgentReady);
    }

    public static void notifyState(State state) {
        List<Listener> stateListeners = listeners.get(state);
        if (stateListeners != null) {
            for (Listener listener : stateListeners) {
                listener.onState();
            }
        }
    }

    public enum State {
        Start,
        DruidReady,
        SpringBootReady,
        AgentReady;
    }

    public interface Listener {
        void onState();
    }
}
