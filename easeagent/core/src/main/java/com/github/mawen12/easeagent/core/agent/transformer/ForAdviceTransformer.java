package com.github.mawen12.easeagent.core.agent.transformer;

import com.github.mawen12.easeagent.core.agent.AdviceKeyOffsetMapping;
import com.github.mawen12.easeagent.core.agent.CompoundClassLoader;
import com.github.mawen12.easeagent.core.agent.advice.CommonInlineAdvice;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class ForAdviceTransformer implements AgentBuilder.Transformer {

    private final ForAdvice advice;

    public ForAdviceTransformer(ElementMatcher.Junction<MethodDescription> methodMatcher, String adviceKey) {
        AdviceKeyOffsetMapping.Factory factory = new AdviceKeyOffsetMapping.Factory(adviceKey);
        Advice.WithCustomMapping customMapping = Advice.withCustomMapping().bind(factory);

        this.advice = new ForAdvice(customMapping)
                .include(getClass().getClassLoader())
                .advice(methodMatcher, CommonInlineAdvice.class.getCanonicalName());
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        // 修复 easeagent class loader 无法找到 api 类
        CompoundClassLoader.compound(this.getClass().getClassLoader(), classLoader);
        return advice.transform(builder, typeDescription, classLoader, module, protectionDomain);
    }
}
