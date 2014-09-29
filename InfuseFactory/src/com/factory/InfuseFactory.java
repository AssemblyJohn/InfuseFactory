package com.factory;

import android.content.Context;

import com.factory.android.InfuseApplication;
import com.factory.infuse.InfuseCreator;
import com.factory.infuse.Infuser;

/**
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class InfuseFactory {	
	public static boolean DEBUG = true;
	public static boolean TOUGH = true;
	
	private static InfuseCreator creator = new InfuseCreator();
	
	/**
	 * Sets the global context. Do not call manually. Will be set by {@link InfuseApplication}.
	 */
	public static void setContext(Context context) {
		creator.setContext(context);
	}
	
	/**
	 * Clears all the shared global instances.
	 */
	public static void clearInstances() {
		creator.clearGlobalInstances();
	}
	
	/**
	 * Obtains a scoped infuser. It is stored in a weak hash map. When all other
	 * references to it disappear it will be removed from the map. 
	 * 
	 * <p>Usually references to a scoped infuser are held in a 
	 * fragment or activity so when those are destroyed the scoped
	 * infuser should also be destroyed. 
	 * 
	 * <p>This method is called by internal mechanisms. There should be no need to
	 * call this manually.
	 * 
	 * @param key
	 * 				Key for that infuser. The activity should be used as a key for
	 * 				the infuser.
	 * @return	A scoped infuser.
	 */
	public static Infuser getScopedInfuser(Object key) {
		return creator.obtainScopedInfuser(key);
	}
	
//	public static void deleteScopedInfuser(Object key) {
//		creator.deleteScopedInfuser(key);
//	}
	
	/**
	 * Obtains the global infuser. If any manual instantiation or member
	 * infusion is necessarily this infuser should be used.
	 * 
	 * @return	The global shared infuser.
	 */
	public static Infuser getInfuser() {
		return creator.obtainGlobalInfuser();
	}
}
