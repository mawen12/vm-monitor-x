package com.github.mawen12.easeagent.core.trace;

import brave.baggage.CorrelationScopeDecorator;
import brave.internal.CorrelationContext;
import brave.propagation.CurrentTraceContext;
import org.apache.logging.log4j.ThreadContext;

public class AgentMDCScopeDecorator {

    public static final CurrentTraceContext.ScopeDecorator INSTANCE = new AgentLoggingScopeDecorator().build();

    static class AgentLoggingScopeDecorator extends CorrelationScopeDecorator.Builder {
        protected AgentLoggingScopeDecorator() {
            super(AgentLoggingMDCContext.INSTANCE);
        }
    }

    enum AgentLoggingMDCContext implements CorrelationContext {
        INSTANCE;

        @Override
        public String getValue(String name) {
            return ThreadContext.get(name);
        }

        @Override
        public boolean update(String name, String value) {
            if (value != null) {
                ThreadContext.put(name, value);
            } else {
                ThreadContext.remove(name);
            }
            return true;
        }
    }
}
