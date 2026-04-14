package com.github.mawen12.easeagent.api.interceptor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class MethodInfo {
    private final Object invoker;
    private final String type;
    private final String method;
    private final Object[] args;
    @Setter
    private Throwable throwable;
    @Setter
    private Object retValue;
}
