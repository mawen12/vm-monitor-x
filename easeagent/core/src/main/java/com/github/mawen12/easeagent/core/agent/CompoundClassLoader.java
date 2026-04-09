package com.github.mawen12.easeagent.core.agent;

public class CompoundClassLoader {

    public static ClassLoader compound(ClassLoader easeAgentClassLoader, ClassLoader other) {
        if (other == null) {
            return easeAgentClassLoader;
        }

        try {
            easeAgentClassLoader.getClass().getDeclaredMethod("add", ClassLoader.class).invoke(easeAgentClassLoader, other);
        } catch (Exception e) {
            System.err.println("this may be bug if it was running in production " + e.getMessage());
        }
        return easeAgentClassLoader;
    }
}
