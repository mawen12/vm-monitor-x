package com.github.mawen12.easeagent.api.field;

public interface DynamicFieldAccessor {

    void setAgent$$Field$$Data(Object data);

    Object getAgent$$Field$$Data();

    static <T> T getDynamicFieldValue(Object target) {
        if (target instanceof DynamicFieldAccessor) {
            return (T) ((DynamicFieldAccessor) target).getAgent$$Field$$Data();
        }
        return null;
    }

    static void setDynamicFieldValue(Object target, Object value) {
        if (target instanceof DynamicFieldAccessor) {
            ((DynamicFieldAccessor) target).setAgent$$Field$$Data(value);
        }
    }
}
