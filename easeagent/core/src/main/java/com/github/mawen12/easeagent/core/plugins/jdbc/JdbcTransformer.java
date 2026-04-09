package com.github.mawen12.easeagent.core.plugins.jdbc;

import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.plugins.jdbc.metric.JdbcDataSourceMetricInterceptor;
import com.github.mawen12.easeagent.core.utils.Lists;
import com.github.mawen12.easeagent.core.utils.Sets;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class JdbcTransformer extends AbstractClassTransformer {

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("javax.sql.DataSource"));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        ElementMatcher.Junction<MethodDescription> methodMatcher = named("getConnection")
                .and(returns(hasSuperType(named("java.sql.Connection"))));
        return Sets.of(methodMatcher);
    }

    @Override
    protected String getAdviceKey() {
        return "Jdbc";
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(JdbcDataSourceMetricInterceptor.INSTANCE);
    }
}
