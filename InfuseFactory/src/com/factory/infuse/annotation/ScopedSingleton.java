package com.factory.infuse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as singleton local for an activity, fragment or service.
 *
 * @author Lazu Ioan-Bogdan
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScopedSingleton { }
