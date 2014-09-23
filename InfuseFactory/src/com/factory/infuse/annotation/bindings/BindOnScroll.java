package com.factory.infuse.annotation.bindings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.widget.AbsListView.OnScrollListener;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindOnScroll {
	Class<? extends OnScrollListener> value();
	int id() default -1;
}
