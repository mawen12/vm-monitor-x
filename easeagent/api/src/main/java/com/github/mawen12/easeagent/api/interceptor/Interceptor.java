package com.github.mawen12.easeagent.api.interceptor;

import com.github.mawen12.easeagent.api.context.Context;

public interface Interceptor {

    default int order() {
        return 0;
    }

    void before(MethodInfo methodInfo, Context ctx);

    void after(MethodInfo methodInfo, Context ctx);
}
