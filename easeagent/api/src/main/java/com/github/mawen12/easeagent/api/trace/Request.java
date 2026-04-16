package com.github.mawen12.easeagent.api.trace;

public interface Request extends Getter, Setter {

    Span.Kind kind();

    String name();

    boolean cacheScope();

    interface HttpRequest extends Request {
        default String name() {
            return method();
        }

        String method();

        String path();

        String route();

        String getRemoteAddr();

        int getRemotePort();
    }
}
