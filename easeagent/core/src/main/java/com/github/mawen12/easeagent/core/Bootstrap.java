package com.github.mawen12.easeagent.core;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.logging.Logger;
import com.github.mawen12.easeagent.api.spi.BeanProvider;
import com.github.mawen12.easeagent.core.agent.AgentIgnore;
import com.github.mawen12.easeagent.core.agent.AgentListener;
import com.github.mawen12.easeagent.core.agent.spi.ClassTransformer;
import com.github.mawen12.easeagent.core.config.ConfigFactory;
import com.github.mawen12.easeagent.core.context.ContextManagerImpl;
import com.github.mawen12.easeagent.core.logging.AgentLogger;
import com.github.mawen12.easeagent.core.logging.AgentLoggerFactory;
import com.github.mawen12.easeagent.core.metrics.MetricRegistryManagerImpl;
import com.github.mawen12.easeagent.core.metrics.MetricServer;
import com.github.mawen12.easeagent.core.utils.NetUtils;
import com.github.mawen12.easeagent.core.utils.ServiceLoaderUtils;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.apache.logging.log4j.LogManager;

import java.lang.instrument.Instrumentation;
import java.util.List;

@EaseAgentClassLoader("from loader/Main then switch class loader to access")
public class Bootstrap {
    private static final Logger LOGGER = new AgentLogger(LogManager.getLogger(Bootstrap.class));

    public static ClassLoader LOADER;

    public static void premain(String args, Instrumentation inst, String jarPath) throws Exception {
        Bootstrap.LOADER = Thread.currentThread().getContextClassLoader();

        LOGGER.info("agent premain start, jarPath: {}", jarPath);
        LOGGER.info("{}", System.getProperty("user.dir"));

        Agent.loggerFactory = new AgentLoggerFactory();
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
            Agent.addListener(beanProvider.onState(), beanProvider::afterPropertiesSet);
        }

        Agent.markStart();

        List<ClassTransformer> transformers = ServiceLoaderUtils.load(ClassTransformer.class);
        for (ClassTransformer transformer : transformers) {
            agentBuilder = transformer.build(agentBuilder);
        }

        agentBuilder.installOn(inst);
    }
}
