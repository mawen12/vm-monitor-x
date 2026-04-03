package com.github.mawen12.easeagent.core;

import com.github.mawen12.easeagent.core.agent.AgentIgnore;
import com.github.mawen12.easeagent.core.agent.AgentListener;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

public class Bootstrap {

    public static void premain(String args, Instrumentation inst, String jarPath) throws Exception {
        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .with(new AgentListener())
                .ignore(AgentIgnore.ignored());

        agentBuilder.installOn(inst);
    }
}
