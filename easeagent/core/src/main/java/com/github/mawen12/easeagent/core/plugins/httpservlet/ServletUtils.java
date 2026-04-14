package com.github.mawen12.easeagent.core.plugins.httpservlet;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.core.utils.ClassUtils;

import javax.servlet.http.HttpServletRequest;

@EaseAgentClassLoader
public class ServletUtils {
    public static final String START_TIME = ServletUtils.class.getName() + "$StartTime";
    public static final String BEST_MATCHING_PATTERN_ATTRIBUTE;

    static {
        String pattern = null;
        Object field = ClassUtils.getStaticField("org.springframework.web.servlet.HandlerMapping", "BEST_MATCHING_PATTERN_ATTRIBUTE");
        if (field == null || !(field instanceof String)) {
            pattern = "org.springframework.web.servlet.HandlerMapping.bestMatchingPattern";
        } else {
            pattern = (String) field;
        }
        BEST_MATCHING_PATTERN_ATTRIBUTE = pattern;
    }

    public static long startTime(HttpServletRequest request) {
        Object startObj = request.getAttribute(START_TIME);
        Long start = null;
        if (startObj == null) {
            start = System.currentTimeMillis();
            request.setAttribute(START_TIME, start);
        } else {
            start = (Long) startObj;
        }
        return start;
    }

    public static boolean markProcessed(HttpServletRequest request, String mark) {
        if (request.getAttribute(mark) != null) {
            return true;
        }
        request.setAttribute(mark, "m");
        return false;
    }

    public static String getHttpRoute(HttpServletRequest request) {
        Object httpRoute = request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        return httpRoute != null ? httpRoute.toString() : "";
    }
}
