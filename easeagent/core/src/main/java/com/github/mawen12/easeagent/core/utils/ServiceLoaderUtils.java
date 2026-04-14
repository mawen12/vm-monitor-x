package com.github.mawen12.easeagent.core.utils;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@EaseAgentClassLoader("used by Bootstrap")
public class ServiceLoaderUtils {

    public static <T> List<T> load(Class<T> serviceClass) {
        List<T> result = new ArrayList<>();
        ServiceLoader<T> services = ServiceLoader.load(serviceClass);
        for (T service : services) {
            result.add(service);
        }
        return result;
    }
}
