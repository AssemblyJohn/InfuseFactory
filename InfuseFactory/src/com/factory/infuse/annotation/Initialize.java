package com.factory.infuse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If this is present on an object's method named "initialize"
 * that takes 0 arguments then this method is invoked after
 * the retrieving of the instance takes place.
 * 
 * @author John
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Initialize { }
