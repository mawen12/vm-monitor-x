package com.github.mawen12.easeagent.core.plugins.jdbc.transformer;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.api.utils.Lists;
import com.github.mawen12.easeagent.api.utils.Sets;
import com.github.mawen12.easeagent.core.agent.spi.ClassTransformer;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.plugins.jdbc.JdbcStmtPrepareInterceptor;
import com.github.mawen12.easeagent.core.plugins.jdbc.metric.JdbcStmtMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@EaseAgentClassLoader
@AutoService(ClassTransformer.class)
public class JdbcStatementTransformer extends AbstractClassTransformer {

    @Override
    public boolean addDynamicField() {
        return true;
    }

    @Override
    protected String getAdviceKey() {
        return "Jdbc-Statement";
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(JdbcStmtPrepareInterceptor.INSTANCE, JdbcStmtMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("java.sql.Statement"))
                .and(not(isAbstract()))
                .and(not(isInterface()));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        ElementMatcher.Junction<NamedElement> overrideFrom = named("java.sql.Statement").or(named("java.sql.PreparedStatement"));

        return Sets.of(
                nameStartsWith("execute")
                        .and(isOverriddenFrom(overrideFrom)),
                named("addBatch")
                        .or(named("clearBatch"))
        );
    }
}
