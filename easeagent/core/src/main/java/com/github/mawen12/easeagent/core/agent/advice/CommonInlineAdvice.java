package com.github.mawen12.easeagent.core.agent.advice;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.InterceptorChain;
import com.github.mawen12.easeagent.api.interceptor.InterceptorChainRouter;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.core.agent.AdviceKey;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

public class CommonInlineAdvice {

    private static final String CONTEXT = "context";

    @Advice.OnMethodEnter
    public static MethodInfo onEnter(@AdviceKey String key,
                                     @Advice.This(optional = true) Object invoker,
                                     @Advice.Origin("#t") String type,
                                     @Advice.Origin("#m") String method,
                                     @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args,
                                     @Advice.Local(CONTEXT) Context context) {
        context = Agent.getContext();
        if (context.isNoop()) {
            return null;
        }

        MethodInfo methodInfo = new MethodInfo(invoker, type, method, args);

        InterceptorChain chain = InterceptorChainRouter.INSTANCE.resolve(key);
        if (chain != null) {
            chain.before(methodInfo, context);
        }

        return methodInfo;
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void onExit(@AdviceKey String key,
                              @Advice.Enter MethodInfo methodInfo,
                              @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object ret,
                              @Advice.Thrown(readOnly = false, typing = Assigner.Typing.DYNAMIC) Throwable throwable,
                              @Advice.Local(CONTEXT) Context context) {
        if (context.isNoop()) {
            return;
        }

        methodInfo.setRetValue(ret);
        methodInfo.setThrowable(throwable);

        InterceptorChain chain = InterceptorChainRouter.INSTANCE.resolve(key);
        if (chain != null) {
            chain.after(methodInfo, context);
        }
    }
}
