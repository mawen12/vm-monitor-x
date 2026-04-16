package com.github.mawen12.easeagent.core.plugins.httpservlet.trace;

import com.github.mawen12.easeagent.api.trace.Getter;
import com.github.mawen12.easeagent.api.trace.Request;
import com.github.mawen12.easeagent.api.trace.Span;
import com.github.mawen12.easeagent.api.trace.TraceConst;

import java.util.Objects;

public class TraceUtils {

    public static void handleReceive(Span span, Request.HttpRequest httpRequest) {
        span.name(httpRequest.name());
        span.tag(TraceConst.TAG_HTTP_ROUTE, httpRequest.route());
        span.tag(TraceConst.TAG_HTTP_METHOD, httpRequest.method());
        span.tag(TraceConst.TAG_HTTP_PATH, httpRequest.path());
        span.remoteIpAndPort(httpRequest.getRemoteAddr(), httpRequest.getRemotePort());
        span.start();
    }

    public static void finish(Span span, Getter.Response.HttpResponse httpResponse) {
        save(span, httpResponse);
        span.finish();
    }

    public static void save(Span span,  Getter.Response.HttpResponse httpResponse) {
        Throwable error = httpResponse.error();
        if (error != null) {
            span.error(error);
        }

        int statusCode = httpResponse.statusCode();
        if (statusCode != 0) {
            if (statusCode < 200 || statusCode > 299) {
                String rewroteName = rewriteName(httpResponse);
                if (rewroteName != null) {
                    span.name(rewroteName);
                }
            }
            span.tag(TraceConst.TAG_STATUS_CODE, String.valueOf(statusCode));
        }
    }

    private static String rewriteName(Getter.Response.HttpResponse httpResponse) {
        String method = httpResponse.method();
        if (method == null) {
            return null;
        }
        String route = httpResponse.route();
        if (route == null) {
            return null;
        }
        if (Objects.equals(route, "")) {
            return method + " " + route;
        }
        return translateCode(method, httpResponse.statusCode());
    }

    private static String translateCode(String method, int statusCode) {
        switch (statusCode) {
            case 301:
            case 302:
            case 303:
            case 304:
            case 305:
            case 306:
            case 307:
                return method + " redirected";
            case 404:
                return method + " not_found";
            default:
                return null;
        }
    }
}
