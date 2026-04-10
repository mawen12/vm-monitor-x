package com.github.mawen12.easeagent.core.plugins.druid;

import com.github.mawen12.easeagent.api.bean.BeanProvider;
import com.github.mawen12.easeagent.core.plugins.druid.metric.DruidDataSourceMetric;
import com.google.auto.service.AutoService;

@AutoService(BeanProvider.class)
public class DruidBeanProvider implements BeanProvider {

    @Override
    public void afterPropertiesSet() {
        DruidDataSourceMetric.buildAndRun();
    }
}
