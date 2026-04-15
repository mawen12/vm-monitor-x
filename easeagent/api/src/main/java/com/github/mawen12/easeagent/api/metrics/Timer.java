package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@SharedToBootstrap
public interface Timer extends Snapshot, Metric {
    Timer NOOP = NoOp.INSTANCE;

    void update(long duration, TimeUnit unit);

    default void updateMs(long durationMs) {
        this.update(durationMs, TimeUnit.MILLISECONDS);
    }

    void update(Duration duration);

    enum NoOp implements Timer, Snapshot.Wrapper {
        INSTANCE;

        @Override
        public void update(long duration, TimeUnit unit) {

        }

        @Override
        public void update(Duration duration) {
        }

        @Override
        public Snapshot unwrap() {
            return Snapshot.NOOP;
        }
    }
}
