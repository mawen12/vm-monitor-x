package com.github.mawen12.easeagent.api.config;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.context.Context;

@SharedToBootstrap
public interface ContextManager {

    Context getContext();
}
