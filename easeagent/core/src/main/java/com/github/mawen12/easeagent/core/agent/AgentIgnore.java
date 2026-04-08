package com.github.mawen12.easeagent.core.agent;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class AgentIgnore {

    public static ElementMatcher<TypeDescription> ignored() {
        return isSynthetic()
                .or(nameStartsWith("java.").and(not(named("java.util.concurrent.ThreadPoolExecutor"))))
                .or(nameStartsWith("sun."))
                .or(nameStartsWith("javax."));
    }
}
