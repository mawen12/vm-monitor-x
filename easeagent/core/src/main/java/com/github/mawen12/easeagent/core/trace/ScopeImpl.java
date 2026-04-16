package com.github.mawen12.easeagent.core.trace;

import brave.propagation.CurrentTraceContext;
import com.github.mawen12.easeagent.api.trace.Scope;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScopeImpl implements Scope {
    private final CurrentTraceContext.Scope scope;

    @Override
    public void close() {
        scope.close();
    }
}
