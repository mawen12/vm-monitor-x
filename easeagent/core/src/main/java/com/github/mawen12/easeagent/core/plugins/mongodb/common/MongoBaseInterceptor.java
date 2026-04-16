package com.github.mawen12.easeagent.core.plugins.mongodb.common;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;

@EaseAgentClassLoader
public abstract class MongoBaseInterceptor implements NonReentrantInterceptor {

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {

    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {

    }
}
