package com.github.mawen12.easeagent.loader;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EaseAgentClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    private final Set<WeakReference<ClassLoader>> externals = new HashSet<>();

    public EaseAgentClassLoader(URL[] urls) {
        super(urls, null);
    }

    public void add(ClassLoader cl) {
        if (cl != null && !Objects.equals(cl, this)) {
            externals.add(new WeakReference<>(cl));
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            for (WeakReference<ClassLoader> external : externals) {
                try {
                    ClassLoader cl = external.get();
                    if (cl == null) {
                        continue;
                    }

                    Class<?> aClass = cl.loadClass(name);
                    if (resolve) {
                        resolveClass(aClass);
                    }
                    return aClass;
                } catch (ClassNotFoundException ignored) {

                }
            }

            throw e;
        }
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url == null) {
            for (WeakReference<ClassLoader> external : externals) {
                try {
                    ClassLoader cl = external.get();
                    if (cl == null) {
                        continue;
                    }

                    url = cl.getResource(name);
                    if (url != null) {
                        break;
                    }
                } catch (Exception ignored) {

                }
            }
        }
        return url;
    }
}
