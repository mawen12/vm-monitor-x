package com.github.mawen12.easeagent.api.trace;

public interface Span {
    Span NOOP = NoOp.INSTANCE;

    boolean isNoop();

    Span name(String name);

    Span tag(String key, String value);

    Span annotate(String value);

    Span annotate(long timestamp, String value);

    Scope maybeScope();

    Span start();

    Span start(long timestamp);

    Span kind(Kind kind);

    Span error(Throwable throwable);

    Span remoteServiceName(String remoteServiceName);

    boolean remoteIpAndPort(String remoteIp, int remotePort);

    void abandon();

    void finish();

    void finish(long timestamp);

    void flush();

    enum Kind {
        CLIENT, SERVER, PRODUCER, CONSUMER
    }

    enum NoOp implements Span {
        INSTANCE;

        @Override
        public boolean isNoop() {
            return true;
        }

        @Override
        public Span name(String name) {
            return null;
        }

        @Override
        public Span tag(String key, String value) {
            return null;
        }

        @Override
        public Span annotate(String value) {
            return null;
        }

        @Override
        public Span annotate(long timestamp, String value) {
            return null;
        }

        @Override
        public Scope maybeScope() {
            return null;
        }

        @Override
        public Span start() {
            return null;
        }

        @Override
        public Span start(long timestamp) {
            return null;
        }

        @Override
        public Span kind(Kind kind) {
            return null;
        }

        @Override
        public Span error(Throwable throwable) {
            return null;
        }

        @Override
        public Span remoteServiceName(String remoteServiceName) {
            return null;
        }

        @Override
        public boolean remoteIpAndPort(String remoteIp, int remotePort) {
            return false;
        }

        @Override
        public void abandon() {

        }

        @Override
        public void finish() {

        }

        @Override
        public void finish(long timestamp) {

        }

        @Override
        public void flush() {

        }
    }
}
