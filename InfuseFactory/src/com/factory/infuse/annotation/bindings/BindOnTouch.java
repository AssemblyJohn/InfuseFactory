package com.factory.infuse.annotation.bindings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View.OnTouchListener;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindOnTouch {
	Class<? extends OnTouchListener> value();
	int id() default -1;
}
