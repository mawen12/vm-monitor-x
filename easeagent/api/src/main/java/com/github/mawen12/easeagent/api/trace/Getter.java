package com.github.mawen12.easeagent.api.trace;

public interface Getter {

    String header(String name);

    interface Response extends Getter {

        interface HttpResponse extends Response {
            String method();

            String route();

            int statusCode();

            Throwable error();
        }
    }
}
