package com.github.mawen12.easeagent.api.utils;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@SharedToBootstrap
public class ScheduleHelper {
    public static final ScheduleHelper DEFAULT = new ScheduleHelper();

    private final ThreadFactory threadFactory = new AgentThreadFactory();

    public void execute(int initDelay, int delay, Runnable command) {
        Executors.newSingleThreadScheduledExecutor(threadFactory)
                .scheduleWithFixedDelay(command, initDelay, delay, TimeUnit.SECONDS);
    }
}
