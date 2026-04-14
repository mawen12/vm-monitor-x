package com.github.mawen12.easeagent.core.agent.advice;

import com.github.mawen12.easeagent.api.annotation.Core;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.field.DynamicFieldAccessor;
import net.bytebuddy.asm.Advice;

@Core("the method content in onExit will be inject into the Dest constructor")
@EaseAgentClassLoader
public class DynamicInstanceInitAdvice {

    @Advice.OnMethodExit
    public static void onExit(@Advice.This(optional = true) Object target) {
        if (target instanceof DynamicFieldAccessor) {
            DynamicFieldAccessor accessor = (DynamicFieldAccessor) target;
            if (accessor.getAgent$$Field$$Data() != null) {
                accessor.setAgent$$Field$$Data(null);
            }
        }
    }
}
