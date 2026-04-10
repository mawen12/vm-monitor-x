package com.github.mawen12.easeagent.core.config;

import com.github.mawen12.easeagent.api.config.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ConfigFactory {

    public static final String CONFIG_PATH_PROPERTY = "agent.config.path";
    public static final String CONFIG_PATH_ENV = "AGENT_CONFIG_PATH";
    public static final String DEFAULT_CONFIG_PATH = "agent.properties";


    public static String getConfigPath() {
        String value = System.getProperty(CONFIG_PATH_PROPERTY);
        if (value != null) {
            return value;
        }

        value = System.getenv(CONFIG_PATH_ENV);
        if (value != null) {
            return value;
        }

        return DEFAULT_CONFIG_PATH;
    }

    public static Config loadConfig(String jarPath, ClassLoader loader) {
        String configPath = getConfigPath();
        try {
            JarFile jarFile = new JarFile(new File(jarPath));
            ZipEntry zipEntry = jarFile.getEntry(configPath);
            if (zipEntry == null) {
                return null;
            }

            try (InputStream in = jarFile.getInputStream(zipEntry)) {
                return loadFromStream(in, configPath);
            }
        } catch (Exception e) {
        }
        return null;
    }

    static Config loadFromStream(InputStream in, String fileName) throws IOException {
        if (in != null) {
            Properties properties = new Properties();
            properties.load(in);

            Map<String, String> map = new HashMap<>();
            for (String key : properties.stringPropertyNames()) {
                map.put(key, properties.getProperty(key));
            }
            return new ConfigImpl(map);
        }
        return null;
    }
}
