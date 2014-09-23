package com.factory.infuse;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.factory.InfuseFactory;
import com.factory.infuse.annotation.Infuse;
import com.factory.infuse.annotation.InfuseView;
import com.factory.infuse.annotation.Instantiate;
import com.factory.infuse.annotation.ScopedSingleton;
import com.factory.infuse.annotation.Singleton;

public interface Infuser {	
	/**
	 * Same as {@link #getInstance(Class)} but this
	 * will make sure the returned instance is a global singleton.
	 * 
	 * <p>The singleton will be global even if this infuser is global
	 * or scoped.
	 */
	public <T> T getSingletonInstance(Class<T> clazz);

	/**
	 * A new instance of the specified class will be returned.
	 * If the class is marked with a {@link Singleton} or {@link ScopedSingleton} 
	 * annotation a cached instance is returned. 
	 * 
	 * <p> Note that the {@link ScopedSingleton} annotation cannot be used with
	 * the global infuser, but only with the scoped infuser.
	 * 
	 * <p>For it's fields marked with {@link Infuse} or for the constructor that was 
	 * marked with {@link Instantiate} the map of supplied global arguments that 
	 * were set with {@link InfuseFactory#setContext(android.content.Context)} is used 
	 * and also all the singleton instances that were created along the way.
	 * 
	 * <p>If we really really can't annotate the class with {@link Singleton} use
	 * {@link #getSingletonInstance(Class)} instead.
	 * 
	 * @param clazz
	 * 				Class that is to be instantiated.
	 * @return	Instance of the desired class, and will all it's fields infused.
	 */
	public <T> T getInstance(Class<T> clazz);

	/**
	 * Infuses all the fields annotated with {@link Infuse}
	 * of the provided object. 
	 * 
	 * <p>After a {@link #getInstance(Class)} or {@link #getSingletonInstance(Class)}
	 * there is no need to call this manually. It is automatically called for you
	 * just before returning you the object.
	 * 
	 * <p>It is safe to call manually.
	 * 
	 * <p>You cannot use {@link ScopedSingleton} if this 
	 * infuser is the global infuser. In that case and exception 
	 * is thrown or printed, based on the debug options.
	 * 
	 * @param object
	 * 				Object with fields for infusion.
	 */
	public void infuseMembers(Object object);
	
	/**
	 * Infuses all the fields of the object that have the {@link InfuseView} annotation
	 * with the views {@link View#findViewById(id)}. 
	 * 
	 * <p>Can safely be called manually. It is quite useful for view holders.
	 * 
	 * <p>Bindings are NOT resolved.
	 * 
	 * @param object
	 * 				Object to infuse the fields.
	 * @param v
	 * 				View to use when infusing the views.
	 */
	public void infuseViews(Object object, View v);

	
	/**
	 * <p>Do not call this method on the global infuser! It will throw an exception.
	 * 
	 * Infuses all the views of an fragment. It will use {@link Fragment#getView()}
	 * to look them up. Will also resolve all the view's bindings. 
	 * 
	 * <p>No need to call this manually, it is called automatically by the
	 * framework.
	 * 
	 * <p>Since this infuse is in the scope of the fragment any binding class
	 * can have view infusions and can be marked as a {@link ScopedSingleton}.
	 */
	public void infuseViews(Fragment fragment);
	
	/**
	 * <p>Do not call this method on the global infuser! It will throw an exception.
	 * 
	 * Infuses all the views of an activity. It will use {@link Activity#findViewById(int)}
	 * to look them up. Will also resolve all the view's bindings. 
	 * 
	 * <p>No need to call this manually, it is called automatically by the
	 * framework.
	 * 
	 * <p>Since this infuse is in the scope of the activity any binding class
	 * can have view infusions and can be marked as a {@link ScopedSingleton}.
	 */
	public void infuseViews(Activity activity);

	/**
	 * <p>Do not call this method on the global infuser! It will throw an exception.
	 * 
	 * Infuses all members of this fragment. The infused classes can be marked as
	 * a {@link ScopedSingleton}. They will remain in the scope of this fragment
	 * and will be singletons only in this scope, but not globally.
	 */
	public void infuseMembers(Fragment fragment);
	
	/**
	 * <p>Do not call this method on the global infuser! It will throw an exception.
	 * 
	 * Infuses all members of this activity. The infused classes can be marked as
	 * a {@link ScopedSingleton}. They will remain in the scope of this activity
	 * and will be singletons only in this scope, but not globally.
	 */
	public void infuseMembers(Activity activity);
}
