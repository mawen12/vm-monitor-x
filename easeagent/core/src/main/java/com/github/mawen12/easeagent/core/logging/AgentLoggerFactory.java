package com.github.mawen12.easeagent.core.logging;

import com.github.mawen12.easeagent.api.logging.Logger;
import com.github.mawen12.easeagent.api.logging.LoggerFactory;
import org.apache.logging.log4j.LogManager;

public class AgentLoggerFactory implements LoggerFactory {

    @Override
    public Logger getLogger(String name) {
        return new AgentLogger(LogManager.getLogger(name));
    }
}
