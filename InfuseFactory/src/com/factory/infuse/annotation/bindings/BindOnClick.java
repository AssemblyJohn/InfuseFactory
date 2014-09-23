package com.factory.infuse.annotation.bindings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View.OnClickListener;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindOnClick {
	Class<? extends OnClickListener> value();
	int id() default -1;
}
