package com.github.mawen12.easeagent.core.plugins.tomcat;

import com.github.mawen12.easeagent.api.bean.BeanProvider;
import com.github.mawen12.easeagent.core.plugins.tomcat.metric.TomcatMetric;
import com.google.auto.service.AutoService;

@AutoService(BeanProvider.class)
public class TomcatBeanProvider implements BeanProvider {

    @Override
    public void afterPropertiesSet() {
        TomcatMetric.buildAndRun();
    }
}
