package com.github.mawen12.model;

import com.github.mawen12.utils.Json;

import java.util.Map;

public class Metrics implements Jsonable {

    public JMemory Memory;

    public double Load;

    public JThreads Threads;

    public Map<String, JDruidDataSource> DataSources;

    public JTomcat Tomcat;

    public long Time;

    @Override
    public String toJson() {
        return Json.toJson("Memory", Memory, "Load", Load, "Threads", Threads, "DataSources", DataSources, "Tomcat", Tomcat, "Time", Time);
    }
}
