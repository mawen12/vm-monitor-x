package com.github.mawen12.easeagent.logging;

import org.slf4j.Logger;

import java.util.logging.Level;

import static com.github.mawen12.easeagent.logging.AgentLevel.*;
import static com.github.mawen12.easeagent.logging.AgentLevel.ERROR_VALUE;
import static com.github.mawen12.easeagent.logging.AgentLevel.WARN_VALUE;

public class Slf4jLogger extends java.util.logging.Logger {

    private final Logger logger;

    protected Slf4jLogger(Logger logger) {
        super("", null);
        this.logger = logger;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public boolean isLoggable(Level level) {
        switch (level.intValue()) {
            case ERROR_VALUE:
                return logger.isErrorEnabled();
            case WARN_VALUE:
                return logger.isWarnEnabled();
            case INFO_VALUE:
                return logger.isInfoEnabled();
            case DEBUG_VALUE:
                return logger.isDebugEnabled();
            case TRACE_VALUE:
                return logger.isTraceEnabled();
            default:
                return false;
        }
    }

    @Override
    public void log(Level level, String msg) {
        switch (level.intValue()) {
            case ERROR_VALUE:
                logger.error(msg);
            case WARN_VALUE:
                logger.warn(msg);
            case INFO_VALUE:
                logger.info(msg);
            case DEBUG_VALUE:
                logger.debug(msg);
            case TRACE_VALUE:
                logger.trace(msg);
            default:
        }
    }

    @Override
    public void log(Level level, String msg, Object param1) {
        switch (level.intValue()) {
            case ERROR_VALUE:
                logger.error(msg, param1);
            case WARN_VALUE:
                logger.warn(msg, param1);
            case INFO_VALUE:
                logger.info(msg, param1);
            case DEBUG_VALUE:
                logger.debug(msg, param1);
            case TRACE_VALUE:
                logger.trace(msg, param1);
            default:
        }
    }

    @Override
    public void log(Level level, String msg, Object[] params) {
        switch (level.intValue()) {
            case ERROR_VALUE:
                logger.error(msg, params);
            case WARN_VALUE:
                logger.warn(msg, params);
            case INFO_VALUE:
                logger.info(msg, params);
            case DEBUG_VALUE:
                logger.debug(msg, params);
            case TRACE_VALUE:
                logger.trace(msg, params);
            default:
        }
    }

    @Override
    public void log(Level level, String msg, Throwable thrown) {
        switch (level.intValue()) {
            case ERROR_VALUE:
                logger.error(msg, thrown);
            case WARN_VALUE:
                logger.warn(msg, thrown);
            case INFO_VALUE:
                logger.info(msg, thrown);
            case DEBUG_VALUE:
                logger.debug(msg, thrown);
            case TRACE_VALUE:
                logger.trace(msg, thrown);
            default:
        }
    }
}
