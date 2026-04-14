package com.github.mawen12.easeagent.core.agent;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;

@EaseAgentClassLoader
@AllArgsConstructor
public class AdviceKeyOffsetMapping implements Advice.OffsetMapping {

    private final String adviceKey;

    @Override
    public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Advice.ArgumentHandler argumentHandler, Sort sort) {
        return new Target.ForStackManipulation(new TextConstant(this.adviceKey));
    }

    @AllArgsConstructor
    public static class Factory implements Advice.OffsetMapping.Factory<AdviceKey> {
        private final String adviceKey;

        @Override
        public Class<AdviceKey> getAnnotationType() {
            return AdviceKey.class;
        }

        @Override
        public Advice.OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<AdviceKey> annotation, AdviceType adviceType) {
            return new AdviceKeyOffsetMapping(this.adviceKey);
        }
    }
}
