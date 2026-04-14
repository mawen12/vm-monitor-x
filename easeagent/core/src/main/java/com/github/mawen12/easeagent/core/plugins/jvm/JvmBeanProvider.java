package com.github.mawen12.easeagent.core.plugins.jvm;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.spi.BeanProvider;
import com.github.mawen12.easeagent.core.plugins.jvm.metric.JvmGCMetric;
import com.github.mawen12.easeagent.core.plugins.jvm.metric.JvmMemoryMetric;
import com.google.auto.service.AutoService;

@EaseAgentClassLoader
@AutoService(BeanProvider.class)
public class JvmBeanProvider implements BeanProvider {

    @Override
    public void afterPropertiesSet() {
        JvmGCMetric.buildAndRun();
        JvmMemoryMetric.buildAndRun();
    }

    @Override
    public Agent.State onState() {
        return Agent.State.Start;
    }
}
