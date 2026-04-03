package com.github.mawen12.easeagent.api.context;

public interface Context {

    boolean isNoop();

    <V> V get(Object key);

    <V> V remove(Object key);

    <V> V put(Object key, V value);

    int enter(Object key);

    int exit(Object key);

    default boolean enter(Object key, int times) {
        return enter(key) == times;
    }

    default boolean exit(Object key, int times) {
        return exit(key) == times;
    }
}
