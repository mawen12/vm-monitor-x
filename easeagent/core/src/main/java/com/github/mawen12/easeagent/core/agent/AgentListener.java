package com.github.mawen12.easeagent.core.agent;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

@EaseAgentClassLoader
public class AgentListener implements AgentBuilder.Listener {

    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//        System.out.printf("[agent/listener] onDiscovery %s\n", typeName);
    }

    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
//        System.out.printf("[agent/listener] onTransformation %s\n", typeDescription.getName());
    }

    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
//        System.out.printf("[agent/listener] onIgnored %s\n", typeDescription.getName());
    }

    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
        System.err.printf("[agent/listener] onError %s\n", typeName);
        throwable.printStackTrace();
    }

    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//        System.out.printf("[agent/listener] onComplete %s\n", typeName);
    }
}
