package com.github.mawen12.easeagent.core.plugins.tomcat;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.spi.BeanProvider;
import com.github.mawen12.easeagent.core.plugins.tomcat.metric.TomcatMetric;
import com.google.auto.service.AutoService;

@EaseAgentClassLoader
@AutoService(BeanProvider.class)
public class TomcatBeanProvider implements BeanProvider {

    @Override
    public void afterPropertiesSet() {
        TomcatMetric.buildAndRun();
    }

    @Override
    public Agent.State onState() {
        return Agent.State.SpringBootReady;
    }
}
