package com.github.mawen12.easeagent.api.context;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.trace.Request;
import com.github.mawen12.easeagent.api.trace.Setter;
import com.github.mawen12.easeagent.api.trace.Span;

@SharedToBootstrap("used by CommonInlineAdvice")
public interface Context {

    boolean isNoop();

    <V> V get(Object key);

    <V> V remove(Object key);

    <V> V put(Object key, V value);

    int enter(Object key);

    int exit(Object key);

    default boolean enter(Object key, int times) {
        return enter(key) == times;
    }

    default boolean exit(Object key, int times) {
        return exit(key) == times;
    }

    //-------------------------------------------

    Span currentSpan();

    Span nextSpan();

    Setter.RequestContext clientRequest(Request request);

    Setter.RequestContext serverReceive(Request request);

    enum NoOp implements Context {
        INSTANCE;

        @Override
        public boolean isNoop() {
            return true;
        }

        @Override
        public <V> V get(Object key) {
            return null;
        }

        @Override
        public <V> V remove(Object key) {
            return null;
        }

        @Override
        public <V> V put(Object key, V value) {
            return null;
        }

        @Override
        public int enter(Object key) {
            return 0;
        }

        @Override
        public int exit(Object key) {
            return 0;
        }

        @Override
        public Span currentSpan() {
            return Span.NOOP;
        }

        @Override
        public Span nextSpan() {
            return Span.NOOP;
        }

        @Override
        public Setter.RequestContext clientRequest(Request request) {
            return Setter.RequestContext.NOOP;
        }

        @Override
        public Setter.RequestContext serverReceive(Request request) {
            return Setter.RequestContext.NOOP;
        }


    }
}
