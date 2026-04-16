package com.github.mawen12.easeagent.core.plugins.redis.transformer;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.api.utils.Lists;
import com.github.mawen12.easeagent.core.agent.spi.ClassTransformer;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.plugins.redis.metric.LettuceMetricInterceptor;
import com.github.mawen12.easeagent.core.plugins.redis.trace.LettuceTraceInterceptor;
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
public class LettuceChannelWriterTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return LettuceChannelWriterTransformer.class.getCanonicalName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(LettuceMetricInterceptor.INSTANCE, LettuceTraceInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("io.lettuce.core.RedisChannelWriter"))
                .and(not(isInterface()));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Collections.singleton(
                named("write")
                        .and(takesArguments(1))
                        .and(isPublic())
                        .and(not(returns(named("void"))))
        );
    }
}
