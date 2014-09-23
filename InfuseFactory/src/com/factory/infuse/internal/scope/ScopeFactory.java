package com.factory.infuse.internal.scope;

public class ScopeFactory {	
	public static LocalScope getNewLocalScope() {
		return new LocalScope();
	}
	
	private static GlobalScope global = new GlobalScope(); 
	public static GlobalScope getGlobalScope() {
		return global;
	}
}
