package com.tioxii.simulation.consensus.metric.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark fields, so that they can be initialized by the {@link com.tioxii.simulation.consensus.metric.init.ParameterManager ParameterManager}.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
    boolean isParameter() default false;
    String name() default "";
}
