package com.factory.infuse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to declare which of the constructors is used. If no annotation is present
 * a constructor that takes a context as a parameter or the default constructor is used.
 * 
 * <p>
 * The order is: <br>
 * 	- annotation <br>
 * 	- context constructor <br>
 * 	- default no-argument constructor <br>
 * </p>
 * 
 * @author Lazu Ioan-Bogdan
 */

@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface Instantiate { }
