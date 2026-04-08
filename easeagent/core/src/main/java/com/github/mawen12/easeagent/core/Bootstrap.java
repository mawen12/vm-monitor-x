package com.github.mawen12.easeagent.core;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.core.agent.AgentIgnore;
import com.github.mawen12.easeagent.core.agent.AgentListener;
import com.github.mawen12.easeagent.core.context.ContextManagerImpl;
import com.github.mawen12.easeagent.core.plugins.demo.DemoTransformer;
import com.github.mawen12.easeagent.core.plugins.jdbc.JdbcTransformer;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

public class Bootstrap {

    public static void premain(String args, Instrumentation inst, String jarPath) throws Exception {
        Agent.contextManager = ContextManagerImpl.build();

        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .with(new AgentListener())
                .ignore(AgentIgnore.ignored());

        // jdbc
        agentBuilder = new JdbcTransformer().build(agentBuilder);

        // demo
        agentBuilder = new DemoTransformer().build(agentBuilder);

        agentBuilder.installOn(inst);
    }
}
