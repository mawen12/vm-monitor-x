package com.github.mawen12.easeagent.core;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.bean.BeanProvider;
import com.github.mawen12.easeagent.core.agent.AgentIgnore;
import com.github.mawen12.easeagent.core.agent.AgentListener;
import com.github.mawen12.easeagent.core.agent.transformer.ClassTransformer;
import com.github.mawen12.easeagent.core.config.ConfigFactory;
import com.github.mawen12.easeagent.core.context.ContextManagerImpl;
import com.github.mawen12.easeagent.core.metrics.MetricRegistryManagerImpl;
import com.github.mawen12.easeagent.core.metrics.MetricServer;
import com.github.mawen12.easeagent.core.utils.NetUtils;
import com.github.mawen12.easeagent.core.utils.ServiceLoaderUtils;
import net.bytebuddy.agent.builder.AgentBuilder;
import sun.nio.ch.Net;

import java.lang.instrument.Instrumentation;
import java.util.List;

public class Bootstrap {

    public static ClassLoader LOADER;

    public static void premain(String args, Instrumentation inst, String jarPath) throws Exception {
        Bootstrap.LOADER = Thread.currentThread().getContextClassLoader();

        Agent.config = ConfigFactory.loadConfig(jarPath, Bootstrap.LOADER);
        if (Agent.config != null) {
            Agent.additionalAttributes.put("service", Agent.config.getString("name"));
            Agent.additionalAttributes.put("system", Agent.config.getString("system"));
            Agent.additionalAttributes.put("host_ipv4", NetUtils.getHostIpV4());
            Agent.additionalAttributes.put("host_name", NetUtils.getHostName());
        }


        Agent.contextManager = ContextManagerImpl.build();
        Agent.metricRegistryManager = MetricRegistryManagerImpl.build();

        new MetricServer(19090).start();

        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .with(new AgentListener())
                .ignore(AgentIgnore.ignored());

        List<BeanProvider> beanProviders = ServiceLoaderUtils.load(BeanProvider.class);
        for (BeanProvider beanProvider : beanProviders) {
            beanProvider.afterPropertiesSet();
        }

        List<ClassTransformer> transformers = ServiceLoaderUtils.load(ClassTransformer.class);
        for (ClassTransformer transformer : transformers) {
            agentBuilder = transformer.build(agentBuilder);
        }

        agentBuilder.installOn(inst);
    }
}
