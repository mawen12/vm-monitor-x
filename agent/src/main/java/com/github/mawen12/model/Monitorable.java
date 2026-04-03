package com.github.mawen12.model;

public interface Monitorable {
    Jsonable getAbilities() throws Exception;

    Jsonable getMetrics() throws Exception;
}
