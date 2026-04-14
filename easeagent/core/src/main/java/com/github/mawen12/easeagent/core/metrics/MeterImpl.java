package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.Meter;

@EaseAgentClassLoader
public class MeterImpl implements Meter {

    private final com.codahale.metrics.Meter meter;

    private MeterImpl(com.codahale.metrics.Meter meter) {
        this.meter = meter;
    }

    public static Meter build(com.codahale.metrics.Meter meter) {
        return meter == null ? Meter.NOOP : new MeterImpl(meter);
    }

    @Override
    public void mark() {
        meter.mark();
    }

    @Override
    public double getOneMinuteRate() {
        return meter.getOneMinuteRate();
    }

    @Override
    public double getFiveMinuteRate() {
        return meter.getFiveMinuteRate();
    }

    @Override
    public double getFifteenMinuteRate() {
        return meter.getFifteenMinuteRate();
    }

    @Override
    public double getMeanRate() {
        return meter.getMeanRate();
    }

    @Override
    public long getCount() {
        return meter.getCount();
    }
}
