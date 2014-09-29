package com.factory.infuse;


public interface Scope {	
	/**
	 * Marks a instance to this scope.
	 * 
	 * @param clazz
	 * 				Class to mark as scoped.
	 * @param instance
	 * 				Instance linked to that class.
	 */
	public void markScoped(Class<?> clazz, Object instance);
	
	/**
	 * Obtains a instance of the object from this scope or null if the class was not marked as scoped.
	 */
	public Object obtainScoped(Class<?> clazz);
	
	/**
	 * Peeks in the scope, returning true if the scope contains the class
	 * instance and false if it does not.
	 */
	public boolean peekScoped(Class<?> clazz);
	
	public void clearScope();
}
