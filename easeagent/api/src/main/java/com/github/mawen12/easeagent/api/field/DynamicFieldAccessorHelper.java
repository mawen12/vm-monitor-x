package com.github.mawen12.easeagent.api.field;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.logging.Logger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SharedToBootstrap("used by Interceptor -> InterceptorChain -> CommonInlineAdvice")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicFieldAccessorHelper {
    private static final Logger LOGGER = Agent.getLogger(DynamicFieldAccessorHelper.class);

    public static <T> T getDynamicFieldValue(Object target) {
        if (target instanceof DynamicFieldAccessor) {
            return (T) ((DynamicFieldAccessor) target).getAgent$$Field$$Data();
        }

        LOGGER.warn("{} must implements DynamicFieldAccessor", target.getClass().getName());
        return null;
    }

    public static void setDynamicFieldValue(Object target, Object value) {
        if (target instanceof DynamicFieldAccessor) {
            ((DynamicFieldAccessor) target).setAgent$$Field$$Data(value);
        }

        LOGGER.warn("{} must implements DynamicFieldAccessor", target.getClass().getName());
    }
}
