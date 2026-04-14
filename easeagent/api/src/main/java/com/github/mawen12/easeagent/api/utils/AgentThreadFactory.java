package com.github.mawen12.easeagent.api.utils;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@SharedToBootstrap
public class AgentThreadFactory implements ThreadFactory {
    protected AtomicInteger counter = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, "Agent-" + counter.incrementAndGet());
        thread.setDaemon(true);
        return thread;
    }
}
