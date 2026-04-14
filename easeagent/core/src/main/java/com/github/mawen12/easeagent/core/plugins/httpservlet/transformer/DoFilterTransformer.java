package com.github.mawen12.easeagent.core.plugins.httpservlet.transformer;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.api.utils.Sets;
import com.github.mawen12.easeagent.core.agent.spi.ClassTransformer;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.plugins.httpservlet.metric.DoFilterMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@EaseAgentClassLoader
@AutoService(ClassTransformer.class)
public class DoFilterTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return "Do-Filter";
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(DoFilterMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("javax.servlet.Filter"))
                .or(hasSuperClass(named("javax.servlet.http.HttpServlet")));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Sets.of(
                named("doFilter").and(takesArgument(0, named("javax.servlet.ServletRequest")))
                        .and(takesArgument(1, named("javax.servlet.ServletResponse"))),
                named("service").and(takesArgument(0, named("javax.servlet.ServletRequest")))
                        .and(takesArgument(1, named("javax.servlet.ServletResponse")))
                        .and(isDefaultMethod())
        );
    }
}
