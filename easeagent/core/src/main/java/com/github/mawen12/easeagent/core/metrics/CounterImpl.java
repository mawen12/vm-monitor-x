package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.Counter;

@EaseAgentClassLoader
public class CounterImpl implements Counter {
    private com.codahale.metrics.Counter counter;

    private CounterImpl(com.codahale.metrics.Counter counter) {
        this.counter = counter;
    }

    public static Counter build(com.codahale.metrics.Counter counter) {
        return counter == null ? Counter.NOOP : new CounterImpl(counter);
    }

    @Override
    public void inc() {
        counter.inc();
    }

    @Override
    public void inc(long n) {
        counter.inc(n);
    }

    @Override
    public long count() {
        return counter.getCount();
    }
}
