package com.github.mawen12.easeagent.core.trace;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.trace.Span;
import com.github.mawen12.easeagent.api.trace.Tracing;
import com.github.mawen12.easeagent.api.utils.Null;

@EaseAgentClassLoader
public class TracingImpl implements Tracing {

    private final brave.Tracing tracing;
    private final brave.Tracer tracer;

    private TracingImpl(brave.Tracing tracing) {
        this.tracing = tracing;
        this.tracer = tracing.tracer();
    }

    public static Tracing build(brave.Tracing tracing) {
        if (tracing == null) {
            return Tracing.NOOP;
        }
        return new TracingImpl(tracing);
    }

    @Override
    public boolean isNoop() {
        return false;
    }

    @Override
    public boolean hasCurrentSpan() {
        return tracing.currentTraceContext().get() != null;
    }

    @Override
    public Span currentSpan() {
        Span span = Span.NOOP;
        if (tracer != null) {
            span = SpanImpl.build(this, tracer.currentSpan());
        }
        return Null.of(span, Span.NOOP);
    }

    @Override
    public Span nextSpan() {
        return SpanImpl.build(this, tracer.nextSpan());
    }
}
