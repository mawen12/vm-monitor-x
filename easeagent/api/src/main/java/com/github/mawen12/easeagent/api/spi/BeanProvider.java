package com.github.mawen12.easeagent.api.spi;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

@SharedToBootstrap
public interface BeanProvider {
    void afterPropertiesSet();

    Agent.State onState();
}
