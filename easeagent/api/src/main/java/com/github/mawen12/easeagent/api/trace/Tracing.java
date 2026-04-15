package com.github.mawen12.easeagent.api.trace;

public interface Tracing {
    Tracing NOOP = NoOp.INSTANCE;

    boolean isNoop();

    boolean hasCurrentSpan();

    Span currentSpan();

    Span nextSpan();

    enum NoOp implements Tracing {
        INSTANCE;

        @Override
        public boolean isNoop() {
            return true;
        }

        @Override
        public boolean hasCurrentSpan() {
            return false;
        }

        @Override
        public Span currentSpan() {
            return Span.NOOP;
        }

        @Override
        public Span nextSpan() {
            return Span.NOOP;
        }
    }

    interface Supplier {
        Tracing get();
    }
}
