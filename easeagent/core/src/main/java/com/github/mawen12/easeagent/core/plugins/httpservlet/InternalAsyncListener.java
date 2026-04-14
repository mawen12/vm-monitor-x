package com.github.mawen12.easeagent.core.plugins.httpservlet;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;
import java.util.function.Consumer;

@EaseAgentClassLoader
public class InternalAsyncListener implements AsyncListener {

    private final Consumer<AsyncEvent> consumer;

    public InternalAsyncListener(Consumer<AsyncEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        this.consumer.accept(event);
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {

    }

    @Override
    public void onError(AsyncEvent event) throws IOException {

    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        AsyncContext asyncContext = event.getAsyncContext();
        if (asyncContext != null) {
            asyncContext.addListener(this, event.getSuppliedRequest(), event.getSuppliedResponse());
        }
    }
}
