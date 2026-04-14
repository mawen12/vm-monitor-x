package com.github.mawen12.easeagent.core.context;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.config.ContextManager;

@EaseAgentClassLoader("used by Bootstrap, then export to Agent#contextManager")
public class ContextManagerImpl implements ContextManager {

    private static final ThreadLocal<SessionContext> LOCAL_SESSION_CONTEXT = ThreadLocal.withInitial(SessionContext::new);

    public static ContextManager build() {
        return new ContextManagerImpl();
    }

    @Override
    public Context getContext() {
        return LOCAL_SESSION_CONTEXT.get();
    }
}
