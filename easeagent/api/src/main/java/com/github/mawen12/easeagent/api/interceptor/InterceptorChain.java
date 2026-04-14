package com.github.mawen12.easeagent.api.interceptor;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.utils.ContextUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SharedToBootstrap("used by CommonInlineAdvice")
public class InterceptorChain {
    private final List<Interceptor> interceptors;

    public InterceptorChain(List<Interceptor> interceptors) {
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        this.interceptors = interceptors.stream()
                .sorted(Comparator.comparing(Interceptor::order))
                .collect(Collectors.toList());
    }

    public void before(MethodInfo methodInfo, Context ctx) {
        ContextUtils.setBeginTime(ctx);
        doBefore(methodInfo, ctx, 0);
    }

    private void doBefore(MethodInfo methodInfo, Context ctx, int pos) {
        if (pos == interceptors.size()) {
            return;
        }
        Interceptor interceptor = interceptors.get(pos);
        try {
            interceptor.before(methodInfo, ctx);
        } catch (Throwable e) {
            System.err.println("[agent] interceptor before error: " + e.getMessage());
            e.printStackTrace();
        }
        doBefore(methodInfo, ctx, pos + 1);
    }

    public void after(MethodInfo methodInfo, Context ctx) {
        ContextUtils.setEndTime(ctx);
        doAfter(methodInfo, ctx, interceptors.size() - 1);
    }

    private void doAfter(MethodInfo methodInfo, Context ctx, int pos) {
        if (pos < 0) {
            return;
        }
        Interceptor interceptor = interceptors.get(pos);
        try {
            interceptor.after(methodInfo, ctx);
        } catch (Throwable e) {
            System.err.println("[agent] interceptor after error: " + e.getMessage() + " pos: " + pos + " interceptors size: " + interceptors.size());
            e.printStackTrace();
        }
        doAfter(methodInfo, ctx, pos - 1);
    }
}
