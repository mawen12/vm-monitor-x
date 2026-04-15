package com.github.mawen12.easeagent.core.agent.advice;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.Core;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.InterceptorChain;
import com.github.mawen12.easeagent.api.interceptor.InterceptorChainRouter;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.core.agent.AdviceKey;
import com.github.mawen12.easeagent.core.context.SessionContext;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.util.concurrent.ThreadFactory;

@Core("the method content in onEnter and onExit will be inject into the Dest class, and execute the method content is AppClassLoader")
@EaseAgentClassLoader
public class CommonInlineAdvice {

    private static final String CONTEXT = "context";

    @Advice.OnMethodEnter
    public static MethodInfo onEnter(@AdviceKey String key,
                                     @Advice.This(optional = true) Object invoker,
                                     @Advice.Origin("#t") String type,
                                     @Advice.Origin("#m") String method,
                                     @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args,
                                     @Advice.Local(CONTEXT) Context context) {
        // actual is SessionContext, it is created by ContextManagerImpl#getContext
        context = Agent.getContext();
        if (context.isNoop()) {
            return null;
        }

        MethodInfo methodInfo = new MethodInfo(invoker, type, method, args);

        InterceptorChain chain = InterceptorChainRouter.INSTANCE.resolve(key);
        if (chain != null) {
            chain.before(methodInfo, context);
        }

        if (methodInfo.isChanged()) {
            args = methodInfo.getArgs();
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
