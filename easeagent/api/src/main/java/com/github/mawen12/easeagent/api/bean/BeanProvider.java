package com.github.mawen12.easeagent.api.bean;

import com.github.mawen12.easeagent.api.Agent;

public interface BeanProvider {
    void afterPropertiesSet();

    Agent.State onState();
}
