package com.github.mawen12.easeagent.api.context;

public class NoopContext implements Context {
    public static NoopContext INSTANCE = new NoopContext();

    @Override
    public boolean isNoop() {
        return true;
    }

    @Override
    public <V> V get(Object key) {
        return null;
    }

    @Override
    public <V> V remove(Object key) {
        return null;
    }

    @Override
    public <V> V put(Object key, V value) {
        return null;
    }

    @Override
    public int enter(Object key) {
        return 0;
    }

    @Override
    public int exit(Object key) {
        return 0;
    }
}
