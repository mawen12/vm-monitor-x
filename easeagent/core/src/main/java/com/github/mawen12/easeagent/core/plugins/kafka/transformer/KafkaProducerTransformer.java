package com.github.mawen12.easeagent.core.plugins.kafka.transformer;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.core.agent.spi.ClassTransformer;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.plugins.kafka.metric.KafkaProducerMetricInterceptor;
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
public class KafkaProducerTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return KafkaProducerTransformer.class.getCanonicalName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(KafkaProducerMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return named("org.apache.kafka.clients.producer.KafkaProducer");
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Collections.singleton(
                named("doSend")
                        .and(isPrivate())
                        .and(takesArgument(0, named("org.apache.kafka.clients.producer.ProducerRecord")))
                        .and(takesArgument(1, named("org.apache.kafka.clients.producer.Callback")))
                        .and(returns(named("java.util.concurrent.Future")))
        );
    }
}
