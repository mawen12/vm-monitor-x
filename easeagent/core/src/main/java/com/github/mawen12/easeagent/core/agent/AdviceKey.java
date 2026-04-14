package com.github.mawen12.easeagent.core.agent;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@EaseAgentClassLoader
@Retention(RetentionPolicy.RUNTIME)
public @interface AdviceKey {
}
