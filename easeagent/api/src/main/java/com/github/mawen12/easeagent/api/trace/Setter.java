package com.github.mawen12.easeagent.api.trace;

import java.util.Collections;
import java.util.Map;

public interface Setter {

    void setHeader(String name, String value);

    interface RequestContext extends Setter {
        RequestContext NOOP = NoOp.INSTANCE;

        boolean isNoop();

        Span span();

        Scope scope();

        Map<String, String> getHeaders();

        void finish(Getter.Response response);

        enum NoOp implements RequestContext {
            INSTANCE;

            @Override
            public boolean isNoop() {
                return true;
            }

            @Override
            public Span span() {
                return Span.NOOP;
            }

            @Override
            public Scope scope() {
                return Scope.NOOP;
            }

            @Override
            public void setHeader(String name, String value) {

            }

            @Override
            public Map<String, String> getHeaders() {
                return Collections.emptyMap();
            }

            @Override
            public void finish(Getter.Response response) {

            }
        }
    }

}
