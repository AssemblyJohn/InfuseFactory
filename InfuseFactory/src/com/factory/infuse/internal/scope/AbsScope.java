package com.factory.infuse.internal.scope;

import java.util.HashMap;
import java.util.Map;

import com.factory.infuse.Scope;

public abstract class AbsScope implements Scope {
	private Map<Class<?>, Object> scoped = new HashMap<Class<?>, Object>();
	
	/**
	 * Marks a instance to this scope.
	 * 
	 * @param clazz
	 * 				Class to mark as scoped.
	 * @param instance
	 * 				Instance linked to that class.
	 */
	public void markScoped(Class<?> clazz, Object instance) {
		if(!clazz.isInstance(instance)) throw new IllegalArgumentException("Instance not type of provided class!");
		
		scoped.put(clazz, instance);
	}
	
	/**
	 * Obtains a instance of the object from this scope or null if the class was not marked as scoped.
	 */
	public Object obtainScoped(Class<?> clazz) {
		return scoped.get(clazz);
	}
	
	@Override
	public boolean peekScoped(Class<?> clazz) {
		return scoped.containsKey(clazz);
	}
	
	/** Careful with this one, it returns the real list. */
	public Map<Class<?>, Object> obtainScope() {
		return scoped;
	}
	
	public void clearScope() {
		scoped.clear();
	}
}
