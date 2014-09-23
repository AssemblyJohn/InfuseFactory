package com.factory.infuse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.app.Activity;

import com.factory.InfuseFactory;

/**
 * Class for using with the fields that should be
 * instantiated, using {@link InfuseFactory#infuseViews(Activity)}
 * 
 * @author Lazu Ioan-Bogdan
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InfuseView {
	int value();
}
