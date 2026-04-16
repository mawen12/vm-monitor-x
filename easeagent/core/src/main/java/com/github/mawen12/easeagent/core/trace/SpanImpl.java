package com.github.mawen12.easeagent.core.trace;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.trace.Scope;
import com.github.mawen12.easeagent.api.trace.Span;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EaseAgentClassLoader
public class SpanImpl implements Span {
    private static final Map<Kind, brave.Span.Kind> KINDS;

    static {
        Map<Kind, brave.Span.Kind> kinds = new EnumMap<>(Kind.class);
        kinds.put(Kind.CLIENT, brave.Span.Kind.CLIENT);
        kinds.put(Kind.SERVER, brave.Span.Kind.SERVER);
        kinds.put(Kind.PRODUCER, brave.Span.Kind.PRODUCER);
        kinds.put(Kind.CONSUMER, brave.Span.Kind.CONSUMER);
        KINDS = Collections.unmodifiableMap(kinds);
    }

    private final brave.Tracing tracing;
    private final brave.Span span;

    public static Span build(brave.Tracing tracing, brave.Span span) {
        if (span == null) {
            return Span.NOOP;
        }
        return new SpanImpl(tracing, span);
    }

    @Override
    public boolean isNoop() {
        return span.isNoop();
    }

    @Override
    public Span name(String name) {
        span.name(name);
        return this;
    }

    @Override
    public Span tag(String key, String value) {
        span.tag(key, value);
        return this;
    }

    @Override
    public Span annotate(String value) {
        span.annotate(value);
        return this;
    }

    @Override
    public Span annotate(long timestamp, String value) {
        span.annotate(timestamp, value);
        return this;
    }

    @Override
    public Scope maybeScope() {
        return new ScopeImpl(tracing.currentTraceContext().maybeScope(span.context()));
    }

    @Override
    public Span start() {
        span.start();
        return this;
    }

    @Override
    public Span start(long timestamp) {
        span.start(timestamp);
        return this;
    }

    @Override
    public Span kind(Kind kind) {
        span.kind(KINDS.get(kind));
        return this;
    }

    @Override
    public Span error(Throwable throwable) {
        span.error(throwable);
        return this;
    }

    @Override
    public Span remoteServiceName(String remoteServiceName) {
        span.remoteServiceName(remoteServiceName);
        return this;
    }

    @Override
    public boolean remoteIpAndPort(String remoteIp, int remotePort) {
        return span.remoteIpAndPort(remoteIp, remotePort);
    }

    @Override
    public void abandon() {
        span.abandon();
    }

    @Override
    public void finish() {
        span.finish();
    }

    @Override
    public void finish(long timestamp) {
        span.finish(timestamp);
    }

    @Override
    public void flush() {
        span.flush();
    }
}
