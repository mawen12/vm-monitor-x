package com.github.mawen12.easeagent.core.agent.transformer;

import com.github.mawen12.easeagent.core.Bootstrap;
import com.github.mawen12.easeagent.core.agent.advice.DynamicInstanceInitAdvice;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class DynamicFieldTransformer implements AgentBuilder.Transformer {

    private static final String FIELD_NAME = "agent$$Field$$Data";

    private final AgentBuilder.Transformer.ForAdvice transformer;

    public DynamicFieldTransformer() {
        this.transformer = new AgentBuilder.Transformer.ForAdvice()
                .include(getClass().getClassLoader())
                .advice(MethodDescription::isConstructor, DynamicInstanceInitAdvice.class.getCanonicalName());
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(Bootstrap.LOADER);

        try {
            builder.defineField(FIELD_NAME, Object.class, Opcodes.ACC_PRIVATE)
                    .implement(DynamicFieldAccessor.class)
                    .intercept(FieldAccessor.ofField(FIELD_NAME));
            return transformer.transform(builder, typeDescription, classLoader, module, protectionDomain);
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    public interface DynamicFieldAccessor {
        Object NULL = new Object();

        void setAgent$$Field$$Data(Object data);

        Object getAgent$$Field$$Data();
    }


}
