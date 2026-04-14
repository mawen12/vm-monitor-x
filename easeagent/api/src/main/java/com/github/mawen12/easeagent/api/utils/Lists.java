package com.github.mawen12.easeagent.api.utils;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SharedToBootstrap
public class Lists {

    public static <T> List<T> of(T... ts) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, ts);
        return list;
    }
}
