package com.github.mawen12.easeagent.core.context;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.trace.*;
import com.github.mawen12.easeagent.api.utils.Null;
import com.github.mawen12.easeagent.core.plugins.httpservlet.common.RequestContextImpl;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@EaseAgentClassLoader("used by ContextManagerImpl, then export to Agent#getContext")
public class SessionContext implements Context {

    private final Map<Object, Object> context = new HashMap<>();
    private final Map<Object, Integer> entered = new HashMap<>();
    @Getter
    private Tracing tracing = Tracing.NOOP;

    @Override
    public boolean isNoop() {
        return false;
    }

    @Override
    public <V> V get(Object key) {
        return Null.of(context.get(key));
    }

    @Override
    public <V> V remove(Object key) {
        return Null.of(context.remove(key));
    }

    @Override
    public <V> V put(Object key, V value) {
        context.put(key, value);
        return value;
    }

    @Override
    public int enter(Object key) {
        Integer count = entered.get(key);
        if (count == null) {
            count = 1;
        } else {
            count++;
        }
        entered.put(key, count);
        return count;
    }

    @Override
    public int exit(Object key) {
        Integer count = entered.get(key);
        if (count == null) {
            return 0;
        }
        entered.put(key, count - 1);
        return count;
    }

    public void setTracing(Tracing tracing) {
        this.tracing = Null.of(tracing, Tracing.NOOP);
    }

    @Override
    public Span currentSpan() {
        return tracing.currentSpan();
    }

    @Override
    public Span nextSpan() {
        return tracing.nextSpan();
    }

    @Override
    public Setter.RequestContext clientRequest(Request request) {
        Span span = tracing.nextSpan();
        RequestContextImpl requestContext = new RequestContextImpl(request, span, span.maybeScope());

        return requestContext;
    }

    @Override
    public Setter.RequestContext serverReceive(Request request) {
        Span span = tracing.nextSpan();
        RequestContextImpl requestContext = new RequestContextImpl(request, span, span.maybeScope());

        span.kind(request.kind());
        span.name(request.name());

        return requestContext;
    }
}
