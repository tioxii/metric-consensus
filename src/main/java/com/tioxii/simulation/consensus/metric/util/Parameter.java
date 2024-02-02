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

    /**
     * If this is set to true, the field will be initialized by the {@link com.tioxii.simulation.consensus.metric.init.ParameterManager ParameterManager}.
     * @return true if the field should be initialized by the {@link com.tioxii.simulation.consensus.metric.init.ParameterManager ParameterManager}, false otherwise.
     */
    boolean isParameter() default false;

    /**
     * The name of the parameter. This is used to identify the parameter in the {@link com.tioxii.simulation.consensus.metric.init.ParameterManager ParameterManager}.
     * @return The name of the parameter.
     */
    String name() default "";
}
