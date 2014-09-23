package com.factory.infuse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.factory.InfuseFactory;

/**
 * Class for using with the fields that should be
 * instantiated, using {@link InfuseFactory#infuseMembers(Object)}
 * 
 * @author Lazu Ioan-Bogdan
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Infuse {}
