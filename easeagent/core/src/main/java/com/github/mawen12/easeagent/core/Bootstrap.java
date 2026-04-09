package com.github.mawen12.easeagent.core;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.core.agent.AgentIgnore;
import com.github.mawen12.easeagent.core.agent.AgentListener;
import com.github.mawen12.easeagent.core.context.ContextManagerImpl;
import com.github.mawen12.easeagent.core.metrics.MetricRegistryManagerImpl;
import com.github.mawen12.easeagent.core.plugins.demo.DemoTransformer;
import com.github.mawen12.easeagent.core.plugins.jdbc.transformer.JdbcConnectionTransformer;
import com.github.mawen12.easeagent.core.plugins.jdbc.transformer.JdbcDataSourceTransformer;
import com.github.mawen12.easeagent.core.plugins.jdbc.transformer.JdbcStatementTransformer;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

public class Bootstrap {

    public static ClassLoader LOADER;

    public static void premain(String args, Instrumentation inst, String jarPath) throws Exception {
        System.out.println("load Timer from bootstrap result is " + Class.forName("com.github.mawen12.easeagent.api.metrics.Metric"));

        Bootstrap.LOADER = Thread.currentThread().getContextClassLoader();
        Agent.contextManager = ContextManagerImpl.build();
        Agent.metricRegistryManager = MetricRegistryManagerImpl.build();

        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .with(new AgentListener())
                .ignore(AgentIgnore.ignored());

        // jdbc
        agentBuilder = new JdbcDataSourceTransformer().build(agentBuilder);
        agentBuilder = new JdbcConnectionTransformer().build(agentBuilder);
        agentBuilder = new JdbcStatementTransformer().build(agentBuilder);

        // demo
        agentBuilder = new DemoTransformer().build(agentBuilder);

        agentBuilder.installOn(inst);
    }
}
