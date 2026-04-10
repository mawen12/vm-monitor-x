package com.github.mawen12.easeagent.core.plugins.demo;

import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.api.utils.Lists;
import com.github.mawen12.easeagent.api.utils.Sets;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.agent.transformer.ClassTransformer;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.named;

@AutoService(ClassTransformer.class)
public class DemoTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return "Demo";
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(DemoInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher<ClassLoader> getClassLoaderMatcher() {
        return any();
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return named("com.demo.HelloWorld");
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Sets.of(named("sayHello"));
    }
}
