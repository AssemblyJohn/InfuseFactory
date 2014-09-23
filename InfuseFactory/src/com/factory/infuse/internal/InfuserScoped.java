package com.factory.infuse.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.factory.infuse.Infuser;
import com.factory.infuse.annotation.Infuse;
import com.factory.infuse.annotation.InfuseView;
import com.factory.infuse.annotation.bindings.BindAdapter;
import com.factory.infuse.annotation.bindings.BindOnClick;
import com.factory.infuse.annotation.bindings.BindOnItemClick;
import com.factory.infuse.annotation.bindings.BindOnItemLongClick;
import com.factory.infuse.annotation.bindings.BindOnScroll;
import com.factory.infuse.annotation.bindings.BindOnTouch;
import com.factory.infuse.internal.InfuseReflection.FieldConsumer;
import com.factory.infuse.internal.InfuseReflection.ScopeType;
import com.factory.infuse.internal.base.AbsInfuser;
import com.factory.infuse.internal.scope.LocalScope;
import com.factory.infuse.internal.scope.ScopeFactory;

public class InfuserScoped extends AbsInfuser implements Infuser {
	// Map for scoped views 
	private Map<Integer, View> scopedViews;
	private LocalScope localScope;
	
	@SuppressLint("UseSparseArrays") 
	public InfuserScoped() {
		super();
		
		scopedViews = new HashMap<Integer, View>();
		localScope = ScopeFactory.getNewLocalScope();
	}
	
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
	protected <T> T resolveInstanceSingleton(Class<T> clazz) {
		if(globalScope.peekScoped(clazz)) {
			return (T) globalScope.obtainScoped(clazz);
		} else {
			return getInstanceWithScope(clazz, buildArguments(), ScopeType.SINGLETON_GLOBAL);
		}
	}
	
	
	
	@Override
	public void infuseViews(Fragment fragment) {
		ViewResolver resolver = new ViewResolver(fragment);
		
		infuseViewsScoped(fragment, resolver);
		resolveBindings(fragment, resolver);
	}

	@Override
	public void infuseViews(Activity activity) {
		ViewResolver resolver = new ViewResolver(activity);
		
		infuseViewsScoped(activity, resolver);
		resolveBindings(activity, resolver);
	}

	@Override
	public void infuseMembers(Fragment fragment) {
		if(localScope.peekScoped(Fragment.class) == false) {
			localScope.markScoped(Fragment.class, fragment);
		}
		
		if(localScope.peekScoped(Activity.class) == false) {
			localScope.markScoped(Activity.class, fragment.getActivity());
		}
		
		infuseMembers((Object)fragment);
	}

	@Override
	public void infuseMembers(Activity activity) {
		if(localScope.peekScoped(Activity.class) == false) {
			localScope.markScoped(Activity.class, activity);
		}
		
		infuseMembers((Object)activity);
	}
	
	private void resolveBindings(final Object object, final ViewResolver resolver) {
		reflection.fieldIterator(object.getClass(), new FieldConsumer() {			
			@Override
			public void processField(Field f) throws IllegalAccessException {
				List<Annotation> anotations = Arrays.asList(f.getAnnotations());

				for (Annotation annotation : anotations) {
					Class<?> type = annotation.annotationType();
					processBindingAnnotation(type, object, f, resolver);
				}
			}
			
			@Override
			public boolean acceptsField(Field f) throws IllegalAccessException, IllegalArgumentException {
				return true;
			}
		});
	}
	
	private void processBindingAnnotation(Class<?> type, Object object, Field f, ViewResolver resolver) throws IllegalAccessException, IllegalArgumentException {		
		if(type.equals(BindOnClick.class)) {
			BindOnClick ant = f.getAnnotation(BindOnClick.class);

			// The view has not been found yet
			if(f.get(object) == null) {
				resolveView(object, f, ant.id(), resolver);
			}
			
			Object listener = getInstance(ant.value());
			infuseViewsScoped(listener, resolver);
			
			// Set the view's listener
			((View)f.get(object)).setOnClickListener((OnClickListener)listener);
		} else if(type.equals(BindOnItemClick.class)) {
			BindOnItemClick ant = f.getAnnotation(BindOnItemClick.class);

			if(f.get(object) == null) {
				resolveView(object, f, ant.id(), resolver);
			}
			
			Object listener = getInstance(ant.value());
			infuseViewsScoped(listener, resolver);

			((ListView)f.get(object)).setOnItemClickListener((OnItemClickListener)listener);
		} else if(type.equals(BindAdapter.class)) {
			BindAdapter ant = f.getAnnotation(BindAdapter.class);

			if(f.get(object) == null) {
				resolveView(object, f, ant.id(), resolver);
			}
			
			Object listener = getInstance(ant.value());
			infuseViewsScoped(listener, resolver);

			((ListView)f.get(object)).setAdapter((ListAdapter)listener);
		} else if(type.equals(BindOnItemLongClick.class)) {
			BindOnItemLongClick ant = f.getAnnotation(BindOnItemLongClick.class);

			if(f.get(object) == null) {
				resolveView(object, f, ant.id(), resolver);
			}
			
			Object listener = getInstance(ant.value());
			infuseViewsScoped(listener, resolver);

			((ListView)f.get(object)).setOnItemLongClickListener((OnItemLongClickListener)listener);
		} else if(type.equals(BindOnScroll.class)) {
			BindOnScroll ant = f.getAnnotation(BindOnScroll.class);

			if(f.get(object) == null) {
				resolveView(object, f, ant.id(), resolver);
			}
			
			Object listener = getInstance(ant.value());
			infuseViewsScoped(listener, resolver);

			((ListView)f.get(object)).setOnScrollListener((OnScrollListener)listener);						
		} else if(type.equals(BindOnTouch.class)) {
			BindOnTouch ant = f.getAnnotation(BindOnTouch.class);

			if(f.get(object) == null) {
				resolveView(object, f, ant.id(), resolver);
			}
			
			Object listener = getInstance(ant.value());
			infuseViewsScoped(listener, resolver);

			((View)f.get(object)).setOnTouchListener((OnTouchListener)listener);
		}
	}
	
	private void infuseViewsScoped(final Object object, final ViewResolver resolver) {
		reflection.fieldIterator(object.getClass(), new FieldConsumer() {
			@Override
			public void processField(Field f) throws IllegalAccessException {
				if((f.isAnnotationPresent(InfuseView.class) 
						&& f.get(object) == null)) {
					InfuseView view = f.getAnnotation(InfuseView.class);
					resolveView(object, f, view.value(), resolver);
				} else if(f.isAnnotationPresent(Infuse.class)) {
					// Infuse the views of the field marked with @Infuse
					infuseViewsScoped(f.get(object), resolver);
				}
			}
			
			@Override
			public boolean acceptsField(Field f) throws IllegalAccessException, IllegalArgumentException {				
				return true;
			}
		});
	}
	
	private void resolveView(Object object, Field f, int value, ViewResolver resolver) throws IllegalAccessException, IllegalArgumentException {
		if(scopedViews.containsKey(value) == false) {
			View v = resolver.resolveView(value);
			scopedViews.put(value, v);
			
			f.set(object, v);
		} else {
			f.set(object, scopedViews.get(value));
		}	
	}
	
	protected boolean existsGlobally(Class<?> clazz) {
		return globalScope.peekScoped(clazz) || localScope.peekScoped(clazz);
	}
	
	protected Object extractGlobally(Class<?> clazz) {
		if(globalScope.peekScoped(clazz)) return globalScope.obtainScoped(clazz);
		else if(localScope.peekScoped(clazz)) return localScope.obtainScoped(clazz);
		else throw new IllegalStateException("Scould never happen!");
	}
	
	@Override
	protected Map<Class<?>, Object> buildArguments(Object... additional) {
		Map<Class<?>, Object> args = new HashMap<Class<?>, Object>();
		
		args.putAll(localScope.obtainScope());
		args.putAll(globalScope.obtainScope());
		for (int i = 0; i < additional.length; i++) {
			args.put(additional[i].getClass(), additional[i]);
		}
		
		return args;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getInstanceWithScope(Class<T> clazz, Map<Class<?>, Object> arguments, ScopeType scope) {
		T result = null;
		
		if(scope == ScopeType.SINGLETON_GLOBAL && globalScope.peekScoped(clazz)) {
			return (T)globalScope.obtainScoped(clazz);
		} else if(scope == ScopeType.SINGLETON_SCOPED && localScope.peekScoped(clazz)) {
			return (T)localScope.obtainScoped(clazz);
		}
		
		result = reflection.getClassInstance(clazz, arguments);
		
		if(scope == ScopeType.SINGLETON_GLOBAL) {
			globalScope.markScoped(clazz, result);
		} else if(scope == ScopeType.SINGLETON_SCOPED) {
			localScope.markScoped(clazz, result);
		}
		
		return result;
	}
}
