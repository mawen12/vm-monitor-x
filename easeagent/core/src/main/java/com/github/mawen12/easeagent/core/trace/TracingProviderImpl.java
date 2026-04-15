package com.github.mawen12.easeagent.core.trace;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.spi.BeanProvider;
import com.github.mawen12.easeagent.api.trace.Tracing;
import com.google.auto.service.AutoService;

@EaseAgentClassLoader
@AutoService(BeanProvider.class)
public class TracingProviderImpl implements BeanProvider {
    private Tracing tracing;
    private brave.Tracing braveTracing;

    @Override
    public void afterPropertiesSet() {
        this.braveTracing = brave.Tracing.newBuilder()
                .localServiceName(Agent.config.getString("name"))
                .traceId128Bit(false)
                .build();

        this.tracing = TracingImpl.build(braveTracing);

        Agent.tracing = this.tracing;
    }

    @Override
    public Agent.State onState() {
        return Agent.State.Start;
    }
}
