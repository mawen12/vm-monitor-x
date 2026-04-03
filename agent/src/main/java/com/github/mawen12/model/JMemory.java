package com.github.mawen12.model;

import com.github.mawen12.utils.Json;

public class JMemory implements Jsonable{
    public long Used;

    public long Max;

    @Override
    public String toJson() {
        return Json.toJson("Used", Used, "Max", Max);
    }
}
