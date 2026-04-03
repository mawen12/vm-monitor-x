package com.github.mawen12.easeagent.api.interceptor;

public class MethodInfo {
    private Object invoker;
    private String type;
    private String method;
    private Object[] args;
    private Throwable throwable;
    private Object retValue;

    public MethodInfo(Object invoker, String type, String method, Object[] args) {
        this.invoker = invoker;
        this.type = type;
        this.method = method;
        this.args = args;
    }

    public Object getInvoker() {
        return invoker;
    }

    public String getType() {
        return type;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Object getRetValue() {
        return retValue;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public void setRetValue(Object retValue) {
        this.retValue = retValue;
    }
}
