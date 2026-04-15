package com.github.mawen12.easeagent.core.plugins.kafka.common;

import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import lombok.Getter;
import org.apache.kafka.clients.producer.Callback;


public abstract class AsyncCallback implements Callback {
    protected final Callback delegate;
    @Getter
    private final boolean async;

    public AsyncCallback(Callback delegate) {
        this.delegate = delegate;
        this.async = isAsync(delegate);
    }

    public static Callback callback(MethodInfo methodInfo) {
        Object arg1 = methodInfo.getArgs()[1];
        return arg1 != null ? (Callback) arg1 : null;
    }

    public static boolean isAsync(Callback callback) {
        if (callback == null) {
            return false;
        }
        if (callback instanceof AsyncCallback) {
            return ((AsyncCallback) callback).isAsync();
        }
        return true;
    }
}
