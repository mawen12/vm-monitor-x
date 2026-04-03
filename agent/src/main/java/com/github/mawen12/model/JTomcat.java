package com.github.mawen12.model;

import com.github.mawen12.utils.Json;

public class JTomcat implements Jsonable{

    public int CurrentThreadsBusy;

    public int CurrentThreadCount;

    public int MaxThreads;

    @Override
    public String toJson() {
        return Json.toJson("CurrentThreadsBusy", CurrentThreadsBusy, "CurrentThreadCount", CurrentThreadCount, "MaxThreads", MaxThreads);
    }
}
