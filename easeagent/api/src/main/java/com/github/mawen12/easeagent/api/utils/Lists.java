package com.github.mawen12.easeagent.api.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lists {

    public static <T> List<T> of(T... ts) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, ts);
        return list;
    }
}
