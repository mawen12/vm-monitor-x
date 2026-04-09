package com.github.mawen12.easeagent.api.utils;

import com.github.mawen12.easeagent.api.context.Context;

public class ContextUtils {

    private static final String BEGIN_TIME = ContextUtils.class.getSimpleName() + ".beginTime";
    private static final String END_TIME = ContextUtils.class.getSimpleName() + ".endTime";

    public static void setBeginTime(Context ctx) {
        ctx.put(BEGIN_TIME, System.currentTimeMillis());
    }

    public static void setEndTime(Context ctx) {
        ctx.put(END_TIME, System.currentTimeMillis());
    }

    public static Long getBeginTime(Context ctx) {
        return ctx.get(BEGIN_TIME);
    }

    public static Long getEndTime(Context ctx) {
        return ctx.get(END_TIME);
    }

    public static Long getDuration(Context ctx) {
        return getEndTime(ctx) - getBeginTime(ctx);
    }
}
