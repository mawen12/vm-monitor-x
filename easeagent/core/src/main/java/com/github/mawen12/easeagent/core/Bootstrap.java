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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EaseAgentClassLoader("from loader/Main then switch class loader to access")
public class Bootstrap {
    private static final Logger LOGGER = new AgentLogger(LogManager.getLogger(Bootstrap.class));

    public static ClassLoader LOADER;

    public static void premain(String args, Instrumentation inst, String jarPath) throws Exception {
        long begin = System.nanoTime();
        Bootstrap.LOADER = Thread.currentThread().getContextClassLoader();
        Agent.config = ConfigFactory.loadConfig(jarPath, Bootstrap.LOADER);

        initLoggerLevel();

        LOGGER.info("agent premain start, jarPath: {}", jarPath);

        Agent.loggerFactory = new AgentLoggerFactory();

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
                .with(AgentListener.INSTANCE)
                .ignore(AgentIgnore.ignored());

        List<BeanProvider> beanProviders = ServiceLoaderUtils.load(BeanProvider.class);
        for (BeanProvider beanProvider : beanProviders) {
            Agent.addListener(beanProvider.onState(), beanProvider::afterPropertiesSet);
            LOGGER.info("register BeanProvider: {} on {}", beanProvider.getClass().getSimpleName(), beanProvider.onState().name());
        }

        Agent.markStart();

        List<ClassTransformer> transformers = ServiceLoaderUtils.load(ClassTransformer.class);
        for (ClassTransformer transformer : transformers) {
            agentBuilder = transformer.build(agentBuilder);
            LOGGER.info("register ClassTransformer: {}", transformer.getClass().getSimpleName());
        }

        long installBegin = System.currentTimeMillis();
        agentBuilder.installOn(inst);

        LOGGER.info("installBegin use time: {}ms", (System.currentTimeMillis() - installBegin));

        LOGGER.info("initialization has took {}ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - begin));
    }

    private static void initLoggerLevel() {
        String level = Agent.config.getString("logging.level");
        Level loggerLevel = Level.INFO;
        switch (level) {
            case "OFF":
            case "off":
                loggerLevel = Level.OFF;
                break;
            case "FATAL":
            case "fatal":
                loggerLevel = Level.FATAL;
                break;
            case "ERROR":
            case "error":
                loggerLevel = Level.ERROR;
                break;
            case "DEBUG":
            case "debug":
                loggerLevel = Level.DEBUG;
                break;
            case "TRACE":
            case "trace":
                loggerLevel = Level.TRACE;
                break;
        }

        Configurator.setLevel("com.github.mawen12.easeagent", loggerLevel);
    }
}
