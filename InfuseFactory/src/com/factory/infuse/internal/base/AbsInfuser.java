package com.factory.infuse.internal.base;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import android.view.View;

import com.factory.infuse.Infuser;
import com.factory.infuse.annotation.Infuse;
import com.factory.infuse.annotation.InfuseView;
import com.factory.infuse.internal.InfuseReflection;
import com.factory.infuse.internal.InfuseReflection.FieldConsumer;
import com.factory.infuse.internal.ViewResolver;
import com.factory.infuse.internal.lock.SharedLock;
import com.factory.infuse.internal.scope.GlobalScope;
import com.factory.infuse.internal.scope.ScopeFactory;

public abstract class AbsInfuser implements Infuser {	
	// Reflection utils
	protected InfuseReflection reflection;	
	
	// Global scope
	protected GlobalScope globalScope;
	
	// Shared lock
	protected Lock lock;
	
	public AbsInfuser() {
		reflection = new InfuseReflection();
		
		globalScope = ScopeFactory.getGlobalScope();
		lock = SharedLock.getSharedLock();
	}
	
	/**
	 * Resolves a raw instance, without having it's members infused.
	 * 
	 * <p>The instance resolving should be different on the global
	 * and local infuser.
	 */
	protected abstract <T> T resolveInstance(Class<T> clazz);
	
	/**
	 * Same ass {@link #resolveInstance(Class)} but it will make sure
	 * this instance is a GLOBAL singleton.
	 */
	protected abstract <T> T resolveInstanceSingleton(Class<T> clazz);
	
	/**
	 * Same as {@link #resolveInstance(Class)} but this should 
	 * be able to resolve instances for inner non-static classes too.
	 * 
	 * <p>In practice we just have to add to the supplied arguments of
	 * the method {@link InfuseReflection#getClassInstance(Class, java.util.Map)}
	 * this outerInstance @param.
	 */
	protected abstract <T> T resolveInstance(Class<T> clazz, Object outerInstance);
	
	/**
	 * Builds the map if arguments for a provided instance.
	 * 
	 * @param additional
	 * 				Additional arguments for the map.
	 */
	protected abstract Map<Class<?>, Object> buildArguments(Object... additional);
	
	/**
	 * Tests if a instance exists in any scope.
	 */
	protected abstract boolean existsGlobally(Class<?> clazz);
	
	/**
	 * If the instance exists it is safe to extract it.
	 */
	protected abstract Object extractGlobally(Class<?> clazz);
	
	@Override
	public final <T> T getSingletonInstance(Class<T> clazz) {
		lock.lock();
		try {
			T instance = resolveInstance(clazz);
			infuseMembers(instance);
			
			return instance;
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public final <T> T getInstance(Class<T> clazz) {
		lock.lock();
		try {
			T instance = resolveInstance(clazz);
			infuseMembers(instance);
			
			return instance;
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void infuseMembers(Object object) {
		infuseMembersInner(object);
	}
	
	@Override
	public void infuseViews(Object object, View v) {
		infuseViewsResolver(object, new ViewResolver(v));
	}
	
	/**
	 * Infuses the object's members. Will resolve inner
	 * non-static classes if they require a instance of the 
	 * parent class.
	 * 
	 * <p>All the object's fields that were marked with
	 * {@link Infuse} will also have their members infused.
	 */
	protected void infuseMembersInner(final Object object) {
		reflection.fieldIterator(object.getClass(), new FieldConsumer() {
			@Override
			public void processField(Field f) throws IllegalAccessException {				
				Object instance = resolveInstance(f.getType(), object);
				// Infuse it's members recursively
				infuseMembersInner(instance);
				
				// Finally set it.
				f.set(object, instance);
			}
			
			@Override
			public boolean acceptsField(Field f) throws IllegalAccessException, IllegalArgumentException {
				return f.isAnnotationPresent(Infuse.class) && f.get(object) == null;
			}
		});	
	}
	
	/**
	 * Infuses the views of the object with the values retrieved
	 * from the provided view resolver.
	 */
	protected void infuseViewsResolver(final Object object, final ViewResolver resolver) {
		reflection.fieldIterator(object.getClass(), new FieldConsumer() {
			@Override
			public void processField(Field f) throws IllegalAccessException {
				InfuseView view = f.getAnnotation(InfuseView.class);
				f.set(object, resolver.resolveView(view.value()));
			}
			
			@Override
			public boolean acceptsField(Field f) throws IllegalAccessException, IllegalArgumentException {
				return (f.isAnnotationPresent(InfuseView.class) 
						&& f.get(object) == null);
			}
		});
	}
}
