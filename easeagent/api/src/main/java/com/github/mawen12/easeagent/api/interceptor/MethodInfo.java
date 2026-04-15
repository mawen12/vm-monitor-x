package com.github.mawen12.easeagent.api.interceptor;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@SharedToBootstrap("used by CommonInlineAdvice")
public class MethodInfo {
    private final Object invoker;
    private final String type;
    private final String method;
    private final Object[] args;
    @Setter
    private Throwable throwable;
    @Setter
    private Object retValue;

    private boolean changed;

    public boolean isSuccess() {
        return this.throwable == null;
    }

    public void changeArg(int index, Object arg) {
        this.args[index] = arg;
        this.changed = true;
    }
}
