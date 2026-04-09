package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.metrics.Timer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TimerImpl implements Timer {
    private com.codahale.metrics.Timer timer;

    private TimerImpl(com.codahale.metrics.Timer timer) {
        this.timer = timer;
    }

    public static Timer build(com.codahale.metrics.Timer timer) {
        return timer == null ? Timer.NOOP : new TimerImpl(timer);
    }

    @Override
    public void update(long duration, TimeUnit unit) {
        timer.update(duration, unit);
    }

    @Override
    public void update(Duration duration) {
        timer.update(duration);
    }
}
