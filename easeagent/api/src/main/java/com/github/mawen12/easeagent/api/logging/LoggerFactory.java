package com.github.mawen12.easeagent.api.logging;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

@SharedToBootstrap
public interface LoggerFactory {
    LoggerFactory NOOP = NoOp.INSTANCE;

    Logger getLogger(String name);

    default Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getCanonicalName());
    }

    enum NoOp implements LoggerFactory {
        INSTANCE;

        @Override
        public Logger getLogger(String name) {
            return new Logger.NoOp(name);
        }
    }
}
