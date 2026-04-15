package com.github.mawen12.easeagent.core.plugins.redis.transformer;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.core.agent.spi.ClassTransformer;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.plugins.redis.metric.JedisMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@SharedToBootstrap
@AutoService(ClassTransformer.class)
public class JedisTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return JedisTransformer.class.getCanonicalName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(JedisMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperClass(named("redis.clients.jedis.BinaryJedis"));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        ElementMatcher.Junction<TypeDescription> overrideFrom =
                named("redis.clients.jedis.commands.JedisCommands").and(isInterface())
                        .or(named("redis.clients.jedis.JedisCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.AdvancedJedisCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.BasicCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.ClusterCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.ModuleCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.MultiKeyCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.ScriptingCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.SentinelCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.BinaryJedisCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.MultiKeyBinaryCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.AdvancedBinaryJedisCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.BinaryScriptingCommands").and(isInterface()));

        return Collections.singleton(
                isOverriddenFrom(overrideFrom)
        );
    }
}
