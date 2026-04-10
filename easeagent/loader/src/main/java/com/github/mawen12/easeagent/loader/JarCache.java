package com.github.mawen12.easeagent.loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class JarCache {
    private final JarFile jarFile;
    private final Map<String, JarFile> childJars;
    private final Map<String, URL> childUrls;

    static JarCache build(File archiveFile) throws IOException {
        JarFile jarFile = new JarFile(archiveFile);
        String tmpDir = generateTmpDir(jarFile);

        Map<String, JarFile> childJars = new HashMap<>();
        Map<String, URL> childUrls = new HashMap<>();
        jarFile.stream().forEach(jarEntry -> {
            String name = jarEntry.getName();
            if (!jarEntry.isDirectory() && name.endsWith(".jar")) {
                try (InputStream input = jarFile.getInputStream(jarEntry)){
                    File output = createTempJarFile(tmpDir, input, name);
                    JarFile childJarFile = new JarFile(output);

                    childJars.put(name, childJarFile);
                    childUrls.put(name, output.toURI().toURL());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return new JarCache(jarFile, childJars, childUrls);
    }

    public JarCache(JarFile jarFile, Map<String, JarFile> childJars, Map<String, URL> childUrls) {
        this.jarFile = jarFile;
        this.childJars = childJars;
        this.childUrls = childUrls;
    }

    public List<URL> nestedJarUrls(String prefix) {
        return childUrls.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<JarFile> nestedJarFiles(String prefix) {
        return childJars.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Manifest getManifest() throws Exception {
        return jarFile.getManifest();
    }

    static String generateTmpDir(JarFile jarFile) throws IOException {
        String tmp = System.getProperty("java.io.tmpdir");
        Random random = new Random();
        String dirName = String.format("easeagent-%s-%d", getAttribute(jarFile, "Agent-Version"), Math.abs(random.nextLong()));
        if (tmp != null && !tmp.endsWith(File.separator)) {
            tmp += File.separator;
        }
        return tmp + dirName + File.separator;
    }

    static String getAttribute(JarFile jarFile, String key) throws IOException {
        return jarFile.getManifest().getMainAttributes().getValue(key);
    }

    static File createTempJarFile(String tmpDir, InputStream input, String outputName) throws IOException {
        File dir;
        String fName = new File(outputName).getName();
        if (fName.length() < outputName.length()) {
            String localDir = outputName.substring(0, outputName.length() - fName.length());
            Path path = Paths.get(tmpDir, localDir);
            dir = Files.createDirectories(path).toFile();
        } else {
            dir = new File(tmpDir);
        }

        File f = new File(dir, fName);
        f.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(f)) {
            int n;
            byte[] buffer = new byte[4096];
            while (-1 != (n = input.read(buffer))) {
                fos.write(buffer, 0, n);
            }
        }
        return f;
    }

}
