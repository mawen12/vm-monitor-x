package com.github.mawen12.model;

import com.github.mawen12.utils.Json;

public class JThread implements Jsonable{

    long Id;

    String Name;

    Thread.State State;

    long CpuTime;

    long prevCpuTime;

    @Override
    public String toJson() {
        return Json.toJson("Id", Id, "Name", Name, "State", State, "CpuTime", CpuTime);
    }
}
