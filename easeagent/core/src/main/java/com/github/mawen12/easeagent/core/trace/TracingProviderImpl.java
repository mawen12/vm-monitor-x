package com.github.mawen12.easeagent.core.trace;

import brave.propagation.ThreadLocalCurrentTraceContext;
import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.spi.BeanProvider;
import com.github.mawen12.easeagent.api.trace.Tracing;
import com.google.auto.service.AutoService;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

@EaseAgentClassLoader
@AutoService(BeanProvider.class)
public class TracingProviderImpl implements BeanProvider {
    private Tracing tracing;
    private brave.Tracing braveTracing;

    @Override
    public void afterPropertiesSet() {
        ThreadLocalCurrentTraceContext currentTraceContext = ThreadLocalCurrentTraceContext.newBuilder()
                .addScopeDecorator(AgentMDCScopeDecorator.INSTANCE)
                .build();

        OkHttpSender sender = OkHttpSender.create("http://localhost:9411/api/v2/spans");
        AsyncZipkinSpanHandler spanHandler = AsyncZipkinSpanHandler.create(sender);

        this.braveTracing = brave.Tracing.newBuilder()
                .localServiceName(Agent.config.getString("name"))
                .supportsJoin(false)
                .traceId128Bit(false)
                .currentTraceContext(currentTraceContext)
                .addSpanHandler(spanHandler)
                .build();

        this.tracing = TracingImpl.build(braveTracing);

        Agent.tracing = this.tracing;
    }

    @Override
    public Agent.State onState() {
        return Agent.State.Start;
    }
}
