package com.github.mawen12.model;

import com.github.mawen12.utils.Json;

import java.util.List;

public class JThreads implements Jsonable {

    int Count;

    List<JThread> Threads;

    @Override
    public String toJson() {
        return Json.toJson("Count", Count, "Threads", Threads);
    }
}
