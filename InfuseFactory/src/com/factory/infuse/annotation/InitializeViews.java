package com.factory.infuse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If this is present on an object's method named "initializeViews"
 * that takes 0 arguments, then that method is called after
 * it's views are infused. Good for view state initialization.
 * 
 * @author John
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InitializeViews { }
