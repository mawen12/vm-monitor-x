package com.github.mawen12.easeagent.core.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractClassTransformer implements ClassTransformer, AgentBuilder.Transformer {

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {

        List<AgentBuilder.Transformer> transformers = this.getMethodMatchers().stream().map(methodMatcher -> new ForAdviceTransformer(methodMatcher)).collect(Collectors.toList());

        for (AgentBuilder.Transformer transformer : transformers) {
            builder = transformer.transform(builder, typeDescription, classLoader, module, protectionDomain);
        }

        return builder;
    }
}
