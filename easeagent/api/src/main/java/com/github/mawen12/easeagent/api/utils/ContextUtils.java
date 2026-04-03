package com.github.mawen12.easeagent.api.utils;

import com.github.mawen12.easeagent.api.context.Context;

public class ContextUtils {

    private static final String BEGIN_TIME = ContextUtils.class.getSimpleName() + ".beginTime";
    private static final String END_TIME = ContextUtils.class.getSimpleName() + ".endTime";

    public static void setBeginTime(Context context) {
        context.put(BEGIN_TIME, System.currentTimeMillis());
    }

    public static void setEndTime(Context context) {
        context.put(END_TIME, System.currentTimeMillis());
    }
}
