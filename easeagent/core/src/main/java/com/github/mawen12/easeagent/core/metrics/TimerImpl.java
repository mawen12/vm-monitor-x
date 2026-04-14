package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.metrics.Snapshot;
import com.github.mawen12.easeagent.api.metrics.Timer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TimerImpl implements Timer, Snapshot.Wrapper {
    private final com.codahale.metrics.Timer timer;
    private final Snapshot snapshot;

    private TimerImpl(com.codahale.metrics.Timer timer) {
        this.timer = timer;
        this.snapshot = SnapshotImpl.build(timer.getSnapshot());
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

    @Override
    public Snapshot unwrap() {
        return snapshot;
    }
}
