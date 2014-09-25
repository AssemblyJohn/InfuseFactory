package com.factory.infuse.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

import android.content.Context;

import com.factory.InfuseFactory;
import com.factory.infuse.annotation.Instantiate;
import com.factory.infuse.annotation.ScopedSingleton;
import com.factory.infuse.annotation.Singleton;

public class InfuseReflection {
	// Class scope type
	public static enum ScopeType {
		// Always legal
		SINGLETON_GLOBAL,
		// Illegal for non-scoped infuser
		SINGLETON_SCOPED,
		// Default class type
		NONE,
	}
	
	public void fieldIterator(Class<?> clazz, FieldConsumer consumer) {
		Field fields[] = clazz.getDeclaredFields();
		
		for(int i = 0, size = fields.length; i < size; i++) {
			Field f = fields[i];
			
			try {
				f.setAccessible(true);
				
				if(consumer.acceptsField(f)) {
					consumer.processField(f);
				}
			} catch (IllegalAccessException e) {
				if(InfuseFactory.DEBUG) e.printStackTrace();
			} catch (IllegalArgumentException e) {
				if(InfuseFactory.DEBUG) e.printStackTrace();
			} catch (ClassCastException e) {
				if(InfuseFactory.DEBUG) e.printStackTrace();
			}
		}
	}
	
	public ScopeType extractClassScope(Class<?> clazz) {
		if(clazz.isAnnotationPresent(Singleton.class)) {
			return ScopeType.SINGLETON_GLOBAL;
		} else if(clazz.isAnnotationPresent(ScopedSingleton.class)) {
			return ScopeType.SINGLETON_SCOPED;
		} else {
			return ScopeType.NONE;			
		}
	}
	
	/**
	 * Returns a instance of the provided class. If TOUGH is on an
	 * exception is thrown, else a null value is returned in case of an error.
	 * 
	 * @param clazz
	 * 				Blueprint for the instance.
	 * @param arguments
	 * 				Arguments for the class constructor. It is the caller's
	 * 				responsibility to populate them.
	 * @return A instance of the provided class. 
	 */
	public <T> T getClassInstance(Class<T> clazz, Map<Class<?>, Object> arguments) {
		T result = null;
		
		// Get the constructor used for instantiation
		Constructor<T> constructor = getUsedConstructor(clazz);

		// Parameters
		Class<?> parameters[] = constructor.getParameterTypes();

		// Arguments
		Object args[] = new Object[parameters.length];

		for (int i = 0; i < args.length; i++) {
			Object arg = arguments.get(parameters[i]);

			// If we do not have an needed argument
			if(arg == null) {
				try {
					throw new RuntimeException(
						"A supplied argument is null for the" +
						" parameter type: " + parameters[i].getName() + 
						" for the class: " + clazz.getSimpleName() + 
						" with constructor parameter list: " + Arrays.toString(constructor.getParameterTypes()) +
						" The problem can be: wrong constructor annotated," +
						" class not marked as scoped singleton," +
						" inner class in the wrong place," +
						" constructor requires an singleton argument that was not instantiated first with getInstance()," +
						" application not populating a context argument.");
				} catch(RuntimeException e) {
					if(InfuseFactory.TOUGH) throw e;
					else e.printStackTrace();
				}
			}
			
			args[i] = arg;
		}
		
		boolean error = true;

		// Try instantiate the object
		try {
			result = (T) constructor.newInstance(args);
			error = false;
		} catch (InstantiationException e) {
			if(InfuseFactory.TOUGH) e.printStackTrace();
		} catch (IllegalAccessException e) {
			if(InfuseFactory.TOUGH) e.printStackTrace();
		} catch (IllegalArgumentException e) {
			if(InfuseFactory.TOUGH) e.printStackTrace();
		} catch (InvocationTargetException e) {
			if(InfuseFactory.TOUGH) e.printStackTrace();
		}

		if(error) {
			try {
				throw new RuntimeException("Could not create instance for the class '" + 
						clazz.getSimpleName() + "' with the provided constructor!" +
						" Maybe an exception was thrown in the constructor");
			} catch(RuntimeException e) {
				if(InfuseFactory.TOUGH) throw e;
				else e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * <p>Constructor priority: <br>
	 * 1. Instantiate constructor <br>
	 * 2. Context constructor <br>
	 * 3. Default no-arg constructor <br>
	 * 
	 * <p>Will return the used constructor or will throw an runtime error.
	 * If the class is a inner class that is not static a instance of the
	 * outer class is required for it's argument list.
	 */
	@SuppressWarnings("unchecked")
	private <T> Constructor<T> getUsedConstructor(Class<T> clazz) {
		Constructor<?> constructors[] = clazz.getDeclaredConstructors();
		
		// 1. Annotated constructors
		for (int i = 0; i < constructors.length; i++) {
			if(constructors[i].isAnnotationPresent(Instantiate.class)) {
				// If the constructor has the annotation it's ours
				constructors[i].setAccessible(true);
				return (Constructor<T>) constructors[i];
			}
		}

		Class<?> enclosing = classRequiresEnclosingType(clazz);
		
		// Parameter types for context and default
		Class<?> context[]; Class<?> noarg[];
		
		// 2. Context constructor
		// 3. Default no-arg constructor
		if(enclosing == null) {
			context = new Class<?>[1]; context[0] = Context.class;
			noarg = null;
		} else {
			context = new Class<?>[2]; context[0] = enclosing; context[1] = Context.class;
			noarg = new Class<?>[1]; noarg[0] = enclosing;
		}
		
		// Try extract context, then default
		Constructor<?> usedConstructor = extractConstructor(clazz, context);
		if(usedConstructor == null) {
			usedConstructor = extractConstructor(clazz, noarg);
		}

		// If we didn't found anything...
		if(usedConstructor == null) {
			try {
				throw new RuntimeException(
						"No constructor found for class: " + clazz.getSimpleName() +
						". Does the class have a default, annotated or a context" +
						" constructor?");
			} catch(RuntimeException e) {
				if(InfuseFactory.TOUGH) throw e;
				else e.printStackTrace();
			}
		}

		// Set the constructor accessible so we don't get funny stuff
		usedConstructor.setAccessible(true);
		return (Constructor<T>) usedConstructor;
	}
	
	private Constructor<?> extractConstructor(Class<?> clazz, Class<?>... types) {
		try {
			return clazz.getDeclaredConstructor(types);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	private Class<?> classRequiresEnclosingType(Class<?> clazz) {
		Class<?> enclosing = clazz.getEnclosingClass();
		boolean requiresOuterInstance = false;
		
		if(enclosing != null) {
			// If we have an enclosing class and we are NOT static we require the enclosing argument 
			requiresOuterInstance = (!Modifier.isStatic(clazz.getModifiers()));
		}
		
		if(requiresOuterInstance) {
			return enclosing;
		} else {
			return null;
		}
	}
	
	public static interface FieldConsumer {
		/** 
		 * Processes a field. The field will be set to accessible and
		 * this function will be wrapped to process any errors.  
		 */
		public void processField(Field f) throws IllegalAccessException;
		
		/**
		 * If the field can be accepted.
		 */
		public boolean acceptsField(Field f) throws IllegalAccessException, IllegalArgumentException;
	}
}
