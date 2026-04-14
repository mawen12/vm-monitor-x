package com.github.mawen12.easeagent.logging;

public class AgentLevel extends java.util.logging.Level {

    public static final int OFF_VALUE = 1;
    public static final int FATAL_VALUE = 2;
    public static final int ERROR_VALUE = 3;
    public static final int WARN_VALUE = 4;
    public static final int INFO_VALUE = 5;
    public static final int DEBUG_VALUE = 6;
    public static final int TRACE_VALUE = 7;
    public static final int ALL_VALUE = 8;

    public static final AgentLevel OFF = new AgentLevel("OFF", OFF_VALUE);
    public static final AgentLevel FATAL = new AgentLevel("FATAL", FATAL_VALUE);
    public static final AgentLevel ERROR = new AgentLevel("ERROR", ERROR_VALUE);
    public static final AgentLevel WARN = new AgentLevel("WARN", WARN_VALUE);
    public static final AgentLevel INFO = new AgentLevel("INFO", INFO_VALUE);
    public static final AgentLevel DEBUG = new AgentLevel("DEBUG", DEBUG_VALUE);
    public static final AgentLevel TRACE = new AgentLevel("TRACE", TRACE_VALUE);
    public static final AgentLevel ALL = new AgentLevel("ALL", ALL_VALUE);

    protected AgentLevel(String name, int value) {
        super(name, value);
    }
}
