package com.github.mawen12.easeagent.api.trace;

import java.io.Closeable;

public interface Scope extends Closeable {
    Scope NOOP = NoOp.INSTANCE;

    void close();

    enum NoOp implements Scope {
        INSTANCE;

        @Override
        public void close() {

        }
    }
}