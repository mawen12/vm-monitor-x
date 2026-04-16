package com.github.mawen12.easeagent.core.plugins.httpservlet.common;

import com.github.mawen12.easeagent.api.trace.Request;
import com.github.mawen12.easeagent.api.trace.Span;
import com.github.mawen12.easeagent.api.trace.TraceConst;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
public class HttpServletRequestWrapper implements Request.HttpRequest {
    private final HttpServletRequest delegate;

    @Override
    public String method() {
        return delegate.getMethod();
    }

    @Override
    public String path() {
        return delegate.getRequestURI();
    }

    @Override
    public String route() {
        Object route = delegate.getAttribute(TraceConst.HTTP_ATTRIBUTE_ROUTER);
        return route instanceof String ? (String) route : ServletUtils.getHttpRoute(delegate);
    }

    @Override
    public String getRemoteAddr() {
        return delegate.getRemoteAddr();
    }

    @Override
    public int getRemotePort() {
        return delegate.getRemotePort();
    }

    @Override
    public Span.Kind kind() {
        return Span.Kind.SERVER;
    }

    @Override
    public boolean cacheScope() {
        return false;
    }

    @Override
    public String header(String name) {
        return delegate.getHeader(name);
    }

    @Override
    public void setHeader(String name, String value) {
        // NOP
    }
}
