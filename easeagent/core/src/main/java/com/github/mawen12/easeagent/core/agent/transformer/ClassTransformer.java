package com.github.mawen12.easeagent.core.agent.transformer;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Set;

public interface ClassTransformer {

    default ElementMatcher<ClassLoader> getClassLoaderMatcher() {
        return ElementMatchers.any();
    }

    ElementMatcher.Junction<TypeDescription> getClassMatcher();

    Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers();

    AgentBuilder build(AgentBuilder builder);

    default boolean addDynamicField() {
        return false;
    }

//    default String getTypeFieldName() {
//        return null;
//    }
}
