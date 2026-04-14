package com.github.mawen12.easeagent.core.plugins.druid;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.spi.BeanProvider;
import com.github.mawen12.easeagent.core.plugins.druid.metric.DruidDataSourceMetric;
import com.google.auto.service.AutoService;

@EaseAgentClassLoader
@AutoService(BeanProvider.class)
public class DruidBeanProvider implements BeanProvider {

    @Override
    public void afterPropertiesSet() {
        DruidDataSourceMetric.buildAndRun();
    }

    @Override
    public Agent.State onState() {
        return Agent.State.SpringBootReady;
    }
}
