package com.github.mawen12.easeagent.core.plugins.httpservlet.trace;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.api.trace.*;
import com.github.mawen12.easeagent.core.plugins.httpservlet.common.HttpServletRequestWrapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

@EaseAgentClassLoader
public enum DoFilterTraceInterceptor implements NonReentrantInterceptor {
    INSTANCE;

    private static final String PROCESS_CONTEXT = DoFilterTraceInterceptor.class.getName() + ".RequestContext";

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        HttpServletRequest servletRequest = (HttpServletRequest) methodInfo.getArgs()[0];
        Setter.RequestContext requestContext = (Setter.RequestContext) servletRequest.getAttribute(PROCESS_CONTEXT);
        if (requestContext != null) {
            return;
        }

        Request.HttpRequest httpRequest = new HttpServletRequestWrapper(servletRequest);
        requestContext = ctx.serverReceive(httpRequest);
        servletRequest.setAttribute(PROCESS_CONTEXT, requestContext);
        TraceUtils.handleReceive(requestContext.span(), httpRequest);
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        HttpServletRequest serverRequest = (HttpServletRequest) methodInfo.getArgs()[0];
        HttpServletResponse serverResponse = (HttpServletResponse) methodInfo.getArgs()[1];
        Setter.RequestContext requestContext = (Setter.RequestContext) serverRequest.getAttribute(PROCESS_CONTEXT);

        try {
            Span span = requestContext.span();
            if (!serverRequest.isAsyncStarted()) {
                TraceUtils.finish(span, new Response(methodInfo.getThrowable(), serverRequest, serverResponse));
            } else if (methodInfo.getThrowable() != null) {
                span.error(methodInfo.getThrowable());
                span.finish();
            } else {
                // async handle
                serverRequest.getAsyncContext().addListener(new TracingAsyncListener(requestContext));
            }
        } finally {
            requestContext.scope().close();
        }
    }

    @Override
    public Order order() {
        return Order.TRACING;
    }

    @AllArgsConstructor
    public static class Response implements Getter.Response.HttpResponse {
        private final Throwable caught;
        private final HttpServletRequest request;
        private final HttpServletResponse response;

        @Override
        public String method() {
            return request.getMethod();
        }

        @Override
        public String route() {
            Object route = request.getAttribute(TraceConst.HTTP_ATTRIBUTE_ROUTER);
            return route instanceof String ? (String) route : null;
        }

        @Override
        public int statusCode() {
            if (response == null) {
                return 0;
            }
            int status = response.getStatus();
            if (caught != null && status == 200) {
                if (caught instanceof UnavailableException) {
                    return ((UnavailableException) caught).isPermanent() ? 404 : 503;
                } else {
                    return 500;
                }
            }
            return status;
        }

        @Override
        public Throwable error() {
            if (caught != null) {
                return caught;
            }
            Object error = request.getAttribute(TraceConst.HTTP_ATTRIBUTE_ERROR);
            if (error instanceof Throwable) {
                return (Throwable) error;
            } else {
                error = request.getAttribute("javax.servlet.error.exception");
                return error instanceof Throwable ? (Throwable) error : null;
            }
        }

        @Override
        public String header(String name) {
            return response.getHeader(name);
        }
    }

    @RequiredArgsConstructor
    public static final class TracingAsyncListener implements AsyncListener {
        private final Setter.RequestContext requestContext;
        private final AtomicBoolean sendHandled = new AtomicBoolean();

        @Override
        public void onComplete(AsyncEvent event) {
            HttpServletRequest servletRequest = (HttpServletRequest) event.getSuppliedRequest();
            if (sendHandled.compareAndSet(false, true)) {
                HttpServletResponse servletResponse = (HttpServletResponse) event.getSuppliedResponse();
                Response response = new Response(event.getThrowable(), servletRequest, servletResponse);

                TraceUtils.save(requestContext.span(), response);
                requestContext.finish(response);
            }
        }

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {
            onError(event);
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
            ServletRequest request = event.getSuppliedRequest();
            if (request.getAttribute(TraceConst.HTTP_ATTRIBUTE_ERROR) == null) {
                request.setAttribute(TraceConst.HTTP_ATTRIBUTE_ERROR, event.getThrowable());
            }
        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {
            AsyncContext asyncContext = event.getAsyncContext();
            if (asyncContext != null) {
                asyncContext.addListener(this, event.getSuppliedRequest(), event.getSuppliedResponse());
            }
        }
    }
}
