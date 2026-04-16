package com.github.mawen12.easeagent.core.plugins.redis.trace;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.logging.Logger;
import io.lettuce.core.protocol.RedisCommand;

import java.util.Collection;
import java.util.stream.Collectors;

@EaseAgentClassLoader
public class LettuceTraceInterceptor extends CommonRedisTraceInterceptor{
    public static final LettuceTraceInterceptor INSTANCE = new LettuceTraceInterceptor();

    @Override
    public void doTraceBefore(MethodInfo methodInfo, Context ctx) {
        String cmd = cmd(methodInfo.getArgs()[0]);
        this.startTrace(cmd, null, cmd, ctx);
    }

    private static String cmd(Object arg0) {
        String cmd = null;
        if (arg0 instanceof RedisCommand) {
            RedisCommand<?, ?, ?> redisCommand = (RedisCommand<?, ?, ?>) arg0;
            cmd = redisCommand.getType().name();
        } else if (arg0 instanceof Collection) {
            Collection<RedisCommand<?, ?, ?>> redisCommands = (Collection<RedisCommand<?, ?, ?>>) arg0;
            cmd = "[" + String.join(",", redisCommands.stream().map(input -> input.getType().name()).collect(Collectors.toList()) + "]");
        }

        return cmd;
    }
}
