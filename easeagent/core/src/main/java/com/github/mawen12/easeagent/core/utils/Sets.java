package com.github.mawen12.easeagent.core.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Sets {

    public static <T> Set<T> of(T... ts) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, ts);
        return set;
    }
}
