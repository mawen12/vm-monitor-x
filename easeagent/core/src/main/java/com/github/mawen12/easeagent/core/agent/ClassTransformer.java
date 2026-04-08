package com.github.mawen12.easeagent.core.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Set;

public interface ClassTransformer {

    ElementMatcher<ClassLoader> getClassLoaderMatcher();

    ElementMatcher.Junction<TypeDescription> getClassMatcher();

    Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers();

    AgentBuilder build(AgentBuilder builder);
}
