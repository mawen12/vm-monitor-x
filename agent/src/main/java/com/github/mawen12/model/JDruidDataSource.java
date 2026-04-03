package com.github.mawen12.model;

import com.github.mawen12.utils.Json;

public class JDruidDataSource implements Jsonable {
    public int ActiveCount;

    public int PoolingCount;

    public int MaxActive;

    public int WaitThreadCount;

    @Override
    public String toJson() {
        return Json.toJson("ActiveCount", ActiveCount, "PoolingCount", PoolingCount, "MaxActive", MaxActive, "WaitThreadCount", WaitThreadCount);
    }
}
