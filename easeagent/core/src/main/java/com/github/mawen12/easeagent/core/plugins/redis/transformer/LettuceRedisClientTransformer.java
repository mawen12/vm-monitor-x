package com.github.mawen12.easeagent.core.plugins.redis.transformer;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@EaseAgentClassLoader
public class LettuceRedisClientTransformer  extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return LettuceRedisClientTransformer.class.getCanonicalName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
//        return Collections.singletonList(LettuceMetricInterceptor.INSTANCE);
        return Collections.emptyList();
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperClass(named("io.lettuce.core.RedisClient"))
                .or(named("io.lettuce.core.RedisClient"));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Collections.singleton(
                named("connectStandaloneAsync").and(isPrivate())
                        .or(named("connectPubSubAsync").and(isPrivate()))
                        .or(named("connectSentinelAsync").and(isPrivate()))
        );
    }
}
