package com.github.mawen12.easeagent.core.plugins.springboot.transformer;

import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.api.utils.Sets;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.agent.transformer.ClassTransformer;
import com.github.mawen12.easeagent.core.plugins.springboot.state.SpringBootReadyInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(ClassTransformer.class)
public class SpringBootReadyTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return "Spring-Boot-Ready";
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(SpringBootReadyInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("org.springframework.context.ApplicationEventPublisher"));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Sets.of(
                named("publishEvent").and(takesArguments(1))
        );
    }
}
