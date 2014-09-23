package com.factory.infuse.internal;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.factory.InfuseFactory;
import com.factory.infuse.Infuser;
import com.factory.infuse.internal.InfuseReflection.ScopeType;
import com.factory.infuse.internal.base.AbsInfuser;

public class InfuserGlobal extends AbsInfuser implements Infuser {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T resolveInstance(Class<T> clazz) {
		if(existsGlobally(clazz)) {
			return (T)extractGlobally(clazz);
		}
		
		ScopeType scope = reflection.extractClassScope(clazz);
		
		if(scope == ScopeType.SINGLETON_GLOBAL) {
			return resolveInstanceSingleton(clazz);
		} else {
			return getInstanceWithScope(clazz, buildArguments(), scope); 
		}
	}		
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T resolveInstance(Class<T> clazz, Object outerInstance) {
		if(existsGlobally(clazz)) {
			return (T)extractGlobally(clazz);
		}
		
		ScopeType scope = reflection.extractClassScope(clazz);
		
		if(scope == ScopeType.SINGLETON_GLOBAL) {
			return resolveInstanceSingleton(clazz);
		} else {
			return getInstanceWithScope(clazz, buildArguments(outerInstance), scope);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T resolveInstanceSingleton(Class<T> clazz) {
		if(globalScope.peekScoped(clazz)) {
			return (T) globalScope.obtainScoped(clazz);
		} else {
			return getInstanceWithScope(clazz, buildArguments(), ScopeType.SINGLETON_GLOBAL);
		}
	}
	
	protected boolean existsGlobally(Class<?> clazz) {
		return globalScope.peekScoped(clazz);
	}
	
	protected Object extractGlobally(Class<?> clazz) {
		if(globalScope.peekScoped(clazz)) return globalScope.obtainScoped(clazz);
		else throw new IllegalStateException("Scould never happen!");
	}
	
	@Override
	protected Map<Class<?>, Object> buildArguments(Object... additional) {
		Map<Class<?>, Object> args = new HashMap<Class<?>, Object>();
		
		args.putAll(globalScope.obtainScope());
		for (int i = 0; i < additional.length; i++) {
			args.put(additional[i].getClass(), additional[i]);
		}
		
		return args;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getInstanceWithScope(Class<T> clazz, Map<Class<?>, Object> arguments, ScopeType scope) {
		if(scope == ScopeType.SINGLETON_SCOPED) {
			try {
				throw new RuntimeException("@ScopedSingleton not allowed for" +
						" global infuser for class: " + clazz.getSimpleName() +
						" . Use @Singleton instead.");
			} catch(RuntimeException e) {
				if(InfuseFactory.TOUGH) throw e;
				else if(InfuseFactory.DEBUG) e.printStackTrace();
			}
		}
		
		T result = null;
		
		if(scope == ScopeType.SINGLETON_GLOBAL && globalScope.peekScoped(clazz)) {
			return (T)globalScope.obtainScoped(clazz);
		}
		
		result = reflection.getClassInstance(clazz, arguments);
		
		if(scope == ScopeType.SINGLETON_GLOBAL) {
			globalScope.markScoped(clazz, result);
		}
		
		return result;
	}
	
	public void infuseMembers(Fragment fragment) {
		throw new UnsupportedOperationException("Must be called on a scoped infuser!");
	}	
	
	public void infuseViews(Fragment fragment) {
		throw new UnsupportedOperationException("Must be called on a scoped infuser!");
	}
	
	public void infuseMembers(Activity activity) {
		throw new UnsupportedOperationException("Must be called on a scoped infuser!");
	}
	
	public void infuseViews(Activity activity) {
		throw new UnsupportedOperationException("Must be called on a scoped infuser!");		
	}

	private static Infuser instance = new InfuserGlobal();
	public static Infuser getInfuserInstance() {
		return instance;
	}
}
