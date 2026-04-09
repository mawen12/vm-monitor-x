package com.github.mawen12.easeagent.core.plugins.jdbc.transformer;

import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.core.agent.transformer.AbstractClassTransformer;
import com.github.mawen12.easeagent.core.plugins.jdbc.JdbcConPrepareOrCreateStmtInterceptor;
import com.github.mawen12.easeagent.api.utils.Lists;
import com.github.mawen12.easeagent.api.utils.Sets;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class JdbcConnectionTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return "Jdbc-Connection";
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(JdbcConPrepareOrCreateStmtInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("java.sql.Connection"));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Sets.of(
                named("createStatement").and(isPublic())
                        .or(named("prepareCall").and(isPublic()))
                        .or(named("prepareStatement").and(isPublic()))
        );
    }
}
