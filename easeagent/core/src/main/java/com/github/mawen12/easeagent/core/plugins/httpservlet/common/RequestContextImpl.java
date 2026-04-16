package com.github.mawen12.easeagent.core.plugins.httpservlet.common;

import com.github.mawen12.easeagent.api.trace.*;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Map;

@AllArgsConstructor
public class RequestContextImpl implements Setter.RequestContext {
    private final Request request;
    private final Span span;
    private final Scope scope;

    @Override
    public boolean isNoop() {
        return false;
    }

    @Override
    public Span span() {
        return span;
    }

    @Override
    public Scope scope() {
        return scope;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public void finish(Getter.Response response) {
        span.finish();
    }

    @Override
    public void setHeader(String name, String value) {
        request.setHeader(name, value);
    }
}
