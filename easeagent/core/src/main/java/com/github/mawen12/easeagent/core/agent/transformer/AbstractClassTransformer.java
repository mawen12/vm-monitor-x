package com.github.mawen12.easeagent.core.agent.transformer;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.api.interceptor.InterceptorChain;
import com.github.mawen12.easeagent.api.interceptor.InterceptorChainRouter;
import com.github.mawen12.easeagent.core.agent.spi.ClassTransformer;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@EaseAgentClassLoader
public abstract class AbstractClassTransformer implements ClassTransformer, AgentBuilder.Transformer {

    protected abstract String getAdviceKey();

    protected abstract List<Interceptor> getInterceptors();

    @Override
    public AgentBuilder build(AgentBuilder builder) {
        // sort by order
        this.getInterceptors().sort(Comparator.comparing(i -> i.order().getOrder()));
        // register chain
        InterceptorChainRouter.INSTANCE.add(this.getAdviceKey(), new InterceptorChain(this.getInterceptors()));
        // register byte buddy
        return builder.type(this.getClassMatcher(), this.getClassLoaderMatcher()).transform(this);
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        String adviceKey = getAdviceKey();

        // build transformers
        List<AgentBuilder.Transformer> transformers = this.getMethodMatchers()
                .stream()
                .map(methodMatcher -> new ForAdviceTransformer(methodMatcher, adviceKey))
                .collect(Collectors.toList());

        if (this.addDynamicField()) {
            transformers.add(new DynamicFieldTransformer());
        }
//        if (this.getTypeFieldName() != null && this.getTypeFieldName() != "") {
//            transformers.add();
//        }

        // init interceptors
        List<Interceptor> interceptors = getInterceptors();
        for (Interceptor interceptor : interceptors) {
            interceptor.init();
        }

        // transform
        for (AgentBuilder.Transformer transformer : transformers) {
            builder = transformer.transform(builder, typeDescription, classLoader, module, protectionDomain);
        }

        return builder;
    }
}
