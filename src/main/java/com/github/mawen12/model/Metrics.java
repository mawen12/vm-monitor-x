package com.github.mawen12.model;

import com.github.mawen12.utils.Json;

public class Metrics implements Jsonable {

    public JMemory Memory;

    public double Load;

    public JThreads Threads;

    public long Time;

    @Override
    public String toJson() {
        return Json.toJson("Memory", Memory, "Load", Load, "Threads", Threads, "Time", Time);
    }
}
