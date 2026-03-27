package com.github.mawen12.model;

import com.github.mawen12.utils.Json;

import java.sql.Time;
import java.time.LocalDateTime;

public class Metrics implements Jsonable {

    public long Used;

    public long Max;

    public double Load;

    public JThreads Threads;

    public String Time;

    @Override
    public String toJson() {
        return Json.toJson("Used", Used, "Max", Max, "Load", Load, "Threads", Threads, "Time", Time);
    }
}
