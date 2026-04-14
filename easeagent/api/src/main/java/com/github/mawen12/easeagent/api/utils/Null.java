package com.github.mawen12.easeagent.api.utils;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

@SharedToBootstrap
public class Null {

    public static <V> V of(Object o) {
        return o == null ? null : (V) o;
    }

    public static <V> V of(V val, V defaultVal) {
        return val == null ? defaultVal : val;
    }
}
