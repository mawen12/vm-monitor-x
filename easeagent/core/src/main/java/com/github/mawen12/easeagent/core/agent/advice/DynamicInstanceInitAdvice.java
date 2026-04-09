package com.github.mawen12.easeagent.core.agent.advice;

import com.github.mawen12.easeagent.core.agent.transformer.DynamicFieldTransformer;
import net.bytebuddy.asm.Advice;

public class DynamicInstanceInitAdvice {

    @Advice.OnMethodExit
    public static void exit(@Advice.This(optional = true) Object target) {
        if (target instanceof DynamicFieldTransformer.DynamicFieldAccessor) {
            DynamicFieldTransformer.DynamicFieldAccessor accessor = (DynamicFieldTransformer.DynamicFieldAccessor) target;
            if (accessor.getAgent$$Field$$Data() == null) {
                accessor.setAgent$$Field$$Data(DynamicFieldTransformer.DynamicFieldAccessor.NULL);
            }
        }
    }
}
