package com.github.mawen12.easeagent.loader;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.jar.JarFile;

public class Main {

    public static void premain(String args, Instrumentation inst) throws Exception {
        // 1. read jar path
        File jar = getArchiveFile();
        JarCache jarCache = JarCache.build(jar);

        // 2. build easeagent classloader from lib/
        List<URL> urls = jarCache.nestedJarUrls("/lib");
        ClassLoader loader = new EaseAgentClassLoader(urls.toArray(new URL[0]));

        // 3. register boot/ to bootstrap classloader
        List<JarFile> jarFiles = jarCache.nestedJarFiles("boot/");
        jarFiles.forEach(inst::appendToBootstrapClassLoaderSearch);

        // 4. init agent
        String bootstrap = jarCache.getManifest().getMainAttributes().getValue("Bootstrap-Class");
        switchLoader(loader, () -> {
            loader.loadClass(bootstrap).getMethod("premain", String.class, Instrumentation.class, String.class).invoke(null, args, inst, jar.getPath());
            return null;
        });
    }

    private static File getArchiveFile() throws URISyntaxException {
        final ProtectionDomain protectionDomain = Main.class.getProtectionDomain();
        final CodeSource codeSource = protectionDomain.getCodeSource();
        final URI location = codeSource == null ? null : codeSource.getLocation().toURI();
        final String path = location == null ? null : location.getSchemeSpecificPart();

        if (path == null) {
            throw new IllegalStateException("Unable to determine code source archive");
        }

        final File root = new File(path);
        if (!root.exists() || root.isDirectory()) {
            throw new IllegalStateException("Unable to determine code source archive from " + path);
        }
        return root;
    }

    private static void switchLoader(ClassLoader loader, Callable<Void> callback) throws Exception {
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            callback.call();
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }
}
