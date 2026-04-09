package com.github.mawen12.easeagent.api.metrics;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface Timer extends Metric {
    Timer NOOP = NoOp.INSTANCE;

    void update(long duration, TimeUnit unit);

    void update(Duration duration);

    enum NoOp implements Timer {
        INSTANCE;

        @Override
        public void update(long duration, TimeUnit unit) {

        }

        @Override
        public void update(Duration duration) {

        }
    }
}
