package com.github.mawen12.easeagent.core.context;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.core.utils.Null;

import java.util.HashMap;
import java.util.Map;

public class SessionContext implements Context {

    private final Map<Object, Object> context = new HashMap<>();
    private final Map<Object, Integer> entered = new HashMap<>();

    @Override
    public boolean isNoop() {
        return false;
    }

    @Override
    public <V> V get(Object key) {
        return Null.of(context.get(key));
    }

    @Override
    public <V> V remove(Object key) {
        return Null.of(context.remove(key));
    }

    @Override
    public <V> V put(Object key, V value) {
        context.put(key, value);
        return value;
    }

    @Override
    public int enter(Object key) {
        Integer count = entered.get(key);
        if (count == null) {
            count = 1;
        } else {
            count++;
        }
        entered.put(key, count);
        return count;
    }

    @Override
    public int exit(Object key) {
        Integer count = entered.get(key);
        if (count == null) {
            return 0;
        }
        entered.put(key, count - 1);
        return count;
    }
}
