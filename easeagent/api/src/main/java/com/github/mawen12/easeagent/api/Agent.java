package com.github.mawen12.easeagent.api;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.manager.ContextManager;

public class Agent {

    public static ContextManager contextManager = () -> Context.NoOp.INSTANCE;

    public static Context getContext() {
        return contextManager.getContext();
    }
}
