package com.factory.infuse;

import java.util.Map;
import java.util.WeakHashMap;

import android.content.Context;

import com.factory.infuse.internal.InfuserGlobal;
import com.factory.infuse.internal.InfuserScoped;
import com.factory.infuse.internal.scope.ScopeFactory;

public class InfuseCreator {	
	private Map<Object, Infuser> scopedInfusers;
	
	private Infuser globalInfuser;	
	private Scope scopeGlobal;
	private Context context;
	
	public InfuseCreator() {
		scopedInfusers = new WeakHashMap<Object, Infuser>();
		
		globalInfuser = InfuserGlobal.getInfuserInstance();
		scopeGlobal = ScopeFactory.getGlobalScope();
	}
	
	// Clear and populate the global scope
	public void clearGlobalInstances() {
		scopeGlobal.clearScope();
		scopeGlobal.markScoped(Context.class, context);
	}
	
	// Set the context and populate the global scope
	public void setContext(Context context) {
		this.context = context; 
		clearGlobalInstances();
	}
	
	public Infuser obtainScopedInfuser(Object key) {
		if(scopedInfusers.containsKey(key)) {
			return scopedInfusers.get(key);
		} else {
			Infuser infuser = new InfuserScoped();
			scopedInfusers.put(key, infuser);
			
			return infuser;
		}
	}
	
//	public void deleteScopedInfuser(Object key) {
//		if(scopedInfusers.containsKey(key)) {
//			scopedInfusers.remove(key);
//		}
//	}
	
	public Infuser obtainGlobalInfuser() {
		return globalInfuser;
	}
}
