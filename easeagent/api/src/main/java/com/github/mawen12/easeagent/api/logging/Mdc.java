package com.github.mawen12.easeagent.api.logging;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

@SharedToBootstrap
public interface Mdc {
    Mdc NOOP = NoOp.INSTANCE;

    void put(String key, String value);

    void remove(String key);

    String get(String key);

    enum NoOp implements Mdc {
        INSTANCE;

        @Override
        public void put(String key, String value) {

        }

        @Override
        public void remove(String key) {

        }

        @Override
        public String get(String key) {
            return "";
        }
    }
}
