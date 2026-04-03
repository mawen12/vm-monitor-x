package com.github.mawen12.easeagent.core.utils;

public class Null {

    public static <V> V of(Object o) {
        return o == null ? null : (V) o;
    }
}
