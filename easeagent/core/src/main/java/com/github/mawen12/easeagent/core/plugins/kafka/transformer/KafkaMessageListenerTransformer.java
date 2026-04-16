package com.github.mawen12.easeagent.core.plugins.kafka.transformer;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.core.agent.spi.ClassTransformer;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.plugins.kafka.metric.KafkaMessageListenerMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.hasSuperType;
import static net.bytebuddy.matcher.ElementMatchers.named;

@EaseAgentClassLoader
@AutoService(ClassTransformer.class)
public class KafkaMessageListenerTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return KafkaMessageListenerTransformer.class.getCanonicalName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(KafkaMessageListenerMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("org.springframework.kafka.listener.MessageListener"));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Collections.singleton(named("onMessage"));
    }
}
